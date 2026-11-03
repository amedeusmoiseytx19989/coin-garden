package com.Lirvoke2.Wezhuk.core.ui

fun formatPoints(value: Int): String {
    val digits = value.toString()
    if (digits.length <= 3) {
        return digits
    }
    val offset = digits.length % 3
    val builder = StringBuilder()
    digits.forEachIndexed { index, char ->
        if (index > 0 && (index - offset) % 3 == 0) {
            builder.append(' ')
        }
        builder.append(char)
    }
    return builder.toString()
}

fun formatCountdown(millis: Long): String {
    val total = (millis.coerceAtLeast(0L) + 999L) / 1000L
    val hours = total / 3600L
    if (hours <= 0L) {
        return formatClock(millis)
    }
    val minutes = (total % 3600L) / 60L
    val builder = StringBuilder()
    if (hours < 10L) {
        builder.append('0')
    }
    builder.append(hours)
    builder.append(':')
    if (minutes < 10L) {
        builder.append('0')
    }
    builder.append(minutes)
    return builder.toString()
}

fun formatClock(millis: Long): String {
    val total = (millis.coerceAtLeast(0L) + 999L) / 1000L
    val minutes = total / 60L
    val seconds = total % 60L
    val builder = StringBuilder()
    if (minutes < 10L) {
        builder.append('0')
    }
    builder.append(minutes)
    builder.append(':')
    if (seconds < 10L) {
        builder.append('0')
    }
    builder.append(seconds)
    return builder.toString()
}
