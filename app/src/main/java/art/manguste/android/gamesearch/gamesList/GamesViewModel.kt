package art.manguste.android.gamesearch.gamesList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GamesViewModel(application: Application) : AndroidViewModel(application) {


    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    // displays data on init
    init {
        getGamesHotListSearch()
    }

    private fun getGamesHotListSearch() {
        _response.value = "Response will be here soon"
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