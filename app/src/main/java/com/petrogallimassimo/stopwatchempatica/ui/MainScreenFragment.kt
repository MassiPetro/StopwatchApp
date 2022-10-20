package com.petrogallimassimo.stopwatchempatica.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.petrogallimassimo.stopwatchempatica.MainApplication
import com.petrogallimassimo.stopwatchempatica.MainViewModel
import com.petrogallimassimo.stopwatchempatica.R
import com.petrogallimassimo.stopwatchempatica.databinding.DialogInputDistanceBinding
import com.petrogallimassimo.stopwatchempatica.databinding.FragmentMainScreenBinding
import com.petrogallimassimo.stopwatchempatica.datasource.ApiFactory
import com.petrogallimassimo.stopwatchempatica.datasource.ApiService
import com.petrogallimassimo.stopwatchempatica.datasource.Repository
import com.petrogallimassimo.stopwatchempatica.ui.adapter.FootballPlayersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreenFragment : Fragment() {

    private val repository = Repository(ApiFactory.buildService(ApiService::class.java))


    private val viewModel: MainViewModel by activityViewModels {
        MainViewModel.ViewModelFactory(
            repository,
            (requireActivity().application as MainApplication).footballPlayerStatisticsRepository
        )
    }
    private lateinit var binding: FragmentMainScreenBinding
    private lateinit var footballPlayersAdapter: FootballPlayersAdapter
    private lateinit var materialAlertDialogBuilder: MaterialAlertDialogBuilder
    private lateinit var inputDistanceBinding: DialogInputDistanceBinding
    private lateinit var inputDistanceView: View

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

    /**
     * Setup observer to add items to Adapter
     */
    private fun setObservers() {
        viewModel.footballPlayersLiveData.observe(viewLifecycleOwner) {
            footballPlayersAdapter.replaceItems(it)
        }
    }

    /**
     * Setup view
     */
    private fun setView() {
        footballPlayersAdapter = FootballPlayersAdapter(requireContext()) { player ->
            viewModel.selectedPlayer = player
            inputDistanceView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_input_distance, null, false)
            inputDistanceBinding = DialogInputDistanceBinding.inflate(layoutInflater)
            showInputDistanceDialog()
        }
        binding.rvFootballPlayers.adapter = footballPlayersAdapter
        val itemDecoration = MaterialDividerItemDecoration(requireContext(), VERTICAL)
        itemDecoration.dividerColor =
            resources.getColor(R.color.md_theme_light_primaryContainer, activity?.theme)
        itemDecoration.dividerInsetStart = 10
        itemDecoration.dividerInsetEnd = 10
        binding.rvFootballPlayers.addItemDecoration(itemDecoration)

        materialAlertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
    }

    /**
     * Show dialog to input distance
     */
    private fun showInputDistanceDialog() {
        materialAlertDialogBuilder.setView(inputDistanceBinding.root)
            .setTitle("Input Distance")
            .setPositiveButton("Confirm") { dialog, _ ->
                viewModel.selectedDistanceMeters =
                    inputDistanceBinding.tiDistance.editText?.text.toString().toDouble()

                dialog.dismiss()
                findNavController().navigate(R.id.action_mainScreenFragment_to_sessionFragment)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}