package com.petrogallimassimo.stopwatchempatica.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.petrogallimassimo.stopwatchempatica.databinding.ItemFootballPlayerBinding
import com.petrogallimassimo.stopwatchempatica.datasource.FootballPlayerUiModel

class FootballPlayersAdapter(private val listFootballPlayers: List<FootballPlayerUiModel>) :
    RecyclerView.Adapter<FootballPlayersAdapter.FootballPlayersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FootballPlayersViewHolder {
        return FootballPlayersViewHolder(
            ItemFootballPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FootballPlayersViewHolder, position: Int) {
        val playerPosition = listFootballPlayers[position]
        holder.bind(playerPosition)

    }

    override fun getItemCount(): Int {
        return listFootballPlayers.size
    }

    inner class FootballPlayersViewHolder(private val binding: ItemFootballPlayerBinding) :
        ViewHolder(binding.root) {
        fun bind(item: FootballPlayerUiModel) {
            with(binding) {
                title.text = item.title
                firstName.text = item.first
                lastName.text = item.last
            }
        }
    }
}