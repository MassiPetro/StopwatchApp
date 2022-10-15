package com.petrogallimassimo.stopwatchempatica.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.petrogallimassimo.stopwatchempatica.datasource.FootballPlayerUiModel
import com.petrogallimassimo.stopwatchempatica.datasource.Repository
import kotlinx.coroutines.launch

class MainViewModel (
    private val repository: Repository
) : ViewModel() {

    val footballPlayersLiveData = MutableLiveData<List<FootballPlayerUiModel>>()
    var selectedPlayer = FootballPlayerUiModel()

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


