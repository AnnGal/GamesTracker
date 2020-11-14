package art.manguste.android.gamesearch.api

import android.net.Uri
import art.manguste.android.gamesearch.core.SearchType
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

object URLMaker {
    private const val BASE_URL = "https://api.rawg.io/api/games"
    private const val PARAM_QUERY = "search"
    private const val PARAM_PAGE_SIZE = "page_size"
    private const val PARAM_SORT = "ordering"
    private const val PARAM_DATE_RANGE = "dates"
    private const val rowNum = "10" // how many rows in query
    private const val orderBy = "-added" // sort query by
    private const val MONTH_GAP = -6
    @JvmStatic
    fun formURL(search: SearchType, searchText: String): String {
        var url: URL? = null
        if (SearchType.SEARCH == search) {
            url = createSearchByNameUrl(searchText)
        } else if (SearchType.HOT == search) {
            url = createSearchHotGamesUrl()
        } else if (SearchType.GAME == search) {
            url = createSearchConcreteGameUrl(searchText)
        }
        return url.toString()
    }

    private fun createSearchConcreteGameUrl(searchText: String): URL? {
        //https://api.rawg.io/api/games/cyberpunk-2077
        val builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(searchText)
                .build()
        var url: URL? = null
        try {
            url = URL(builtUri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return url
    }

    private fun createSearchByNameUrl(searchText: String): URL? {
        // example https://api.rawg.io/api/games?page_size=5&search=dishonored
        val builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, searchText)
                .appendQueryParameter(PARAM_PAGE_SIZE, rowNum)
                .build()
        var url: URL? = null
        try {
            url = URL(builtUri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return url
    }

    private fun createSearchHotGamesUrl(): URL? {
        // example: https://api.rawg.io/api/games?dates=2020-06-01,2020-09-15&ordering=-added

        // set last N month
        val dates = getDatesRange(MONTH_GAP)
        val builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_DATE_RANGE, dates) //.appendQueryParameter(PARAM_PAGE_SIZE, rowNum)
                .appendQueryParameter(PARAM_SORT, orderBy)
                .build()
        var url: URL? = null
        try {
            url = URL(builtUri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return url
    }

    private fun getDatesRange(diff: Int): String {
        val dateNow = Date()
        val dateFrom = addMonth(dateNow, diff)
        val dateFuture = addMonth(dateNow, 3)
        return formatDate(dateFrom) + "," + formatDate(dateFuture)
    }

    private fun formatDate(dateObject: Date): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(dateObject)
    }

    fun addMonth(date: Date?, diff: Int): Date {
        val cal = Calendar.getInstance()
        cal.time = date
        cal.add(Calendar.MONTH, diff)
        return cal.time
    }
}