package com.petrogallimassimo.stopwatchempatica.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.petrogallimassimo.stopwatchempatica.databinding.ItemLapBinding
import com.petrogallimassimo.stopwatchempatica.model.LapModel

class LapsAdapter :
    ListAdapter<LapModel, LapsAdapter.LapsViewHolder>(LapsDiff) {

    private var listLaps = ArrayList<LapModel>()
    private var lapNumber = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapsViewHolder {
        return LapsViewHolder(
            ItemLapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: LapsViewHolder, position: Int) {
        val lapPosition = listLaps[position]
        holder.bind(lapPosition)

    }

    override fun getItemCount(): Int {
        return listLaps.size
    }

    fun addLap(time: String) {
        listLaps.add(LapModel(lapNumber = "Lap $lapNumber", time = time))
        lapNumber++
        notifyDataSetChanged()
    }

    fun clearLaps() {
        listLaps.clear()
        lapNumber = 1
        notifyDataSetChanged()
    }

    inner class LapsViewHolder(private val binding: ItemLapBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LapModel) {
            with(binding) {
                lapNumber.text = item.lapNumber
                time.text = item.time
            }
        }
    }

    private object LapsDiff : DiffUtil.ItemCallback<LapModel>() {
        override fun areItemsTheSame(
            oldItem: LapModel,
            newItem: LapModel
        ): Boolean =
            oldItem.lapNumber == newItem.lapNumber

        override fun areContentsTheSame(
            oldItem: LapModel,
            newItem: LapModel
        ): Boolean =
            oldItem.equals(newItem)

    }
}