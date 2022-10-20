package com.petrogallimassimo.stopwatchempatica.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.petrogallimassimo.stopwatchempatica.MainViewModel
import com.petrogallimassimo.stopwatchempatica.R
import com.petrogallimassimo.stopwatchempatica.StopwatchHelper
import com.petrogallimassimo.stopwatchempatica.databinding.FragmentSessionBinding
import com.petrogallimassimo.stopwatchempatica.model.FootballPlayerStatisticsModel
import com.petrogallimassimo.stopwatchempatica.model.TrainingMetricsModel
import com.petrogallimassimo.stopwatchempatica.ui.adapter.LapsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@AndroidEntryPoint
class SessionFragment : Fragment() {

    private lateinit var binding: FragmentSessionBinding
    private val viewModel: SessionViewModel by viewModels()
    private val sharedViewModel: MainViewModel by activityViewModels()

    private lateinit var stopwatchHelper: StopwatchHelper
    private val lapsAdapter = LapsAdapter()

    private val timer = Timer()
    private var previousTime = 0L
    private val lineValues = ArrayList<Entry>()
    private var hasStatistics = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSessionBinding.inflate(inflater, container, false)

        hasStatistics = false
        viewModel.lapSeconds = 0
        viewModel.lapsTime = 0F

        stopwatchHelper = StopwatchHelper(requireContext())
        setViews()
        setListeners()
        setObservers()
        runStopWatch()
        resetAction()

