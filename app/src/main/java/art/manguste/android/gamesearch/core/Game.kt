package art.manguste.android.gamesearch.core

import java.text.SimpleDateFormat
import java.util.*


data class Game(val alias: String,
                val name: String,
                var releaseDate: Date?,
                val imgHttp: String?,
                var rating: String?,
                val metacritic: String?,
                var genresList: Array<String>,
                var platformsList: Array<String>,
                var developersList: Array<String>?,
                var publishersList: Array<String>?,
                var isFavorite: Boolean = false) {
    var id: Int? = null
        private set
    var description: String? = null
        private set
    var website: String? = null
        private set
    var jsonString: String? = null
        private set


    /**
     * Constructor for short game info
     * @param alias - site code name - used to find game via https request
     * @param name - name of the game
     * @param releaseDate - when game will or was released
     * @param imgHttp - games cover image
     * @param rating - site rating
     * @param metacritic - metacritic rating
     * @param genresList - list of genres
     * @param platformsList  - which platforms can launch
     */
/*    constructor(alias: String?, name: String, releaseDate: Date?, imgHttp: String?, rating: String?, metacritic: String?,
                genresList: Array<String>, platformsList: Array<String>, isFavorite: Boolean) {
        gameAlias = alias
        this.name = name
        this.releaseDate = releaseDate
        this.imgHttp = imgHttp
        this.rating = rating
        this.metacritic = metacritic
        this.genresList = genresList
        this.platformsList = platformsList
        this.isFavorite = isFavorite
    }*/

    /**
     * Constructor for full game info (+ id, website)
     * @param id - game id
     * @param alias - site code name - used to find game via https request
     * @param name - name of the game
     * @param description - description with html tags
     * @param releaseDate - when game will or was released
     * @param imgHttp - games cover image
     * @param rating - site rating
     * @param metacritic - metacritic rating
     * @param website - game official website
     * @param genresList - list of genres
     * @param platformsList  - which platforms can launch
     * @param developersList - who developed
     * @param publishersList - who published
     */
  /*  constructor(id: Int?, alias: String?, name: String, description: String?, releaseDate: Date?,
                imgHttp: String?, rating: String?, metacritic: String?, website: String?,
                genresList: Array<String>, platformsList: Array<String>, developersList: Array<String>, publishersList: Array<String>,
                jsonString: String?, isFavorite: Boolean): this() {
        id = id
        gameAlias = alias
        this.name = name
        this.description = description
        this.releaseDate = releaseDate
        this.imgHttp = imgHttp
        this.rating = rating
        this.metacritic = metacritic
        this.website = website
        this.genresList = genresList
        this.platformsList = platformsList
        this.developersList = developersList
        this.publishersList = publishersList
        this.jsonString = jsonString
        this.isFavorite = isFavorite
    }*/

    // the shot version for tests only
/*    constructor(name: String, description: String?) {
        this.description = description
        this.name = name
    }*/

    val releaseStr: String
        get() {
            var dateStr = ""
            if (releaseDate != null) {
                dateStr = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(releaseDate)
            }
            return dateStr
        }

    fun getGenresList(): String? {
        return makeString(genresList)
    }

    fun getPlatformsList(): String? {
        return makeString(platformsList)
    }

    fun getDevelopersList(): String? {
        return makeString(developersList)
    }

    fun getPublishersList(): String? {
        return makeString(publishersList)
    }

    private fun makeString(list: Array<String>?): String? {
        if (list == null || list.size == 0) {
            return null
        }
        val result = StringBuilder()
        for (s in list) {
            if (result.isEmpty()) {
                result.append(s)
            } else {
                result.append(DELIMITER).append(s)
            }
        }
        return result.toString()
    }

    override fun toString(): String {
        return """
            siteName=$alias
            name=$name
            description$description
            released=$releaseStr
            imgHttp=$imgHttp
            rating=$rating
            metacritic=$metacritic
            genres=${getGenresList()}
            platforms=${getPlatformsList()}
            developers=${getDevelopersList()}
            publisher=${getPublishersList()}
            """.trimIndent()
    }

    val apiLink: String
        get() = API_SITE + alias

    companion object {
        private const val DELIMITER = ", "
        private const val DATE_PATTERN = "dd-MM-yyyy"
        private const val API_SITE = "https://rawg.io/games/"
    }
}