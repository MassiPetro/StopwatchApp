package com.petrogallimassimo.stopwatchempatica

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import com.petrogallimassimo.stopwatchempatica.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        setupWithNavController(binding.bottomNavigation, navController)

        /*
        NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_screen -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.leaderboard_screen -> {
                    // Respond to navigation item 2 click
                    true
                }
                else -> false


         */

    }
}