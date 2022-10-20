package com.petrogallimassimo.stopwatchempatica

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.petrogallimassimo.stopwatchempatica.datasource.FootballPlayerStatisticsRepository
import com.petrogallimassimo.stopwatchempatica.datasource.Repository
import com.petrogallimassimo.stopwatchempatica.model.FootballPlayerStatisticsModel
import com.petrogallimassimo.stopwatchempatica.model.FootballPlayerUiModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.internal.wait

@ViewModelScoped
class MainViewModel(
    private val repository: Repository,
    private val footballPlayerStatisticsRepository: FootballPlayerStatisticsRepository
) : ViewModel() {

    // livedata of football players from API passed to adapter
    val footballPlayersLiveData = MutableLiveData<List<FootballPlayerUiModel>>()

    // list to save football players statistics
    val footballPlayerStatisticsModelList = ArrayList<FootballPlayerStatisticsModel>()

    // selected player from MainScreenFragment
    var selectedPlayer = FootballPlayerUiModel()

    //selected distance in meters
    var selectedDistanceMeters: Double = 0.0

    // football players statistics saved on DB
    val footballPlayersStatsDB: LiveData<List<FootballPlayerStatisticsModel>> =
        footballPlayerStatisticsRepository.getAll().asLiveData()

    var converterJob: Job? = null

    /**
     * Get football players from API
     */
    fun getFootballPlayers() {
        viewModelScope.launch {
            repository.getFootballPlayers().collect {
                footballPlayersLiveData.postValue(it.mapToFootballPlayerUiModel())
            }
        }
    }

    /**
     * Save football players statistics on DB
     */
    suspend fun insert(footballPlayerStatisticsModel: FootballPlayerStatisticsModel) =
        footballPlayerStatisticsRepository.insert(footballPlayerStatisticsModel)

    /**
     * Job to convert football players statistics to string
     */
    fun convertDataToString(list: List<FootballPlayerStatisticsModel>, callback: () -> Unit) {
        Log.d("JOBCONVERTER", "START")
        converterJob = viewModelScope.launch {
            for (footballPlayerStats in list) {
                footballPlayerStats.footballPlayer.toString()
                footballPlayerStats.metrics.toString()
            }
            delay(10000)
            callback()
            Log.d("JOBCONVERTER", "DONE")
        }
    }

    class ViewModelFactory constructor(
        private val repository: Repository,
        private val footballPlayerStatisticsRepository: FootballPlayerStatisticsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(repository, footballPlayerStatisticsRepository) as T
        }
    }
}


