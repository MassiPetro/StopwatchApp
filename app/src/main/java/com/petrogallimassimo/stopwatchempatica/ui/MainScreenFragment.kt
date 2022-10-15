package com.petrogallimassimo.stopwatchempatica.ui

import android.os.Bundle
import android.text.Layout.Directions
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.petrogallimassimo.stopwatchempatica.R
import com.petrogallimassimo.stopwatchempatica.databinding.FragmentMainScreenBinding
import com.petrogallimassimo.stopwatchempatica.datasource.ApiFactory
import com.petrogallimassimo.stopwatchempatica.datasource.ApiService
import com.petrogallimassimo.stopwatchempatica.datasource.Repository

class MainScreenFragment : Fragment() {

    private val repository = Repository(ApiFactory.buildService(ApiService::class.java))

    private val viewModel: MainViewModel by viewModels {
        MainViewModel.ViewModelFactory(repository)
    }
    private lateinit var binding: FragmentMainScreenBinding
    private lateinit var footballPlayersAdapter: FootballPlayersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainScreenBinding.inflate(inflater, container, false)

        viewModel.getFootballPlayers()
        setView()
        setObservers()

        return binding.root
    }

    private fun setObservers() {
        viewModel.footballPlayersLiveData.observe(viewLifecycleOwner) {
            footballPlayersAdapter.replaceItems(it)
            Log.d("FOOTBALLPLAYERS", footballPlayersAdapter.itemCount.toString())
        }
    }

    private fun setView() {
        footballPlayersAdapter = FootballPlayersAdapter(requireContext()) { player ->
            viewModel.selectedPlayer = player
            findNavController().navigate(R.id.action_mainScreenFragment_to_inputFragment)
        }
        binding.rvFootballPlayers.adapter = footballPlayersAdapter
        val itemDecoration = MaterialDividerItemDecoration(requireContext(), VERTICAL)
        itemDecoration.dividerColor =
            resources.getColor(R.color.md_theme_light_primaryContainer, activity?.theme)
        itemDecoration.dividerInsetStart = 10
        itemDecoration.dividerInsetEnd = 10
        binding.rvFootballPlayers.addItemDecoration(itemDecoration)
    }

}