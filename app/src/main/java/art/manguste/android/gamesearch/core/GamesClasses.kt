package art.manguste.android.gamesearch.core

import com.squareup.moshi.Json

abstract class GameBasic(
        @Json(name = "slug")
        open val alias: String,
        open val name: String,
        open val json: String?
)

data class ResponseJsonGamesList(
        val count: Int,
        val next: String,
        val results: List<GameBriefly>
)

data class GameBriefly(
        val id: Int,
        @Json(name = "slug")
        override val alias: String,
        override val name: String,
        val released: String,
        @Json(name = "background_image")
        val imgHttp: String,
        val rating: String,
        val genres: List<Genre>,
        override val json: String? = ""
) : GameBasic(alias, name, json) {
        var isFavorite: Boolean = false
}

data class GameDetail(
        val id: Int,
        @Json(name = "slug")
        override val alias: String,
        val description: String,
        override val name: String,
        val released: String?,
        @Json(name = "background_image")
        val imgHttp: String,
        val rating: String?,
        val metacritic: String?,
        val website: String,
        val genres: List<Genre>,
        val developers: List<Developer>,
        val publishers: List<Publisher>,
        val platforms: List<Platforms>,
        override val json: String? = ""
) : GameBasic(alias, name, json){
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

data class Platforms(
        val platform: Platform,
        val released_at: String
)

data class Platform(
        val id: Int,
        val name: String
)

data class Developer(
        val id: Int,
        val name: String
)

data class Publisher(
        val id: Int,
        val name: String
)

