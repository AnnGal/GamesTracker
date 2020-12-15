package art.manguste.android.gamesearch.gamesList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import art.manguste.android.gamesearch.api.GamesApi
import art.manguste.android.gamesearch.core.*
import art.manguste.android.gamesearch.core.encodeData
import art.manguste.android.gamesearch.db.*
import kotlinx.coroutines.*

class GameDetailVM(application: Application) : AndroidViewModel(application)  {

    private val _gameDetail = MutableLiveData<GameDetail>()
    val gameDetail: LiveData<GameDetail> get() = _gameDetail

    private val _status = MutableLiveData<LoadStatus>()
    val status: LiveData<LoadStatus>
        get() = _status

    private val databaseLazy: GameDAO  = GameDatabase.getInstance(application).gameDao
    private var gamesDB = MutableLiveData<List<GameBriefly>>()

    init {
        _status.value = LoadStatus.NONE
        getDBGamesForViewModel()
    }

    fun getGameInfo(gameAlias: String){
        viewModelScope.launch {
            try {
                _status.value = LoadStatus.LOADING

                val resultRequest = GamesApi.retrofitService.getSpecificGame(gameAlias = gameAlias)

                gamesDB.value?.let {
                    val gamesAlias: List<String> = gamesDB.value!!.map { it.alias }.toList()
                    resultRequest.isFavorite = (resultRequest.alias in gamesAlias)
                }

                _gameDetail.value = resultRequest
                _status.value = LoadStatus.DONE
            } catch (e: Exception) {
                _status.value = LoadStatus.ERROR
                _gameDetail.value = null
            }
        }
    }

    private fun getDBGamesForViewModel() {
        var games: List<GameBriefly> = listOf()
        CoroutineScope(Dispatchers.IO).launch {
            val gamesDBList = databaseLazy.getAll()
            games = gamesDBList.map { parseData(it.json) }

            viewModelScope.launch {
                gamesDB.value = games
            }
        }
    }

    fun addGameFavorite(gameToSave: GameBasic,) {
        addToFavorite(gameToSave, databaseLazy)
    }

    fun removeGameFavorite(gameToDel: GameBasic) {
        removeFromFavorite(gameToDel, databaseLazy)
    }
}



