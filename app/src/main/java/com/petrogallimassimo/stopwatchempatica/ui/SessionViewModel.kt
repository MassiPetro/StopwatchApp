package com.petrogallimassimo.stopwatchempatica.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import java.text.DecimalFormat

@ViewModelScoped
class SessionViewModel : ViewModel() {

    var lapSeconds: Long = 0
    var lapsTime = 0F
    private var smallestTime = Long.MAX_VALUE
    private var previousTime = 0L

    fun timeStringFromLong(ms: Long): String {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60) % 60)
        val hours = (ms / (1000 * 60 * 60) % 24)
        return makeTimeString(hours, minutes, seconds)
    }

    fun minutesString(sec: Long): String {
        val seconds = sec % 60
        val minutes = (sec / 60 % 60)
        return makeTimeString(minutes, seconds)
    }

    fun minutesString(sec: Float): String {
        val secLong = sec.toLong()
        val seconds = secLong % 60
        val minutes = (secLong / 60 % 60)
        return makeTimeString(minutes, seconds)
    }

    private fun makeTimeString(hours: Long, minutes: Long, seconds: Long): String {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun makeTimeString(minutes: Long, seconds: Long): String {
        return String.format("%02d:%02d", minutes, seconds)
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

        return truncateDecimal(speed) + " m/s"
    }

    fun peakSpeed(distance: Double, time: Long): String {
        if (smallestTime > (time - previousTime)) {
            smallestTime = time - previousTime
            previousTime = time
        }

        val speed = distance / smallestTime

        return truncateDecimal(speed) + " m/s"
    }

    fun cadenceValue(lapNumber: Int): String {
        val result = (lapNumber.toDouble() * 60) / lapSeconds

        return truncateDecimal(result)
    }

    private fun truncateDecimal(value: Double): String {
        val decimalFormat = DecimalFormat("#.##")
        return decimalFormat.format(value)
    }

}