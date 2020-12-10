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

class GamesViewModel(application: Application) : AndroidViewModel(application) {

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    private var _gamesList = MutableLiveData<List<GameBriefly>>()
    val gamesList: LiveData<List<GameBriefly>> get() = _gamesList

    // displays data on init
    init {
        getGamesHotListSearch()
    }

    private fun getGamesHotListSearch() {
        viewModelScope.launch {
            try {
                val resultRequest = GamesApi.retrofitService.getGamesList()
                _gamesList.value = resultRequest.results
                //Log.d("GamesList fragment", "games = ${gamesList.value?.size}")
                _response.value = "Got : ${resultRequest.next}  "//response.body()
            } catch (e: Exception) {
                _response.value = "Not reachable : ${e.message}"
            }
        }

  /*      GamesApi.retrofitService.getGamesList().enqueue( object: Callback<ResultRequest> {
            override fun onFailure(call: Call<ResultRequest>, t: Throwable) {
                _response.value = "Not reachable : " + t.message
            }

            override fun onResponse(call: Call<ResultRequest>, response: Response<ResultRequest>) {
                _response.value = "Got : ${(response.body())?.next} and ${(response.body())?.results?.joinToString (", \n")  {it.name} } "//response.body()
                //_response.value = "Success: ${response.body()?.size} Mars properties retrieved"
            }
        })*/

        /*GamesApi.retrofitService.getGamesList().enqueue( object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                _response.value = "Not reachable : " + t.message
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                _response.value = response.body()
            }
        })*/
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