package art.manguste.android.gamesearch.threads

import android.content.Context
import android.util.Log
import androidx.loader.content.AsyncTaskLoader
import art.manguste.android.gamesearch.old_api.HttpHandler
import art.manguste.android.gamesearch.old_api.JsonParser.extractData
import art.manguste.android.gamesearch.core.OLD_Game
import art.manguste.android.gamesearch.core.SearchType
import java.util.*

class GamesApiLoader(context: Context?, private val mUrl: String?, private val mSearchType: SearchType?) : AsyncTaskLoader<ArrayList<OLD_Game?>?>(context!!) {
    private val alreadyAsked = false
    override fun onStartLoading() {
        Log.d(TAG, "onStartLoading: " + mSearchType.toString())
        forceLoad()
    }

    override fun loadInBackground(): ArrayList<OLD_Game?>? {
        if (mUrl == null) {
            return null
        }
        Log.d(TAG, "loadInBackground: " + mSearchType.toString())
        // make a request and catch response
        val sh = HttpHandler()
        val jsonStr = sh.makeServiceCall(mUrl)
        // parse response
        val games = extractData(jsonStr, mSearchType!!)
       /* if (games != null && games.size > 0) {
            val favGames = GameDatabase.Companion.getInstance(context)!!.favoriteGameDao()!!.selectAllNoLiveData() as ArrayList<FavoriteGame>
            for (game in games) {
                for (gameFavorite in favGames) {
                    if (game!!.alias == gameFavorite.apiAlias) {
                        Log.d(TAG, "Favorite: " + game.alias)
                        game.isFavorite = true
                        break
                    }
                }
            }
        }*/

/*        for (Game game : games) {
            if (isGameInFavorite(game.getGameAlias())){
                game.setFavorite(true);
            }
        }*/return games
    }

    private fun isGameInFavorite(gameAlias: String): Boolean {
        var gameInDB = false
       /* val count: Int = GameDatabase.Companion.getInstance(context)!!.favoriteGameDao()!!.IsGameInFavorite(gameAlias)
        if (count > 0) {
            gameInDB = true
        }****/
        return gameInDB
    }

    companion object {
        private val TAG = GamesApiLoader::class.java.simpleName + "CheckLoader"
    }
}