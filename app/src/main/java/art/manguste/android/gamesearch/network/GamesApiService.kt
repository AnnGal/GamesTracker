package art.manguste.android.gamesearch.network

import art.manguste.android.gamesearch.core.ResponseJsonGamesList


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.rawg.io/api/"
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
}

object GamesApi {
    val retrofitService: GamesApiService by lazy {
        retrofit.create(GamesApiService::class.java)
    }
}


