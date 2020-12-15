package art.manguste.android.gamesearch.core

import android.util.Log
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val jsonFormat = Json { ignoreUnknownKeys = true }


@Serializable
private class JsonGame(
        val id: Int,
        val alias: String,
        val name: String,
        val released: String,
        val imgHttp: String,
        val rating: String,
        val genres: List<JsonGenre>
)

@Serializable
private class JsonGenre(
        val id: Int,
        val name: String
)

internal fun encodeData(game: GameBasic): String {
        return when (game) {
                is GameBriefly -> {
                        val jsonGame = JsonGame(
                                id = game.id,
                                alias = game.alias,
                                name = game.name,
                                released = game.released,
                                imgHttp = game.imgHttp,
                                rating = game.rating,
                                genres = game.genres.map { JsonGenre(id = it.id, name = it.name) }
                        )
                        //Log.d("Json", jsonStr)
                        jsonFormat.encodeToString(jsonGame)
                }
                is GameDetail -> {
                        val jsonGame = JsonGame(
                                id = game.id,
                                alias = game.alias,
                                name = game.name,
                                released = game.released ?: "",
                                imgHttp = game.imgHttp,
                                rating = game.rating ?: "0.0",
                                genres = game.genres.map { JsonGenre(id = it.id, name = it.name) }
                        )
                        //Log.d("Json", jsonStr)
                        jsonFormat.encodeToString(jsonGame)
                }
                else -> ""
        }

}

internal fun parseData(data: String): GameBriefly {
        val jsonGame = jsonFormat.decodeFromString<JsonGame>(data)
        return GameBriefly(
                id = jsonGame.id,
                alias = jsonGame.alias,
                name = jsonGame.name,
                released = jsonGame.released,
                imgHttp = jsonGame.imgHttp,
                rating = jsonGame.rating,
                genres = jsonGame.genres.map { Genre(it.id, it.name) })
                .also { it.isFavorite = true  }
}
