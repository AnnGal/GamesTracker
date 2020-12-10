package art.manguste.android.gamesearch.core

import com.squareup.moshi.Json

data class ResponseJsonGamesList(
        val count: Int,
        val next: String,
        val results: List<GameBriefly>
)

data class GameBriefly(
        val id: Int,
        val name: String,
        val released: String,
        @Json(name = "background_image")
        val imgHttp: String,
        val rating: String,
        val genres: List<Genres>
)

data class Genres(
        val id: Int,
        val name: String
)