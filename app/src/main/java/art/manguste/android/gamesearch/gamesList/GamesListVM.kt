package art.manguste.android.gamesearch.gamesList

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import art.manguste.android.gamesearch.core.GameBriefly
import art.manguste.android.gamesearch.api.GamesApi
import art.manguste.android.gamesearch.core.LoadStatus
import art.manguste.android.gamesearch.db.Game
import art.manguste.android.gamesearch.db.GameDAO
import art.manguste.android.gamesearch.db.GameDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GamesListVM(application: Application, databaseG: GameDAO) : AndroidViewModel(application) {

    // retrofit requests fast settings
    private val rowNum = 10                 // how many rows in query
    private val orderBy = "-added"          // sort query by
    private val monthGapBeforeNow = 1       // for "hot games" - range settings
    private val monthGapAfterNow = 1        // for "hot games" - range settings

    private var _gamesList = MutableLiveData<List<GameBriefly>>()
    val gamesList: LiveData<List<GameBriefly>> get() = _gamesList

    private val _status = MutableLiveData<LoadStatus>()
    val status: LiveData<LoadStatus>
        get() = _status

    //private val database: GameDAO by lazy {GameDatabase.getInstance(application).gameDao}
//    val gamesDB: LiveData<List<Game>> = databaseG.getAll()

    // displays data on init
    init {
        _status.value = LoadStatus.NONE
    }

    fun getHotGamesList(){
        viewModelScope.launch {
            try {
                _status.value = LoadStatus.LOADING

                val resultRequest = GamesApi.retrofitService.getGamesHotListSorts(
                        datesRange = getDatesRange(monthGapBeforeNow,monthGapAfterNow),
                        ordering = orderBy)

                _gamesList.value = resultRequest.results
                //Log.d("GamesList fragment", "games = ${gamesList.value?.size}")

                _status.value = LoadStatus.DONE
            } catch (e: Exception) {
                _status.value = LoadStatus.ERROR
                _gamesList.value = ArrayList()
            }
        }
    }

    fun getSearchGameList(search: String) {
        viewModelScope.launch {
            try {
                _status.value = LoadStatus.LOADING

                val resultRequest = GamesApi.retrofitService.getGamesListSearch(
                        pageSize = rowNum,
                        gameName = search)

                _gamesList.value = resultRequest.results
                //Log.d("GamesList fragment", "games = ${gamesList.value?.size}")

                _status.value = LoadStatus.DONE
            } catch (e: Exception) {
                _status.value = LoadStatus.ERROR
                _gamesList.value = ArrayList()
            }
        }
    }

   fun getDBGameList(database: GameDAO) {
        viewModelScope.launch {
            try {
                _status.value = LoadStatus.LOADING

                val resultRequest = database.getAll()
                //val nights = database.getAll()

                //_gamesList.value = database.getAll().value
                //Log.d("GamesList fragment", "gameDB = ${resultRequest.value?.size}")

                _status.value = LoadStatus.DONE
            } catch (e: Exception) {
                _status.value = LoadStatus.ERROR
                Log.d("GamesList fragment", "Exception = ${e.message}")
                _gamesList.value = ArrayList()
            }
        }
    }

    fun addGameFavorite(gameBriefly: GameBriefly, database: GameDAO) {
        val game = Game(name= gameBriefly.name, alias = gameBriefly.alias, json = gameBriefly.name)

        CoroutineScope(Dispatchers.IO).launch {
            insert(game, database)
        }

        CoroutineScope(Dispatchers.IO).launch {
            count(database)
        }



        //viewModelScope.launch {
            // Create a new night, which captures the current time,
            // and insert it into the database.
            //database.insert(game)
            //val resultRequest = database.getAll()
            //Log.d("GamesList fragment", "gameDB = ${resultRequest.value?.size}")
            //Log.d("GamesList fragment", "database.count = "+database.count())
        //}
    }

    private suspend fun insert(game: Game, database: GameDAO) {
        withContext(Dispatchers.IO) {
            database.insert(game)
        }
    }

    private suspend fun count(database: GameDAO) {
        withContext(Dispatchers.IO) {
            val req = database.getAll()
            val res = database.count()
            Log.d("GamesList fragment", "database.count = $res req.size = ${req.joinToString(", \n") { it.name }}")
        }
    }



    fun removeGameFavorite(gameBriefly: GameBriefly, database: GameDAO) {
      /*  viewModelScope.launch {
            // Create a new night, which captures the current time,
            // and insert it into the database.
            database.delete(gameBriefly)
        }*/
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


/*    public LiveData<ArrayList<Game>> getGamesHot() {
        return gamesHot;
    }

    public LiveData<ArrayList<Game>> getGamesSearch() {
        return gamesSearch;
    }  val gamesFavorite: LiveData<List<FavoriteGame?>?>?

     private LiveData<ArrayList<Game>> gamesHot;
     private LiveData<ArrayList<Game>> gamesSearch;
    init {
        val gameDatabase: GameDatabase = GameDatabase.Companion.getInstance(getApplication<Application>())
        gamesFavorite = gameDatabase.favoriteGameDao().selectAll()
    }*/