        return binding.root
    }

    /**
     * Setup view
     */
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

        // override onbackpressed to save data
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    saveData()
                    findNavController().navigate(R.id.action_sessionFragment_to_mainScreenFragment)
                }
            }
        )
    }

    /**
     * Setup click listeners for buttons
     */
    private fun setListeners() {
        with(binding) {
            btnStart.setOnClickListener {
                startStopAction()
            }
            btnLap.setOnClickListener {
                resetLapAction()
            }
            btnSave.setOnClickListener {
                saveData()
                findNavController().navigate(R.id.action_sessionFragment_to_mainScreenFragment)
            }
        }
    }

    /**
     * Setup observer of football players statistics saved on DB
     */
    private fun setObservers() {
        sharedViewModel.footballPlayersStatsDB.observe(viewLifecycleOwner) {
            sharedViewModel.footballPlayerStatisticsModelList.clear()
            sharedViewModel.footballPlayerStatisticsModelList.addAll(it)
        }
    }

    /**
     *  Setup view with player informations
     */
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

    /**
     * Handle Stopwatch
     */
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

    /**
     * Get lap time
     */
    private fun lapAction() {
        lapsAdapter.addLap(getTime())
        viewModel.lapsTime = (viewModel.secondsFromString(getTime()) - previousTime).toFloat()
        viewModel.lapSeconds =
            viewModel.lapSeconds.plus(viewModel.secondsFromString(getTime()) - previousTime)
        previousTime = viewModel.secondsFromString(getTime())
    }

    /**
     * Reset timer
     */
    private fun resetAction() {
        stopwatchHelper.setStopTime(null)
        stopwatchHelper.setStartTime(null)
        stopTimer()
        lapsAdapter.clearLaps()
        binding.stopwatch.text = viewModel.timeStringFromLong(0)
    }

    /**
     * Stop timer
     */
    private fun stopTimer() {
        stopwatchHelper.setTimerCounting(false)
        binding.btnStart.text = getString(R.string.start)
        binding.btnLap.text = getString(R.string.reset)
    }

    /**
     * Start timer
     */
    private fun startTimer() {
        stopwatchHelper.setTimerCounting(true)
        binding.btnStart.text = getString(R.string.stop)
        binding.btnLap.text = getString(R.string.lap)
    }

    /**
     * Handle start and stop timer actions
     */
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

    /**
     * Handle reset and lap timer actions
     */
    private fun resetLapAction() {
        if (stopwatchHelper.timerCounting()) {
            lapAction()
        } else {
            resetAction()
        }
    }

    /**
     * Calculate time of restart
     */
    private fun calcRestartTime(): Date {
        val diff = stopwatchHelper.startTime()!!.time - stopwatchHelper.stopTime()!!.time
        return Date(System.currentTimeMillis() + diff)
    }

    /**
     * Get time from Stopwatch
     */
    private fun getTime(): String {
        return binding.stopwatch.text.toString()
    }

    /**
     * Setup view of Metrics
     */
    private fun setMetrics() {
        with(binding) {
            lapsAdapter.lapsNumberLiveData.observe(viewLifecycleOwner) {
                tvLapsValue.text = it.toString()
                tvAvgTimeLapValue.text = viewModel.minutesString(viewModel.avgTimeLap(it))
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

                setLineChart(viewModel.lapsTime, it.toFloat(), viewModel.avgTimeLap(it).toFloat())
            }
        }
    }

    /**
     * Setup Chart
     */
    private fun setLineChart(lapTime: Float, lapNumber: Float, avgTimeLap: Float) {
        lineValues.add(Entry(lapNumber, lapTime))

        val lineDataset = LineDataSet(lineValues, "Lap Times")

        lineDataset.color =
            resources.getColor(R.color.md_theme_light_primaryContainer, requireActivity().theme)

        lineDataset.circleRadius = 10f
        lineDataset.setDrawFilled(true)
        lineDataset.valueTextSize = 20F
        lineDataset.fillColor =
            resources.getColor(R.color.md_theme_light_primaryContainer, requireActivity().theme)
        lineDataset.setCircleColor(
            resources.getColor(
                R.color.md_theme_light_primary,
                requireActivity().theme
            )
        )
        lineDataset.mode = LineDataSet.Mode.CUBIC_BEZIER

        val avgTimeLine =
            LimitLine(avgTimeLap, "Avg. Time/Lap ${viewModel.minutesString(avgTimeLap)}")
        avgTimeLine.lineWidth = 4f
        avgTimeLine.lineColor = resources.getColor(
            R.color.md_theme_light_secondary,
            requireActivity().theme
        )
        avgTimeLine.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
        avgTimeLine.textSize = 10f

        val data = LineData(lineDataset)
        data.setValueFormatter(TimeFormatter())
        with(binding) {
            lineChart.description.isEnabled = false
            lineChart.xAxis.granularity = 1F
            lineChart.axisLeft.setDrawLimitLinesBehindData(true)
            lineChart.axisLeft.removeAllLimitLines()
            lineChart.axisLeft.addLimitLine(avgTimeLine)
            lineChart.data = data
            lineChart.invalidate()
            lineChart.visibility = VISIBLE
        }
    }

    /**
     * Save data on DB
     */
    private fun saveData() {
        hasStatistics = false
        stopwatchHelper.setStopTime(Date())
        stopTimer()
        sharedViewModel.footballPlayerStatisticsModelList.apply {
            if (this.isEmpty()) {
                this.add(
                    FootballPlayerStatisticsModel(
                        sharedViewModel.selectedPlayer,
                        TrainingMetricsModel(
                            peakSpeed = binding.tvPeakSpeedValue.text.toString(),
                            lapsNumber = binding.tvLapsValue.text.toString()
                        )
                    )
                )
            } else {
                this.forEach {
                    if (it.footballPlayer == sharedViewModel.selectedPlayer && !hasStatistics) {
                        hasStatistics = true
                        it.metrics = TrainingMetricsModel(
                            peakSpeed = binding.tvPeakSpeedValue.text.toString(),
                            lapsNumber = binding.tvLapsValue.text.toString()
                        )
                    }
                }
                if (!hasStatistics) {
                    hasStatistics = true
                    this.add(
                        FootballPlayerStatisticsModel(
                            sharedViewModel.selectedPlayer,
                            TrainingMetricsModel(
                                peakSpeed = binding.tvPeakSpeedValue.text.toString(),
                                lapsNumber = binding.tvLapsValue.text.toString()
                            )
                        )
                    )
                }
            }
        }

        lifecycleScope.launch {
            sharedViewModel.footballPlayerStatisticsModelList.forEach {
                withContext(Dispatchers.IO) {
                    sharedViewModel.insert(it)
                }
            }
        }
    }

    inner class TimeFormatter : ValueFormatter() {
        override fun getPointLabel(entry: Entry): String {
            return viewModel.minutesString(entry.y)
        }
    }
}



















