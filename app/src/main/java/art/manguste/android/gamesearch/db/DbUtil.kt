package art.manguste.android.gamesearch.db

import android.app.Application
import android.util.Log
import art.manguste.android.gamesearch.core.GameBasic
import art.manguste.android.gamesearch.core.GameBriefly
import art.manguste.android.gamesearch.core.encodeData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

suspend fun insertGame(game: Game, database: GameDAO) {
    database.insert(game)
}

suspend fun deleteGame(game: Game, database: GameDAO) {
    database.delete(game)
}

fun removeFromFavorite(gameToDel: GameBasic, database: GameDAO) {
    CoroutineScope(Dispatchers.IO).launch {
        val game = Game(name = gameToDel.name, alias = gameToDel.alias, json = "" )
        deleteGame(game, database)
    }
}

fun addToFavorite(gameToSave: GameBasic, database: GameDAO) {
    // pack game into class for DB
    CoroutineScope(Dispatchers.IO).launch {
        Log.d("DB", "try to save = ${gameToSave.alias}")
        val game = intoGame(gameToSave)
        Log.d("DB", "try to save 2 = ${game?.alias}")
        game?.let { insertGame(game, database) }
    }
}

private suspend fun intoGame(gameToSave: GameBasic): Game? {
    return withContext(Dispatchers.Default) {
        Game(name = gameToSave.name,
             alias = gameToSave.alias,
             json = encodeData(gameToSave))
    }
}