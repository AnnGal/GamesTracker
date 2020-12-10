package art.manguste.android.gamesearch.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.rawg.io/api/"
private const val PARAM_QUERY = "search"
private const val PARAM_PAGE_SIZE = "page_size"
private const val PARAM_SORT = "ordering"
private const val PARAM_DATE_RANGE = "dates"
private const val rowNum = "10" // how many rows in query
private const val orderBy = "-added" // sort query by
private const val MONTH_GAP = -6

private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

interface GamesApiService {
    @GET("games")
    fun getGamesList(): Call<String>
}

object GamesApi {
    val retrofitService: GamesApiService by lazy {
        retrofit.create(GamesApiService::class.java)
    }
}


