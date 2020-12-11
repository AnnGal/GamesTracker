package art.manguste.android.gamesearch.gamesList

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import art.manguste.android.gamesearch.core.GameBriefly
import art.manguste.android.gamesearch.network.GamesApi
import kotlinx.coroutines.launch

enum class ApiStatus { LOADING, ERROR, DONE }

class GamesViewModel(application: Application) : AndroidViewModel(application) {

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    private var _gamesList = MutableLiveData<List<GameBriefly>>()
    val gamesList: LiveData<List<GameBriefly>> get() = _gamesList

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    // displays data on init
    init {
        getGamesHotListSearch()
    }

    private fun getGamesHotListSearch() {
        viewModelScope.launch {
            try {
                _status.value = ApiStatus.LOADING
                val resultRequest = GamesApi.retrofitService.getGamesList()
                _gamesList.value = resultRequest.results
                //Log.d("GamesList fragment", "games = ${gamesList.value?.size}")
                _response.value = "Got : ${resultRequest.next}  "//response.body()
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                _response.value = "Not reachable : ${e.message}"
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