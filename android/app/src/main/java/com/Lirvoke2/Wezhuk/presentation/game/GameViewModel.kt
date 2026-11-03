package com.Lirvoke2.Wezhuk.presentation.game

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Lirvoke2.Wezhuk.core.config.GameConfig
import com.Lirvoke2.Wezhuk.data.sample.SampleData
import com.Lirvoke2.Wezhuk.domain.model.CoinPoint
import com.Lirvoke2.Wezhuk.domain.model.RoundResult
import com.Lirvoke2.Wezhuk.domain.model.TargetRing
import com.Lirvoke2.Wezhuk.domain.repository.PlayerRepository
import com.Lirvoke2.Wezhuk.domain.usecase.LaunchCoinUseCase
import com.Lirvoke2.Wezhuk.domain.usecase.UnlockCoinUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GameViewModel(
    private val players: PlayerRepository
) : ViewModel() {

    private val flight = LaunchCoinUseCase()
    private val unlockCoins = UnlockCoinUseCase()

    private val _state = MutableStateFlow(initialState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _events = Channel<RoundOutcome>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var loop: Job? = null
    private var elapsedMs = 0L
    private var flightMs = 0L
    private var resolveMs = 0L
    private var targetX = 0.5f
    private var markerId = 0
    private var reloadUsed = false
    private var finishing = false

    init {
        runLoop()
    }

    fun fireCoin(reticleX: Float) {
        val current = _state.value
        if (!current.canLaunch) {
            return
        }
        targetX = reticleX.coerceIn(GameConfig.SWEEP_MIN_X, GameConfig.SWEEP_MAX_X)
        flightMs = 0L
        _state.value = current.copy(
            phase = GamePhase.IN_FLIGHT,
            coinsLeft = current.coinsLeft - 1,
            shots = current.shots + 1,
            coin = flight.coinPoint(targetX, 0f),
            coinSpin = 0f,
            trail = emptyList(),
            marker = null,
            resolveProgress = 0f
        )
    }

    fun pause() {
        loop?.cancel()
        loop = null
        if (_state.value.phase != GamePhase.FINISHED) {
            _state.value = _state.value.copy(paused = true)
        }
    }

    fun resume() {
        if (_state.value.phase == GamePhase.FINISHED) {
            return
        }
        _state.value = _state.value.copy(paused = false)
        runLoop()
    }

    private fun runLoop() {
        if (loop != null || finishing) {
            return
        }
        loop = viewModelScope.launch {
            var last = SystemClock.elapsedRealtime()
            while (isActive) {
                delay(GameConfig.TICK_MS)
                val now = SystemClock.elapsedRealtime()
                val step = (now - last).coerceIn(0L, 80L)
                last = now
                val snapshot = _state.value
                if (snapshot.paused || snapshot.phase == GamePhase.FINISHED) {
                    continue
                }
                tick(step)
            }
        }
    }

    private fun tick(step: Long) {
        val current = _state.value
        elapsedMs += step
        var next = current.copy(
            remainingMs = (current.remainingMs - step).coerceAtLeast(0L),
            rings = flight.advanceRings(current.rings, step),
            reloadBannerLeftMs = (current.reloadBannerLeftMs - step).coerceAtLeast(0L)
        )

        when (current.phase) {
            GamePhase.IN_FLIGHT -> {
                flightMs += step
                val progress = (flightMs.toFloat() / GameConfig.FLIGHT_MS).coerceIn(0f, 1f)
                val point = flight.coinPoint(targetX, progress)
                next = next.copy(
                    coin = point,
                    coinSpin = flight.spinAt(progress),
                    trail = (listOf(point) + next.trail).take(GameConfig.TRAIL_LENGTH)
                )
                val ring = flight.detectHit(next.rings, point)
                next = when {
                    ring != null -> registerHit(next, ring, point)
                    progress >= 1f -> registerMiss(next, point)
                    else -> next
                }
            }

            GamePhase.RESOLVING -> {
                resolveMs += step
                val progress = (resolveMs.toFloat() / GameConfig.RESOLVE_MS).coerceIn(0f, 1f)
                next = if (progress >= 1f) {
                    next.copy(
                        phase = GamePhase.READY,
                        resolveProgress = 0f,
                        coin = null,
                        trail = emptyList(),
                        marker = null
                    )
                } else {
                    next.copy(resolveProgress = progress)
                }
            }

            else -> Unit
        }

        _state.value = next
        evaluateEnd()
    }

    private fun registerHit(
        current: GameUiState,
        ring: TargetRing,
        point: CoinPoint
    ): GameUiState {
        val streak = current.streak + 1
        val multiplier = flight.multiplierFor(streak)
        val gained = flight.pointsFor(ring.value, multiplier, current.skin.bonusFactor)
        markerId += 1
        resolveMs = 0L
        return current.copy(
            phase = GamePhase.RESOLVING,
            score = current.score + gained,
            streak = streak,
            bestStreak = maxOf(current.bestStreak, streak),
            multiplier = multiplier,
            ringsHit = current.ringsHit + 1,
            resolveProgress = 0f,
            marker = HitMarker(markerId, point.x, point.y, gained, true),
            rings = current.rings.map { candidate ->
                if (candidate.id == ring.id) {
                    candidate.copy(hidden = true, respawnLeftMs = GameConfig.RING_RESPAWN_MS)
                } else {
                    candidate
                }
            }
        )
    }

    private fun registerMiss(current: GameUiState, point: CoinPoint): GameUiState {
        markerId += 1
        resolveMs = 0L
        return current.copy(
            phase = GamePhase.RESOLVING,
            streak = 0,
            multiplier = 1,
            resolveProgress = 0f,
            marker = HitMarker(markerId, point.x, point.y, 0, false)
        )
    }

    private fun evaluateEnd() {
        if (finishing) {
            return
        }
        val current = _state.value
        val outOfTime = current.remainingMs <= 0L
        val bankEmpty = current.coinsLeft <= 0 && current.phase == GamePhase.READY
        if (bankEmpty && elapsedMs < GameConfig.MIN_ROUND_MS && !reloadUsed) {
            reloadUsed = true
            _state.value = current.copy(
                coinsLeft = GameConfig.RELOAD_BONUS,
                reloadBannerLeftMs = 2200L
            )
            return
        }
        if (outOfTime || bankEmpty) {
            finishRound()
        }
    }

    private fun finishRound() {
        finishing = true
        loop?.cancel()
        loop = null
        val current = _state.value
        _state.value = current.copy(phase = GamePhase.FINISHED, coin = null, trail = emptyList())
        val result = RoundResult(
            score = current.score,
            ringsHit = current.ringsHit,
            shots = current.shots,
            bestStreak = current.bestStreak
        )
        viewModelScope.launch {
            val before = players.profile()
            val saved = players.saveRound(result)
            val unlocked = unlockCoins.newlyUnlocked(
                SampleData.coinSkins,
                saved.unlockedSkinIds,
                saved.strikePoints
            )
            if (unlocked.isNotEmpty()) {
                players.unlockSkins(unlocked.map { it.id }.toSet())
            }
            _events.send(
                RoundOutcome(
                    result = result,
                    newBest = result.score > 0 && result.score > before.bestScore,
                    unlockedCoinLabel = unlocked.firstOrNull()?.labelRes ?: 0
                )
            )
        }
    }

    private fun initialState(): GameUiState {
        val profile = players.profile()
        return GameUiState(
            phase = GamePhase.READY,
            score = 0,
            streak = 0,
            bestStreak = 0,
            multiplier = 1,
            coinsLeft = GameConfig.COIN_BANK,
            coinBank = GameConfig.COIN_BANK,
            remainingMs = GameConfig.ROUND_DURATION_MS,
            rings = SampleData.initialRings(),
            coin = null,
            coinSpin = 0f,
            trail = emptyList(),
            marker = null,
            resolveProgress = 0f,
            ringsHit = 0,
            shots = 0,
            skin = SampleData.skinById(profile.equippedSkinId),
            reloadBannerLeftMs = 0L,
            paused = false
        )
    }
}
