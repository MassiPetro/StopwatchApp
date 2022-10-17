package com.petrogallimassimo.stopwatchempatica.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.petrogallimassimo.stopwatchempatica.databinding.ItemFootballPlayerBinding
import com.petrogallimassimo.stopwatchempatica.model.FootballPlayerUiModel

class FootballPlayersAdapter(
    private val context: Context,
    private val onClick: (FootballPlayerUiModel) -> Unit
) :
    ListAdapter<FootballPlayerUiModel, FootballPlayersAdapter.FootballPlayersViewHolder>(
        FootballPlayerDiff
    ) {

    private var listFootballPlayers = ArrayList<FootballPlayerUiModel>()

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

    fun replaceItems(list: List<FootballPlayerUiModel>) {
        listFootballPlayers.clear()
        listFootballPlayers.addAll(list)
        notifyDataSetChanged()
    }

    inner class FootballPlayersViewHolder(private val binding: ItemFootballPlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FootballPlayerUiModel) {
            with(binding) {
                profileImage.let {
                    Glide.with(context)
                        .load(item.large)
                        .into(it)
                }
                title.text = item.title
                firstName.text = item.first
                lastName.text = item.last
                itemLayout.setOnClickListener {
                    onClick(listFootballPlayers[adapterPosition])
                }
            }
        }
    }

    private object FootballPlayerDiff : DiffUtil.ItemCallback<FootballPlayerUiModel>() {
        override fun areItemsTheSame(
            oldItem: FootballPlayerUiModel,
            newItem: FootballPlayerUiModel
        ): Boolean =
            oldItem.first == newItem.first

        override fun areContentsTheSame(
            oldItem: FootballPlayerUiModel,
            newItem: FootballPlayerUiModel
        ): Boolean =
            oldItem.equals(newItem)

    }
}