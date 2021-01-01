package art.manguste.android.gamesearch.gameslist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import art.manguste.android.gamesearch.api.GamesApi
import art.manguste.android.gamesearch.core.*
import art.manguste.android.gamesearch.db.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GamesListViewModel(application: Application) : AndroidViewModel(application) {

    // retrofit requests fast settings
    private val rowNum = 10                 // how many rows in query
    private val orderBy = "-added"          // sort query by
    private val monthGapBeforeNow = 3       // for "hot games" - range settings
    private val monthGapAfterNow = 1        // for "hot games" - range settings

    private var _gamesList = MutableLiveData<List<GameBriefly>>()
    val gamesList: LiveData<List<GameBriefly>> get() = _gamesList

    private val _status = MutableLiveData<LoadStatus>(LoadStatus.NONE)
    val status: LiveData<LoadStatus>
        get() = _status

    //private val databaseLazy: GameDAO by lazy {GameDatabase.getInstance(application).gameDao}
    private val databaseLazy: GameDAO  = GameDatabase.getInstance(application).gameDao

    private var gamesDB = MutableLiveData<List<GameBriefly>>()

    // displays data on init
    init {
        getDBGamesForViewModel()
    }

    fun getHotGamesList() {
        viewModelScope.launch {
            try {
                _status.value = LoadStatus.LOADING
                getDBGamesForViewModel()

                val resultRequest = GamesApi.retrofitService.getGamesHotListSorts(
                        datesRange = getDatesRange(monthGapBeforeNow, monthGapAfterNow),
                        ordering = orderBy)

                //Log.d("GamesList fragment", "gamesDB size = ${gamesDB.value?.size}")

                gamesDB.value?.let {
                    val gamesAlias: List<String> = gamesDB.value!!.map { it.alias }.toList()
                    resultRequest.results.filter { it.alias in gamesAlias }.forEach { it.isFavorite = true }
                }

                _gamesList.value = resultRequest.results

                _status.value = LoadStatus.DONE
            } catch (e: Exception) {
                _status.value = LoadStatus.ERROR
                Log.d("Error connect","Error connect ${e.message}")
                _gamesList.value = ArrayList()
            }
        }
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
            val gamesDBList = databaseLazy.getAll()
            games = gamesDBList.map { parseData(it.json) }

            viewModelScope.launch {
                gamesDB.value = games
            }
        }
    }

    /*private suspend fun getDBGamesForViewModel(): Deferred<List<GameBriefly>> {
        return GlobalScope.async {
            val gamesDBList = databaseLazy.getAll()
            val games: List<GameBriefly> = gamesDBList.map { parseData(it.json) }
            return@async games
        }
    }*/

    fun reloadGamesFavoriteStatus(){
        CoroutineScope(Dispatchers.IO).launch {
            //getDBGamesForViewModel()
            val gamesDBList = databaseLazy.getAll()
            var games = gamesDBList.map { parseData(it.json) }

            games.let {
                val gamesAlias: List<String> = games.map { it.alias }.toList()
                val data = _gamesList.value
                data?.filter { it.alias in gamesAlias }?.forEach { it.isFavorite = true }

                viewModelScope.launch {
                    _gamesList.value = data
                }
            }
        }
    }

    fun getDBGameList() {
        var games: List<GameBriefly> = listOf()
        CoroutineScope(Dispatchers.IO).launch {
            // get games from DB
            val gamesDBList = databaseLazy.getAll()
            games = gamesDBList.map { parseData(it.json) }

            viewModelScope.launch {
                _gamesList.value = games
            }
        }
    }

    fun addGameFavorite(gameToSave: GameBasic) {
        addToFavorite(gameToSave, databaseLazy)
        GlobalScope.launch {
            getDBGamesForViewModel()
        }
    }

    fun removeGameFavorite(gameToRemove: GameBasic) {
        removeFromFavorite(gameToRemove, databaseLazy)

        GlobalScope.launch {
            getDBGamesForViewModel()
        }
    }
}

private fun getDatesRange(monthBefore: Int, monthAfter: Int): String {
    val dateNow = Date()
    val dateFrom = addMonth(dateNow, -monthBefore)
    val dateFuture = addMonth(dateNow, monthAfter)
    return formatDate(dateFrom) + "," + formatDate(dateFuture)
}

private fun formatDate(dateObject: Date): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(dateObject)
}

fun addMonth(date: Date, diff: Int): Date {
    val cal = Calendar.getInstance()
    cal.time = date
    cal.add(Calendar.MONTH, diff)
    return cal.time
}

