package com.petrogallimassimo.stopwatchempatica.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.petrogallimassimo.stopwatchempatica.MainConstants.ChipSelected
import com.petrogallimassimo.stopwatchempatica.MainViewModel
import com.petrogallimassimo.stopwatchempatica.R
import com.petrogallimassimo.stopwatchempatica.databinding.FragmentLeaderboardBinding
import com.petrogallimassimo.stopwatchempatica.ui.adapter.FootballPlayerStatisticsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LeaderboardFragment : Fragment() {

    private lateinit var binding: FragmentLeaderboardBinding
    private lateinit var footballPlayersStatisticsAdapter: FootballPlayerStatisticsAdapter

    private val sharedViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false)

        setViews()
        setListeners()
        setObservers()

        return binding.root
    }

    private fun setViews() {
        footballPlayersStatisticsAdapter = FootballPlayerStatisticsAdapter(requireContext())
        binding.rvFootballPlayersStatistics.adapter = footballPlayersStatisticsAdapter
        val itemDecoration = MaterialDividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        itemDecoration.dividerColor =
            resources.getColor(R.color.md_theme_light_primaryContainer, activity?.theme)
        itemDecoration.dividerInsetStart = 10
        itemDecoration.dividerInsetEnd = 10
        binding.rvFootballPlayersStatistics.addItemDecoration(itemDecoration)
        footballPlayersStatisticsAdapter.setChipSelected(ChipSelected.NONE)
    }

    private fun setListeners() {
        binding.chipExpl.setOnClickListener {
            footballPlayersStatisticsAdapter.sortByPeakSpeed()
            footballPlayersStatisticsAdapter.setChipSelected(ChipSelected.EXPLOSIVENESS)
        }

        binding.chipEnd.setOnClickListener {
            footballPlayersStatisticsAdapter.sortByLaps()
            footballPlayersStatisticsAdapter.setChipSelected(ChipSelected.ENDURANCE)
        }

        binding.floatingActionButton.setOnClickListener {
            sharedViewModel.converterJob?.cancel()
            sharedViewModel.convertDataToString(sharedViewModel.footballPlayerStatisticsModelList) {
                Toast.makeText(context, R.string.alert_csv, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setObservers() {
        sharedViewModel.footballPlayersStatsDB.observe(viewLifecycleOwner) {
            footballPlayersStatisticsAdapter.replaceItems(it)
            sharedViewModel.footballPlayerStatisticsModelList.clear()
            sharedViewModel.footballPlayerStatisticsModelList.addAll(it)
        }
    }
}