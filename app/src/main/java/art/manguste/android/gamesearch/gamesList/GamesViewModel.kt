package art.manguste.android.gamesearch.gamesList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import art.manguste.android.gamesearch.core.GameBriefly
import art.manguste.android.gamesearch.network.GamesApi
import kotlinx.coroutines.launch

enum class LoadStatus { LOADING, ERROR, DONE }

class GamesViewModel(application: Application) : AndroidViewModel(application) {

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    private var _gamesList = MutableLiveData<List<GameBriefly>>()
    val gamesList: LiveData<List<GameBriefly>> get() = _gamesList

    private val _status = MutableLiveData<LoadStatus>()
    val status: LiveData<LoadStatus>
        get() = _status

    // displays data on init
    init {
        //getGamesHotList()
    }

    private fun getGamesHotList() {
        viewModelScope.launch {
            try {
                _status.value = LoadStatus.LOADING
                val resultRequest = GamesApi.retrofitService.getGamesList()
                _gamesList.value = resultRequest.results
                //Log.d("GamesList fragment", "games = ${gamesList.value?.size}")
                _response.value = "Got : ${resultRequest.next}  "//response.body()
                _status.value = LoadStatus.DONE
            } catch (e: Exception) {
                _status.value = LoadStatus.ERROR
                _response.value = "Not reachable : ${e.message}"
                _gamesList.value = ArrayList()
            }
        }
    }

    fun getHotGamesList(){
        viewModelScope.launch {
            try {
                _status.value = LoadStatus.LOADING

                val resultRequest = GamesApi.retrofitService.getGamesHotListSorts(
                        datesRange = "2020-06-01,2020-09-15",
                        ordering = "-added")
                // example: https://api.rawg.io/api/games?dates=2020-06-01,2020-09-15&ordering=-added

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

                val resultRequest = GamesApi.retrofitService.getGamesListSearch(pageSize = 10,
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