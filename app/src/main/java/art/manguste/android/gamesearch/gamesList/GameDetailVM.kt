package art.manguste.android.gamesearch.gamesList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import art.manguste.android.gamesearch.api.GamesApi
import art.manguste.android.gamesearch.core.*
import art.manguste.android.gamesearch.core.encodeData
import art.manguste.android.gamesearch.db.Game
import art.manguste.android.gamesearch.db.GameDAO
import kotlinx.coroutines.*

class GameDetailVM(application: Application) : AndroidViewModel(application)  {

    private val _gameDetail = MutableLiveData<GameDetail>()
    val gameDetail: LiveData<GameDetail> get() = _gameDetail

    private val _status = MutableLiveData<LoadStatus>()
    val status: LiveData<LoadStatus>
        get() = _status

    init {
        _status.value = LoadStatus.NONE
    }

    fun getGameInfo(gameAlias: String){
        viewModelScope.launch {
            try {
                _status.value = LoadStatus.LOADING

                val resultRequest = GamesApi.retrofitService.getSpecificGame(gameAlias = gameAlias)

                _gameDetail.value = resultRequest
                //Log.d("GamesList fragment", "games = ${gamesList.value?.size}")

                _status.value = LoadStatus.DONE
            } catch (e: Exception) {
                _status.value = LoadStatus.ERROR
                _gameDetail.value = null
            }
        }
    }

    private fun intoGame(gameToSave: GameBasic): Deferred<Game?> {
        return GlobalScope.async {
            when (gameToSave) {
                is GameBriefly -> {
                    Game(
                            name = gameToSave.name,
                            alias = gameToSave.alias,
                            json = encodeData(gameToSave)
                    )
                }
                is GameDetail -> {
                    Game(
                            name = gameToSave.name,
                            alias = gameToSave.alias,
                            json = encodeData(gameToSave)
                    )
                }
                else -> null
            }
        }
    }

    private suspend fun insert(game: Game, database: GameDAO) {
        database.insert(game)
    }

    private suspend fun delete(game: Game, database: GameDAO) {
        database.delete(game)
    }

    fun addGameFavorite(gameToSave: GameBasic, database: GameDAO) {
        // pack game into class for DB
        CoroutineScope(Dispatchers.IO).launch {
            val game = intoGame(gameToSave).await()
            game?.let { insert(game, database) }
        }
    }

    fun removeGameFavorite(gameToDel: GameBasic, database: GameDAO) {
        CoroutineScope(Dispatchers.IO).launch {
            // todo change on one class
            val game = when (gameToDel) {
                is GameBriefly -> Game(name = gameToDel.name, alias = gameToDel.alias, json = "" )
                is GameDetail -> Game(name = gameToDel.name, alias = gameToDel.alias, json = "" )
                else -> Game(name = "", alias = "", json = "" )
            }
            delete(game, database)
        }
    }
}



