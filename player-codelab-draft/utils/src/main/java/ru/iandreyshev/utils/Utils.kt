package ru.iandreyshev.utils

import android.view.View
import java.text.SimpleDateFormat
import java.util.*

fun Int.toHumanReadableTime(): String {
    val date = Date(this.toLong())
    val formatter = SimpleDateFormat("HH:mm:ss")
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(date)
}

fun View.disable() {
    isEnabled = false
    alpha = 0.25f
}

fun View.enable() {
    isEnabled = true
    alpha = 1f
}
