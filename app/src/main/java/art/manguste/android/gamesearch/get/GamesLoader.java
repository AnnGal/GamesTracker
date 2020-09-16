package art.manguste.android.gamesearch.get;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;
import art.manguste.android.gamesearch.ui.viewcard.GameCard;

public class GamesLoader extends AsyncTaskLoader<ArrayList<GameCard>> {

    //private static final String TAG = GamesLoader.class.getSimpleName();

    private String mUrl;


    public GamesLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<GameCard> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // make a request and catch response
        HttpHandler sh = new HttpHandler();
        String jsonStr = sh.makeServiceCall(mUrl);
        // parse response
        ArrayList<GameCard> gameCards = ParseJson.extractData(jsonStr);

        return gameCards;
    }
}
