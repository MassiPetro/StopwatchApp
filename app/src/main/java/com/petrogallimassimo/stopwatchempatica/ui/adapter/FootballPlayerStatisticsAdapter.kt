package com.petrogallimassimo.stopwatchempatica.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.petrogallimassimo.stopwatchempatica.MainConstants.ChipSelected
import com.petrogallimassimo.stopwatchempatica.R
import com.petrogallimassimo.stopwatchempatica.databinding.ItemFootballPlayerStatisticsBinding
import com.petrogallimassimo.stopwatchempatica.model.FootballPlayerStatisticsModel

class FootballPlayerStatisticsAdapter(
    private val context: Context
) :
    ListAdapter<FootballPlayerStatisticsModel, FootballPlayerStatisticsAdapter.FootballPlayersStatisticsViewHolder>(
        FootballPlayerDiff
    ) {

    private var listFootballPlayers = ArrayList<FootballPlayerStatisticsModel>()
    private var chipSelected: ChipSelected? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FootballPlayersStatisticsViewHolder {
        return FootballPlayersStatisticsViewHolder(
            ItemFootballPlayerStatisticsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FootballPlayersStatisticsViewHolder, position: Int) {
        val playerPosition = listFootballPlayers[position]
        holder.bind(playerPosition)
        holder.changeMetricsStyle()

    }

    override fun getItemCount(): Int {
        return listFootballPlayers.size
    }

    fun replaceItems(list: List<FootballPlayerStatisticsModel>) {
        listFootballPlayers.clear()
        listFootballPlayers.addAll(list)
        notifyDataSetChanged()
    }

    fun sortByPeakSpeed() {
        listFootballPlayers.sortByDescending {
            it.metrics.peakSpeed
        }
        notifyDataSetChanged()
    }

    fun sortByLaps() {
        listFootballPlayers.sortByDescending {
            it.metrics.lapsNumber
        }
        notifyDataSetChanged()
    }

    fun setChipSelected(chip: ChipSelected) {
        chipSelected = chip
    }

    inner class FootballPlayersStatisticsViewHolder(private val binding: ItemFootballPlayerStatisticsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FootballPlayerStatisticsModel) {
            with(binding) {
                profileImage.let {
                    Glide.with(context)
                        .load(item.footballPlayer.large)
                        .into(it)
                }
                title.text = item.footballPlayer.title
                firstName.text = item.footballPlayer.first
                lastName.text = item.footballPlayer.last
                peakSpeedValue.text = item.metrics.peakSpeed
                lapsValue.text = item.metrics.lapsNumber

            }
        }

        fun changeMetricsStyle() {
            when (chipSelected) {
                ChipSelected.EXPLOSIVENESS -> {
                    binding.peakSpeed.setTextAppearance(R.style.boldText)
                    binding.laps.setTextAppearance(R.style.regularText)
                }
                ChipSelected.ENDURANCE -> {
                    binding.peakSpeed.setTextAppearance(R.style.regularText)
                    binding.laps.setTextAppearance(R.style.boldText)
                }
                ChipSelected.NONE -> {
                    binding.peakSpeed.setTextAppearance(R.style.regularText)
                    binding.laps.setTextAppearance(R.style.regularText)
                }
                else -> {
                    binding.peakSpeed.setTextAppearance(R.style.regularText)
                    binding.laps.setTextAppearance(R.style.regularText)
                }
            }

        }
    }

    private object FootballPlayerDiff : DiffUtil.ItemCallback<FootballPlayerStatisticsModel>() {
        override fun areItemsTheSame(
            oldItem: FootballPlayerStatisticsModel,
            newItem: FootballPlayerStatisticsModel
        ): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: FootballPlayerStatisticsModel,
            newItem: FootballPlayerStatisticsModel
        ): Boolean =
            oldItem.equals(newItem)

    }
}