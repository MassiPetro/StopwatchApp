package com.petrogallimassimo.stopwatchempatica

import androidx.lifecycle.*
import com.petrogallimassimo.stopwatchempatica.datasource.FootballPlayerStatisticsRepository
import com.petrogallimassimo.stopwatchempatica.datasource.Repository
import com.petrogallimassimo.stopwatchempatica.model.FootballPlayerStatisticsModel
import com.petrogallimassimo.stopwatchempatica.model.FootballPlayerUiModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

@ViewModelScoped
class MainViewModel(
    private val repository: Repository,
    private val footballPlayerStatisticsRepository: FootballPlayerStatisticsRepository
) : ViewModel() {

    val footballPlayersLiveData = MutableLiveData<List<FootballPlayerUiModel>>()
    val footballPlayerStatisticsModelList = ArrayList<FootballPlayerStatisticsModel>()
    var selectedPlayer = FootballPlayerUiModel()
    var selectedDistanceMeters: Double = 0.0

    val footballPlayersStatsDB: LiveData<List<FootballPlayerStatisticsModel>> =
        footballPlayerStatisticsRepository.getAll().asLiveData()

    fun getFootballPlayers() {
        viewModelScope.launch {
            repository.getFootballPlayers().collect {
                footballPlayersLiveData.postValue(it.mapToFootballPlayerUiModel())
            }
        }
    }

    suspend fun insert(footballPlayerStatisticsModel: FootballPlayerStatisticsModel) =
        footballPlayerStatisticsRepository.insert(footballPlayerStatisticsModel)

    class ViewModelFactory constructor(
        private val repository: Repository,
        private val footballPlayerStatisticsRepository: FootballPlayerStatisticsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(repository, footballPlayerStatisticsRepository) as T
        }
    }
}


