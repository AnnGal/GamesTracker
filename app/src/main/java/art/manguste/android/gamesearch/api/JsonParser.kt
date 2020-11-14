package art.manguste.android.gamesearch.api

import android.util.Log
import art.manguste.android.gamesearch.core.Game
import art.manguste.android.gamesearch.core.SearchType
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object JsonParser {
    private val TAG = JsonParser::class.java.simpleName
    @JvmStatic
    fun extractData(jsonStr: String?, searchType: SearchType): ArrayList<Game?> {
        val game = ArrayList<Game?>()
        if (jsonStr != null) {
            if (SearchType.GAME == searchType) {
                game.add(parseGameData(jsonStr, false))
            } else {
                parseDateFromMassiveRequest(jsonStr, game)
            }
        }

        //parseDateFromMassiveRequest(jsonStr, game);
        return game
    }

    @JvmStatic
    fun parseGameData(jsonStr: String?, thisIsFavorite: Boolean): Game? {
        var game: Game? = null
        try {
            val gameJson = JSONObject(jsonStr)

            // Common Info
            val id = gameJson.getInt("id")
            val slug = gameJson.getString("slug")
            val description = gameJson.getString("description") //exists also "description_raw"
            val name = gameJson.getString("name")
            val releaseDateStr = gameJson.getString("released")
            val released = DateToStr(releaseDateStr)
            val imgHttp = gameJson.getString("background_image")
            val rating = gameJson.getString("rating")
            val metacritic = gameJson.getString("metacritic")
            val website = gameJson.getString("website")

            // genres
            val genresArray = gameJson.getJSONArray("genres")
            val genres = arrayOfNulls<String>(genresArray.length())
            for (j in 0 until genresArray.length()) {
                val genreJson = genresArray.getJSONObject(j)
                genres[j] = genreJson.getString("name")
            }

            // platforms
            val platformsArray = gameJson.getJSONArray("platforms")
            val platforms = arrayOfNulls<String>(platformsArray.length())
            for (j in 0 until platformsArray.length()) {
                val tmpJson = platformsArray.getJSONObject(j)
                val platformJson = tmpJson.getJSONObject("platform")
                platforms[j] = platformJson.getString("name")
            }

            // developers
            val developersArray = gameJson.getJSONArray("developers")
            val developers = arrayOfNulls<String>(developersArray.length())
            for (j in 0 until developersArray.length()) {
                val developersJson = developersArray.getJSONObject(j)
                developers[j] = developersJson.getString("name")
            }

            // publishers
            val publishersArray = gameJson.getJSONArray("publishers")
            val publishers = arrayOfNulls<String>(publishersArray.length())
            for (j in 0 until publishersArray.length()) {
                val publishersJson = publishersArray.getJSONObject(j)
                publishers[j] = publishersJson.getString("name")
            }
            // todo fix
/*            game = Game(id, slug, name, description, released, imgHttp, rating, metacritic,
                    website, genres, platforms, developers, publishers, jsonStr, thisIsFavorite)*/
            Log.d(TAG, "Game: \n$game")
        } catch (e: JSONException) {
            Log.e(TAG, "Json parsing error: " + e.message)
        }
        return game
    }

    fun parseDateFromMassiveRequest(jsonStr: String?, game: ArrayList<Game?>) {
        try {
            val jsonObj = JSONObject(jsonStr)
            // getting json array node
            val gamesArray = jsonObj.getJSONArray("results")
            // looping through
            for (i in 0 until gamesArray.length()) {
                // common info
                val gameJson = gamesArray.getJSONObject(i)
                val slug = gameJson.getString("slug")
                val name = gameJson.getString("name")
                val releaseDateStr = gameJson.getString("released")
                val released = DateToStr(releaseDateStr)
                val imgHttp = gameJson.getString("background_image")
                val rating = gameJson.getString("rating")
                val metacritic = gameJson.getString("metacritic")

                // genres
                val genresArray = gameJson.getJSONArray("genres")
                val genres = arrayOfNulls<String>(genresArray.length())
                for (j in 0 until genresArray.length()) {
                    val genreJson = genresArray.getJSONObject(j)
                    genres[j] = genreJson.getString("name")
                }

                // platforms
                val platformsArray = gameJson.getJSONArray("platforms")
                val platforms = arrayOfNulls<String>(platformsArray.length())
                for (j in 0 until platformsArray.length()) {
                    val tmpJson = platformsArray.getJSONObject(j)
                    val platformJson = tmpJson.getJSONObject("platform")
                    platforms[j] = platformJson.getString("name")
                }
                // todo fix
                //game.add(Game(slug, name, released, imgHttp, rating, metacritic, genres, platforms, false))
                //Log.d(TAG, "Game: \n"+((Game) game.get(game.size()-1)).toString());
            }
        } catch (e: JSONException) {
            Log.e(TAG, "Json parsing error: " + e.message)
        }
    }

    private fun DateToStr(dateStr: String?): Date? {
        var date: Date? = null
        if (dateStr != null) {
            try {
                date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateStr)
            } catch (ex: ParseException) {
                Log.e(TAG, "Exception " + ex.localizedMessage)
            }
        }
        return date
    }
}