package art.manguste.android.gamesearch.network

import art.manguste.android.gamesearch.core.ResponseJsonGamesList


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://api.rawg.io/api/"
private const val HEADER = "http.agent: GameTrackerApp"
private const val PARAM_QUERY = "search"
private const val PARAM_PAGE_SIZE = "page_size"
private const val PARAM_SORT = "ordering"
private const val PARAM_DATE_RANGE = "dates"
private const val rowNum = "10" // how many rows in query
private const val orderBy = "-added" // sort query by
private const val MONTH_GAP = -6

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

    @Headers(HEADER)
    @GET("games")
    suspend fun getGamesListSearch(@Query("page_size") pageSize: Int, @Query("search") gameName: String): ResponseJsonGamesList

    @Headers(HEADER)
    @GET("games/{currentGame}")
    suspend fun getSpecificGame(@Path("currentGame") gameAlias: String): ResponseJsonGamesList

    //Call<Cats> getAllCats(@Query("category") int categoryId);
    // example: https://api.rawg.io/api/games?dates=2020-06-01,2020-09-15&ordering=-added
    // example https://api.rawg.io/api/games?page_size=5&search=dishonored
    //https://api.rawg.io/api/games/cyberpunk-2077
/*
    @Headers({"Cache-Control: max-age=640000", "User-Agent: My-App-Name"})
    @GET("some/endpoint")
    @GET("/users/{username}")
    Call getUser(
    @Path("username") String userName
    );*/
}

object GamesApi {
    val retrofitService: GamesApiService by lazy {
        retrofit.create(GamesApiService::class.java)
    }
}


