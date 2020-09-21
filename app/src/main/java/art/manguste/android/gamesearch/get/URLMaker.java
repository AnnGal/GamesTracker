package art.manguste.android.gamesearch.get;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class URLMaker {
    //TODO (1) API request should have a User-Agent header with your app name

    private final static String BASE_URL ="https://api.rawg.io/api/games";
    private final static String PARAM_QUERY = "search";
    private final static String PARAM_PAGE_SIZE = "page_size";
    private final static String PARAM_SORT = "ordering";
    private final static String PARAM_DATE_RANGE = "dates";
    private final static String rowNum = "10";  // how many rows in query
    private final static String orderBy = "-added";  // sort query by
    private static final int MONTH_GAP = -6;


    public static String formURL(SearchType search, String searchText){
        URL url = null;

        if (SearchType.SEARCH.equals(search)){
            url = createSearchByNameUrl(searchText);
        } else if (SearchType.HOT.equals(search)){
            url = createSearchHotGamesUrl();
        } else if (SearchType.GAME.equals(search)){
            url = createSearchСoncreteGameUrl(searchText);
        }

        return url.toString();
    }

    private static URL createSearchСoncreteGameUrl(String searchText) {
        //https://api.rawg.io/api/games/cyberpunk-2077
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(searchText)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

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

    private static URL createSearchHotGamesUrl() {
        // example: https://api.rawg.io/api/games?dates=2020-06-01,2020-09-15&ordering=-added

        // set last 3 month
        String dates = getDatesRange(MONTH_GAP);

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

    private static String getDatesRange(int diff) {
        Date dateNow = new Date();
        Date dateFrom = addMonth(dateNow, diff);
        return formatDate(dateFrom)+","+formatDate(dateNow);
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
