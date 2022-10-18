package com.petrogallimassimo.stopwatchempatica

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.petrogallimassimo.stopwatchempatica.model.FootballPlayerUiModel
import com.petrogallimassimo.stopwatchempatica.datasource.Repository
import com.petrogallimassimo.stopwatchempatica.model.FootballPlayerStatisticsModel
import kotlinx.coroutines.launch

class MainViewModel (
    private val repository: Repository
) : ViewModel() {

    val footballPlayersLiveData = MutableLiveData<List<FootballPlayerUiModel>>()
    val footballPlayerStatisticsModelList = ArrayList<FootballPlayerStatisticsModel>()
    var selectedPlayer = FootballPlayerUiModel()
    var selectedDistanceMeters: Double = 0.0

    fun getFootballPlayers() {
        viewModelScope.launch {
            repository.getFootballPlayers().collect {
                footballPlayersLiveData.postValue(it.mapToFootballPlayerUiModel())
            }
        }
    }

    class ViewModelFactory constructor(private val repository: Repository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(repository) as T
        }
    }
}


