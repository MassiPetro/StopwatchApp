package com.petrogallimassimo.stopwatchempatica.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.petrogallimassimo.stopwatchempatica.MainViewModel
import com.petrogallimassimo.stopwatchempatica.R
import com.petrogallimassimo.stopwatchempatica.databinding.FragmentLeaderboardBinding
import com.petrogallimassimo.stopwatchempatica.ui.adapter.FootballPlayerStatisticsAdapter

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

        footballPlayersStatisticsAdapter.replaceItems(sharedViewModel.footballPlayerStatisticsModelList)
    }

    private fun setListeners() {
        binding.chipExpl.setOnClickListener {
            footballPlayersStatisticsAdapter.sortByPeakSpeed()

        }

        binding.chipEnd.setOnClickListener {
            footballPlayersStatisticsAdapter.sortByLaps()
        }
    }
}