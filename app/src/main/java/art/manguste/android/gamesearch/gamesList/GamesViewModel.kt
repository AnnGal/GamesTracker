package art.manguste.android.gamesearch.gamesList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import art.manguste.android.gamesearch.network.GamesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GamesViewModel(application: Application) : AndroidViewModel(application) {

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    // displays data on init
    init {
        getGamesHotListSearch()
    }

    private fun getGamesHotListSearch() {
        GamesApi.retrofitService.getGamesList().enqueue( object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                _response.value = "Not reachable : " + t.message
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                _response.value = response.body()
            }
        })
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