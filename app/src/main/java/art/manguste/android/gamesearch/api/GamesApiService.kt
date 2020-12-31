package art.manguste.android.gamesearch.api

import art.manguste.android.gamesearch.core.GameDetail
import art.manguste.android.gamesearch.core.ResponseJsonGamesList


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://api.rawg.io/api/"
private const val HEADER = "http.agent: GameTrackerApp"


private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

interface GamesApiService {
    @GET("games")
    suspend fun getGamesList(): ResponseJsonGamesList

    @Headers(HEADER)
    @GET("games")
    suspend fun getGamesHotListSorts(@Query("dates") datesRange: String, @Query("ordering") ordering: String): ResponseJsonGamesList
    // example: https://api.rawg.io/api/games?dates=2020-06-01,2020-09-15&ordering=-added

    @Headers(HEADER)
    @GET("games")
    suspend fun getGamesListSearch(@Query("page_size") pageSize: Int, @Query("search") gameName: String): ResponseJsonGamesList
    // example https://api.rawg.io/api/games?page_size=5&search=dishonored

    @Headers(HEADER)
    @GET("games/{currentGame}")
    suspend fun getSpecificGame(@Path("currentGame") gameAlias: String): GameDetail
    // example: https://api.rawg.io/api/games/cyberpunk-2077
}

object GamesApi {
    val retrofitService: GamesApiService by lazy {
        retrofit.create(GamesApiService::class.java)
    }
}


