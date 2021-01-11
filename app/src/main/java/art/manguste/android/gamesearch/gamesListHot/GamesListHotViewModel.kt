package art.manguste.android.gamesearch.gamesListHot

import android.app.Application
import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GamesListHotViewModel(application: Application) : AndroidViewModel(application) {

    // retrofit requests fast settings
    private val orderBy = "-added"          // sort query by
    private val monthGapBeforeNow = 3       // for "hot games" - range settings
    private val monthGapAfterNow = 1        // for "hot games" - range settings

    private var _gamesList = MutableLiveData<List<GameBriefly>>()
    val gamesList: LiveData<List<GameBriefly>> get() = _gamesList

    private val _status = MutableLiveData(LoadStatus.NONE)
    val status: LiveData<LoadStatus>
        get() = _status

    private val database: GameDAO = GameDatabase.getInstance(application).gameDao

    private var gamesDB = MutableLiveData<List<GameBriefly>>()

    // displays data on init
    init {
        refreshDBGamesForViewModel()
    }

    fun getHotGamesList() {
        viewModelScope.launch {
            try {
                _status.value = LoadStatus.LOADING
                refreshDBGamesForViewModel()

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
                Log.d("Error connect", "Error connect ${e.message}")
                _gamesList.value = ArrayList()
            }
        }
    }

    private fun refreshDBGamesForViewModel() {
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
            refreshDBGamesForViewModel()
        }
    }

    fun removeGameFavorite(gameToRemove: GameBasic) {
        removeFromFavorite(gameToRemove, database)

        GlobalScope.launch {
            refreshDBGamesForViewModel()
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

