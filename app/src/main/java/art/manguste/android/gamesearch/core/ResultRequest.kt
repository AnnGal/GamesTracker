package art.manguste.android.gamesearch.core

data class ResultRequest(
        val count: Int,
        val next: String,
        val results: List<GameBriefly>
)

data class GameBriefly(
        val id: Int,
        val name: String
)
