package art.manguste.android.gamesearch.gamesListFavorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch

class GamesListFavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private var _gamesList = MutableLiveData<List<GameBriefly>>()
    val gamesList: LiveData<List<GameBriefly>> get() = _gamesList

    private val _status = MutableLiveData(LoadStatus.NONE)
    val status: LiveData<LoadStatus>
        get() = _status

    private val database: GameDAO = GameDatabase.getInstance(application).gameDao

    fun getDBGameList() {
        var games: List<GameBriefly>
        CoroutineScope(Dispatchers.IO).launch {
            // get games from DB
            val gamesDBList = database.getAll()

            games = gamesDBList.map { parseData(it.json) }

            viewModelScope.launch {
                _gamesList.value = games
            }
        }
    }

    fun addGameFavorite(gameToSave: GameBasic) {
        addToFavorite(gameToSave, database)

        getDBGameList()
    }

    fun removeGameFavorite(gameToRemove: GameBasic) {
        removeFromFavorite(gameToRemove, database)

        getDBGameList()
    }
}
