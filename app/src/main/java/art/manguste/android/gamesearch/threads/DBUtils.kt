package art.manguste.android.gamesearch.threads

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import art.manguste.android.gamesearch.api.HttpHandler
import art.manguste.android.gamesearch.api.JsonParser.parseGameData
import art.manguste.android.gamesearch.api.URLMaker.formURL
import art.manguste.android.gamesearch.core.Game
import art.manguste.android.gamesearch.core.SearchType
import art.manguste.android.gamesearch.db.FavoriteGame
import art.manguste.android.gamesearch.db.GameDatabase
import java.lang.Double
import java.lang.ref.WeakReference
import java.util.*

// I really should use Rx or kotlin coroutines next time
class DBUtils {
    private val gameDatabase: GameDatabase? = null

    private class SaveAsyncTask(private val context: Context?, private val game: Game) : AsyncTask<Void?, Void?, Void?>() {
        private val weakActivity: WeakReference<Activity>? = null


        override fun doInBackground(vararg params: Void?): Void? {
            if (isGameInFavorite(game.alias)) {
                //if (game.isFavorite()){
                context?.let { GameDatabase.Companion.getInstance(it) }!!.favoriteGameDao()!!.deleteByAlias(game.alias)
                Log.d(TAG, "DB: removed " + game.alias + " successfully! ")
            } else {
                var fullGameData: Game? = null
                fullGameData = if (game.jsonString == null) {
                    // load full data
                    val urlString = formURL(SearchType.GAME, game.alias)
                    val sh = HttpHandler()
                    val jsonStr = sh.makeServiceCall(urlString)
                    // parse response
                    parseGameData(jsonStr, true)
                } else game
                Log.d(TAG, "DB: try to save game " + fullGameData?.alias)
                val favGame = makeFavoriteGame(fullGameData!!)
                context?.let { GameDatabase.Companion.getInstance(it) }!!.favoriteGameDao()?.insert(favGame)
                Log.d(TAG, "DB: " + fullGameData.alias + " successfully added! ")
            }
            return null
        }

        private fun isGameInFavorite(gameAlias: String?): Boolean {
            var gameInDB = false
            val count: Int = context?.let { GameDatabase.Companion.getInstance(it) }!!.favoriteGameDao()!!.IsGameInFavorite(gameAlias)
            if (count > 0) {
                gameInDB = true
            }
            return gameInDB
        }

        override fun onPostExecute(agentsCount: Void?) {}

        companion object {
            // TODO
            fun makeFavoriteGame(game: Game): FavoriteGame {
                TODO("Not yet implemented")
                // TODO fix
             /*    return FavoriteGame(
                        game.idgetId(),
                        game.getName(),
                        game.getGameAlias(),
                        Date(),
                        game.getReleaseDate(),
                        Double.valueOf(game.getRating()),
                        game.getJsonString())*/
            }
        }


    }

    companion object {
        private val TAG = DBUtils::class.java.simpleName

        // Async save the game in db
        fun changeFavoriteStatus(context: Context?, game: Game) {
            Log.d(TAG, "DB: saveGameAsFavorite ")
            SaveAsyncTask(context, game).execute()
        }
    }
}