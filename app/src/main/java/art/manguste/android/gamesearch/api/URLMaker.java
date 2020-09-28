package art.manguste.android.gamesearch.api;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import art.manguste.android.gamesearch.core.SearchType;

public class URLMaker {

    private final static String BASE_URL ="https://api.rawg.io/api/games";
    private final static String PARAM_QUERY = "search";
    private final static String PARAM_PAGE_SIZE = "page_size";
    private final static String PARAM_SORT = "ordering";
    private final static String PARAM_DATE_RANGE = "dates";
    private final static String rowNum = "10";  // how many rows in query
    private final static String orderBy = "-added";  // sort query by
    private static final int MONTH_GAP_BACK = -3;
    private static final int MONTH_GAP_FORWARD = 3;


    /**
     * Make a URL for future request
     * @param search - enum variable. Shows what kind of url should be built
     * @param searchText - if url should contain any search word or phrase
     * @return - prepared url
     */
    public static String formURL(SearchType search, String searchText){
        URL url = null;

        if (SearchType.SEARCH.equals(search)){
            url = createSearchByNameUrl(searchText);
        } else if (SearchType.HOT.equals(search)){
            url = createSearchHotGamesUrl();
        } else if (SearchType.GAME.equals(search)){
            url = createSearchForParticularGameUrl(searchText);
        }

        return url != null ? url.toString() : null;
    }

    /**
     * form url for particular game - used when we need detail information about game
     * @param gameAlias - alias for game, which was getting from previous massive request
     * @return - url for particular game
     */
    private static URL createSearchForParticularGameUrl(String gameAlias) {
        //https://api.rawg.io/api/games/cyberpunk-2077
        URL url = null;
        if (gameAlias != null) {
            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(gameAlias)
                    .build();

            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return url;
    }

    /**
     * form url for massive search game by name
     * @param searchText - name of the game we looking for
     * @return - url for massive request by name
     */
    private static URL createSearchByNameUrl(String searchText) {
        // example https://api.rawg.io/api/games?page_size=5&search=dishonored
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_QUERY, searchText)
                .appendQueryParameter(PARAM_PAGE_SIZE, rowNum)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * form url for top hot games for the period
     * @return url for hot games
     */
    private static URL createSearchHotGamesUrl() {
        // example: https://api.rawg.io/api/games?dates=2020-06-01,2020-09-15&ordering=-added

        // set last N month
        String dates = getDatesRange(MONTH_GAP_BACK, MONTH_GAP_FORWARD);

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_DATE_RANGE, dates)
                //.appendQueryParameter(PARAM_PAGE_SIZE, rowNum)
                .appendQueryParameter(PARAM_SORT, orderBy)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Make a date range for request
     * @param stepBack - how many months before current date
     * @param stepForward - how many months after current date
     * @return - string with dates range
     */
    private static String getDatesRange(int stepBack, int stepForward) {
        Date dateNow = new Date();
        Date dateFrom = addMonth(dateNow, stepBack);
        Date dateFuture  = addMonth(dateNow, stepForward);
        return formatDate(dateFrom)+","+formatDate(dateFuture);
    }

    private static String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(dateObject);
    }

    public static Date addMonth(Date date, int diff) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, diff);
        return cal.getTime();
    }
}
