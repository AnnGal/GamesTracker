package art.manguste.android.gamesearch.get;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;
import art.manguste.android.gamesearch.core.Game;

public class GamesLoader extends AsyncTaskLoader<ArrayList<Game>> {

    private static final String TAG = GamesLoader.class.getSimpleName()+"CheckLoader";

    private String mUrl;
    private SearchType mSearchType;
    private Boolean alreadyAsked = false;

    public GamesLoader(Context context, String url, SearchType searchType) {
        super(context);
        mUrl = url;
        mSearchType = searchType;
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading: " + mSearchType.toString());

        // Loader not synchronized with fragment lifestyle and repeat same requests when going back from Detail Activity
        // or I miss something :-/
        // Anyway stay with this fix until I understand what's going out and solve it
        if (!alreadyAsked){
            alreadyAsked = true;
            forceLoad();
        }
        //forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<Game> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        Log.d(TAG, "loadInBackground: " + mSearchType.toString());
        // make a request and catch response
        HttpHandler sh = new HttpHandler();
        String jsonStr = sh.makeServiceCall(mUrl);
        // parse response
        ArrayList<Game> games = JsonParser.extractData(jsonStr, mSearchType);

        return games;
    }
}
