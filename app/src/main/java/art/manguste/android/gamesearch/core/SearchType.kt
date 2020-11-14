package art.manguste.android.gamesearch.core

enum class SearchType {
    SEARCH,  // search by name in API
    HOT,  // search trending games by period
    GAME,  // search concrete game
    FAVORITE // search favorite games into DB
}