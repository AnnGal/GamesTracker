package art.manguste.android.gamesearch.threads

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import art.manguste.android.gamesearch.old_api.HttpHandler
import art.manguste.android.gamesearch.old_api.JsonParser.parseGameData
import art.manguste.android.gamesearch.old_api.URLMaker.formURL
import art.manguste.android.gamesearch.core.OLD_Game
import art.manguste.android.gamesearch.core.SearchType

import java.lang.ref.WeakReference

// I really should use Rx or kotlin coroutines next time
class DBUtils {
    /*
    private val gameDatabase: GameDatabase? = null

    private class SaveAsyncTask(private val context: Context?, private val OLDGame: OLD_Game) : AsyncTask<Void?, Void?, Void?>() {
        private val weakActivity: WeakReference<Activity>? = null


        override fun doInBackground(vararg params: Void?): Void? {
            if (isGameInFavorite(OLDGame.alias)) {
                //if (game.isFavorite()){
                context?.let { GameDatabase.Companion.getInstance(it) }!!.favoriteGameDao()!!.deleteByAlias(OLDGame.alias)
                Log.d(TAG, "DB: removed " + OLDGame.alias + " successfully! ")
            } else {
                var fullOLDGameData: OLD_Game? = null
                fullOLDGameData = if (OLDGame.jsonString == null) {
                    // load full data
                    val urlString = formURL(SearchType.GAME, OLDGame.alias)
                    val sh = HttpHandler()
                    val jsonStr = sh.makeServiceCall(urlString)
                    // parse response
                    parseGameData(jsonStr, true)
                } else OLDGame
                Log.d(TAG, "DB: try to save game " + fullOLDGameData?.alias)
                val favGame = makeFavoriteGame(fullOLDGameData!!)
                context?.let { GameDatabase.Companion.getInstance(it) }!!.favoriteGameDao()?.insert(favGame)
                Log.d(TAG, "DB: " + fullOLDGameData.alias + " successfully added! ")
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
            fun makeFavoriteGame(OLDGame: OLD_Game): FavoriteGame {
                TODO("Not yet implemented")
                // TODO fix
             *//*    return FavoriteGame(
                        game.idgetId(),
                        game.getName(),
                        game.getGameAlias(),
                        Date(),
                        game.getReleaseDate(),
                        Double.valueOf(game.getRating()),
                        game.getJsonString())*//*
            }
        }


    }

    companion object {
        private val TAG = DBUtils::class.java.simpleName

        // Async save the game in db
        fun changeFavoriteStatus(context: Context?, OLDGame: OLD_Game) {
            Log.d(TAG, "DB: saveGameAsFavorite ")
            SaveAsyncTask(context, OLDGame).execute()
        }
    }*/
}