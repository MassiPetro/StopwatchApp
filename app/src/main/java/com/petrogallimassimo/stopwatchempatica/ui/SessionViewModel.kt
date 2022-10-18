package com.petrogallimassimo.stopwatchempatica.ui

import androidx.lifecycle.ViewModel
import java.text.DecimalFormat
import kotlin.math.round
import kotlin.math.truncate

class SessionViewModel : ViewModel() {

    var lapSeconds: Long = 0

    fun timeStringFromLong(ms: Long): String {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60) % 60)
        val hours = (ms / (1000 * 60 * 60) % 24)
        return makeTimeString(hours, minutes, seconds)
    }

    fun makeTimeString(hours: Long, minutes: Long, seconds: Long): String {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun secondsFromString(time: String): Long {
        val hours = time.substring(0, 2).toLong() * 360
        val minutes = time.substring(3, 5).toLong() * 60
        val seconds = time.substring(6).toLong()

        return hours + minutes + seconds
    }

    fun avgTimeLap(lapNumber: Int): Long {
        return lapSeconds / lapNumber
    }

    fun avgSpeed(distance: Double, avgTime: Long): String {
        val speed = distance / avgTime.toDouble()
        val decimalFormat = DecimalFormat("#.##")

        return decimalFormat.format(speed)
    }

}