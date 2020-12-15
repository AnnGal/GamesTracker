package art.manguste.android.gamesearch.core

import android.util.Log
import art.manguste.android.gamesearch.db.Game
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
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
        when (game) {
                is GameBriefly -> {
                        var jsonGame = JsonGame(
                                id = game.id,
                                alias = game.alias,
                                name = game.name,
                                released = game.released,
                                imgHttp = game.imgHttp,
                                rating = game.rating,
                                genres = game.genres.map { JsonGenre(id = it.id, name = it.name) }
                        )
                        //Log.d("Json", jsonStr)
                        return jsonFormat.encodeToString(jsonGame)
                }
        }
        return ""
}

internal fun parseData(data: String): GameBriefly {
        val jsonGame = jsonFormat.decodeFromString<JsonGame>(data)
        val game = GameBriefly(
                id = jsonGame.id,
                alias = jsonGame.alias,
                name = jsonGame.name,
                released = jsonGame.released,
                imgHttp = jsonGame.imgHttp,
                rating = jsonGame.rating,
                genres = jsonGame.genres.map { Genre(it.id, it.name) })
                .also { it.isFavorite = true }
        Log.d("Json", game.toString())
        return game
}

class JsonTransform {

}