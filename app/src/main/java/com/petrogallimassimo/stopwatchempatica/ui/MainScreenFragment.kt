package com.petrogallimassimo.stopwatchempatica.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.petrogallimassimo.stopwatchempatica.databinding.FragmentMainScreenBinding

class MainScreenFragment : Fragment() {

    private val footballPlayersAdapter: FootballPlayersAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dataBinding = FragmentMainScreenBinding.inflate(inflater, container, false)

        return dataBinding.root
    }

}