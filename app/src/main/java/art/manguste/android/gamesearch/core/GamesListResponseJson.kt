package art.manguste.android.gamesearch.core

import com.squareup.moshi.Json

data class ResponseJsonGamesList(
        val count: Int,
        val next: String,
        val results: List<GameBriefly>
)

data class GameBriefly(
        val id: Int,
        @Json(name = "slug")
        val alias: String,
        val name: String,
        val released: String,
        @Json(name = "background_image")
        val imgHttp: String,
        val rating: String,
        val genres: List<Genre>
) {
        var isFavorite: Boolean = false
}



data class GameDetail(
        val id: Int,
        @Json(name = "slug")
        val alias: String,
        val description: String,
        val name: String,
        val released: String,
        @Json(name = "background_image")
        val imgHttp: String,
        val rating: String,
        val metacritic: String,
        val website: String,
        val genres: List<Genre>,
        val developers: List<Developer>,
        val publishers: List<Publisher>,
        val platforms: List<Platforms>
){
        val apiLink: String
                get() = API_SITE + alias
        var isFavorite: Boolean = false

        companion object {
                private const val API_SITE = "https://rawg.io/games/"
        }
}

data class Genre(
        val id: Int,
        val name: String
)
/*
"platforms":[
{
        "platform":{
        "id":186,
        "name":"Xbox Series S/X",
        "slug":"xbox-series-x",
        "image":null,
        "year_end":null,
        "year_start":2020,
        "games_count":108,
        "image_background":"https://media.rawg.io/media/games/4dc/4dc099d17435454a66cfed3e5fe1f7e5.jpg"
},
        "released_at":"2020-12-10",
        "requirements":null
},
{*/

data class Platforms(
        val platform: Platform,
        val released_at: String
)

data class Platform(
        val id: Int,
        val name: String
)
/*
"developers":[
{
        "id":9023,
        "name":"CD PROJEKT RED",
        "slug":"cd-projekt-red",
        "games_count":16,
        "image_background":"https://media.rawg.io/media/screenshots/6e1/6e13d9acb4e7a6e184f24892f52c4544.jpg"
},*/

data class Developer(
        val id: Int,
        val name: String
)

data class Publisher(
        val id: Int,
        val name: String
)
/*

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
}*/
