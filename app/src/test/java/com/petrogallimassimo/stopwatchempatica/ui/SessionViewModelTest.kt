package com.petrogallimassimo.stopwatchempatica.ui

import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class SessionViewModelTest {

    private lateinit var viewModel: SessionViewModel
    private val lapSeconds = 30L

    @Before
    fun init() {
        viewModel = SessionViewModel()
    }

    @Test
    fun timeStringFromLong() {
        val result = viewModel.timeStringFromLong(1000L)
        Assert.assertEquals("00:00:01", result)
    }

    @Test
    fun minutesString() {
        val result = viewModel.minutesString(30L)
        Assert.assertEquals("00:30", result)
    }

    @Test
    fun secondsFromString() {
        val result = viewModel.secondsFromString("00:00:20")
        Assert.assertEquals(20L, result)
    }

    @Test
    fun avgTimeLap() {
        viewModel.lapSeconds = lapSeconds

        val result = viewModel.avgTimeLap(2)
        Assert.assertEquals(15L, result)
    }

    @Test
    fun avgSpeed() {
        val result = viewModel.avgSpeed(10.0, 5L)
        Assert.assertEquals("2 m/s", result)
    }

    @Test
    fun peakSpeed() {
        val result = viewModel.peakSpeed(10.0, 5L)
        Assert.assertEquals("2 m/s", result)
    }

    @Test
    fun cadenceValue() {
        viewModel.lapSeconds = lapSeconds

        val result = viewModel.cadenceValue(2)
        Assert.assertEquals("4", result)
    }


}