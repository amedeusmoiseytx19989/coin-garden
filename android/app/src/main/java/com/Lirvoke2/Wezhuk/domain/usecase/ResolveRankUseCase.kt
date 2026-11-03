package com.Lirvoke2.Wezhuk.domain.usecase

import com.Lirvoke2.Wezhuk.domain.model.Rank
import com.Lirvoke2.Wezhuk.domain.model.RankState

class ResolveRankUseCase {

    fun resolve(ladder: List<Rank>, xp: Int): RankState {
        val ordered = ladder.sortedBy { it.minXp }
        var current = ordered.first()
        var next: Rank? = null
        for (rank in ordered) {
            if (xp >= rank.minXp) {
                current = rank
            } else {
                next = rank
                break
            }
        }
        val span = (next?.minXp ?: current.minXp) - current.minXp
        val earned = xp - current.minXp
        return RankState(
            index = current.index,
            labelRes = current.labelRes,
            currentXp = if (span <= 0) earned else earned.coerceAtMost(span),
            targetXp = if (span <= 0) earned.coerceAtLeast(1) else span
        )
    }
}
