package com.Lirvoke2.Wezhuk.domain.model

data class TargetRing(
    val id: Int,
    val laneY: Float,
    val sizeUnits: Int,
    val value: Int,
    val x: Float,
    val speed: Float,
    val direction: Int,
    val hidden: Boolean = false,
    val respawnLeftMs: Long = 0L
)

data class CoinPoint(val x: Float, val y: Float)

data class RingHit(val ring: TargetRing, val points: Int, val point: CoinPoint)
