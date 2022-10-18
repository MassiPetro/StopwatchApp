package com.petrogallimassimo.stopwatchempatica.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.petrogallimassimo.stopwatchempatica.MainViewModel
import com.petrogallimassimo.stopwatchempatica.R
import com.petrogallimassimo.stopwatchempatica.StopwatchHelper
import com.petrogallimassimo.stopwatchempatica.databinding.FragmentSessionBinding
import com.petrogallimassimo.stopwatchempatica.ui.adapter.LapsAdapter
import kotlinx.coroutines.launch
import java.util.*

class SessionFragment : Fragment() {

    private lateinit var binding: FragmentSessionBinding
    private val viewModel: SessionViewModel by viewModels()
    private val sharedViewModel: MainViewModel by activityViewModels()

    private lateinit var stopwatchHelper: StopwatchHelper
    private val lapsAdapter = LapsAdapter()

    private val timer = Timer()
    private var previousTime = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSessionBinding.inflate(inflater, container, false)

        stopwatchHelper = StopwatchHelper(requireContext())
        setViews()
        setListeners()
        runStopWatch()

        return binding.root
    }

    private fun setViews() {
        with(binding) {
            setPlayerDetails()
            rvLaps.adapter = lapsAdapter
            val itemDecoration =
                MaterialDividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
            itemDecoration.dividerColor =
                resources.getColor(R.color.md_theme_light_primaryContainer, activity?.theme)
            itemDecoration.dividerInsetStart = 10
            itemDecoration.dividerInsetEnd = 10
            itemDecoration.isLastItemDecorated = false
            rvLaps.addItemDecoration(itemDecoration)
        }
        setMetrics()
    }

    private fun setListeners() {
        with(binding) {
            btnStart.setOnClickListener {
                startStopAction()
            }
            btnLap.setOnClickListener {
                resetLapAction()
            }
        }

    }

    private fun setPlayerDetails() {
        with(binding) {
            itemFootballPlayer.profileImage.let {
                Glide.with(requireContext())
                    .load(sharedViewModel.selectedPlayer.large)
                    .into(it)
            }

            itemFootballPlayer.title.text = sharedViewModel.selectedPlayer.title
            itemFootballPlayer.firstName.text = sharedViewModel.selectedPlayer.first
            itemFootballPlayer.lastName.text = sharedViewModel.selectedPlayer.last
        }
    }

    private fun runStopWatch() {
        if (stopwatchHelper.timerCounting()) {
            startTimer()
        } else {
            stopTimer()
            if (stopwatchHelper.startTime() != null && stopwatchHelper.stopTime() != null) {
                val time = Date().time - calcRestartTime().time
                binding.stopwatch.text = viewModel.timeStringFromLong(time)
            }
        }

        timer.scheduleAtFixedRate(TimeTask(), 0, 500)
    }

    private inner class TimeTask : TimerTask() {
        override fun run() {
            lifecycleScope.launch {
                if (stopwatchHelper.timerCounting()) {
                    val time = Date().time - stopwatchHelper.startTime()!!.time
                    binding.stopwatch.text = viewModel.timeStringFromLong(time)
                }
            }
        }
    }

    private fun lapAction() {
        lapsAdapter.addLap(getTime())
        viewModel.lapSeconds =
            viewModel.lapSeconds.plus(viewModel.secondsFromString(getTime()) - previousTime)
        previousTime = viewModel.secondsFromString(getTime())
    }

    private fun resetAction() {
        stopwatchHelper.setStopTime(null)
        stopwatchHelper.setStartTime(null)
        stopTimer()
        lapsAdapter.clearLaps()
        binding.stopwatch.text = viewModel.timeStringFromLong(0)
    }

    private fun stopTimer() {
        stopwatchHelper.setTimerCounting(false)
        binding.btnStart.text = getString(R.string.start)
        binding.btnLap.text = getString(R.string.reset)
    }

    private fun startTimer() {
        stopwatchHelper.setTimerCounting(true)
        binding.btnStart.text = getString(R.string.stop)
        binding.btnLap.text = getString(R.string.lap)
    }

    private fun startStopAction() {
        if (stopwatchHelper.timerCounting()) {
            stopwatchHelper.setStopTime(Date())
            stopTimer()
        } else {
            if (stopwatchHelper.stopTime() != null) {
                stopwatchHelper.setStartTime(calcRestartTime())
                stopwatchHelper.setStopTime(null)
            } else {
                stopwatchHelper.setStartTime(Date())
            }
            startTimer()
        }
    }

    private fun resetLapAction() {
        if (stopwatchHelper.timerCounting()) {
            lapAction()
        } else {
            resetAction()
        }
    }

    private fun calcRestartTime(): Date {
        val diff = stopwatchHelper.startTime()!!.time - stopwatchHelper.stopTime()!!.time
        return Date(System.currentTimeMillis() + diff)
    }

    private fun getTime(): String {
        return binding.stopwatch.text.toString()
    }

    private fun setMetrics() {
        with(binding) {
            lapsAdapter.lapsNumberLiveData.observe(viewLifecycleOwner) {
                tvLapsValue.text = it.toString()
                tvAvgTimeLapValue.text = viewModel.minutesStringFromLong(viewModel.avgTimeLap(it))
                tvAvgSpeedValue.text =
                    viewModel.avgSpeed(
                        sharedViewModel.selectedDistanceMeters,
                        viewModel.avgTimeLap(it)
                    )
                tvPeakSpeedValue.text =
                    viewModel.peakSpeed(
                        sharedViewModel.selectedDistanceMeters,
                        viewModel.secondsFromString(getTime())
                    )
                tvCadenceValue.text = viewModel.cadenceValue(it)
            }
        }
    }
}