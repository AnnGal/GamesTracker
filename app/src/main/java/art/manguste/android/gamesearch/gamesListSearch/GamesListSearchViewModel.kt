package art.manguste.android.gamesearch.gamesListSearch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import art.manguste.android.gamesearch.api.GamesApi
import art.manguste.android.gamesearch.core.GameBasic
import art.manguste.android.gamesearch.core.GameBriefly
import art.manguste.android.gamesearch.core.LoadStatus
import art.manguste.android.gamesearch.core.parseData
import art.manguste.android.gamesearch.db.GameDAO
import art.manguste.android.gamesearch.db.GameDatabase
import art.manguste.android.gamesearch.db.addToFavorite
import art.manguste.android.gamesearch.db.removeFromFavorite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class GamesListSearchViewModel(application: Application) : AndroidViewModel(application) {

    // retrofit requests fast settings
    private val rowNum = 10                 // how many rows in query

    private var _gamesList = MutableLiveData<List<GameBriefly>>()
    val gamesList: LiveData<List<GameBriefly>> get() = _gamesList

    private val _status = MutableLiveData(LoadStatus.NONE)
    val status: LiveData<LoadStatus>
        get() = _status

    private val database: GameDAO = GameDatabase.getInstance(application).gameDao

    private var gamesDB = MutableLiveData<List<GameBriefly>>()

    // displays data on init
    init {
        getDBGamesForViewModel()
    }

    fun getSearchGameList(search: String) {
        viewModelScope.launch {
            try {
                _status.value = LoadStatus.LOADING
                getDBGamesForViewModel()

                val resultRequest = GamesApi.retrofitService.getGamesListSearch(
                        pageSize = rowNum,
                        gameName = search)

                gamesDB.value?.let {
                    val gamesAlias: List<String> = gamesDB.value!!.map { it.alias }.toList()
                    resultRequest.results.filter { it.alias in gamesAlias }.forEach { it.isFavorite = true }
                }

                _gamesList.value = resultRequest.results

                _status.value = LoadStatus.DONE
            } catch (e: Exception) {
                _status.value = LoadStatus.ERROR
                _gamesList.value = ArrayList()
            }
        }
    }

    private fun getDBGamesForViewModel() {
        var games: List<GameBriefly>
        CoroutineScope(Dispatchers.IO).launch {
            val gamesDBList = database.getAll()
            games = gamesDBList.map { parseData(it.json) }

            viewModelScope.launch {
                gamesDB.value = games
            }
        }
    }

    fun reloadGamesFavoriteStatus() {
        CoroutineScope(Dispatchers.IO).launch {
            //getDBGamesForViewModel()
            val gamesDBList = database.getAll()
            val games = gamesDBList.map { parseData(it.json) }

            games.let {
                val gamesAlias: List<String> = games.map { it.alias }.toList()
                val data = _gamesList.value
                data?.forEach { it.isFavorite = false }
                data?.filter { it.alias in gamesAlias }?.forEach { it.isFavorite = true }

                viewModelScope.launch {
                    _gamesList.value = data
                }
            }
        }
    }

    fun addGameFavorite(gameToSave: GameBasic) {
        addToFavorite(gameToSave, database)
        GlobalScope.launch {
            getDBGamesForViewModel()
        }
    }

    fun removeGameFavorite(gameToRemove: GameBasic) {
        removeFromFavorite(gameToRemove, database)

        GlobalScope.launch {
            getDBGamesForViewModel()
        }
    }
}

