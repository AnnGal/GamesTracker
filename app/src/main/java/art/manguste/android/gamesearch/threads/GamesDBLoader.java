package art.manguste.android.gamesearch.threads;

import android.content.Context;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;
import java.util.ArrayList;
import art.manguste.android.gamesearch.db.FavoriteGame;
import art.manguste.android.gamesearch.db.GameDatabase;


public class GamesDBLoader extends AsyncTaskLoader<ArrayList<FavoriteGame>> {

    private static final String TAG = GamesDBLoader.class.getSimpleName()+"CheckLoader";

    //private String mUrl;
    //private Boolean alreadyAsked = false;
    //private GameDatabase mGameDB;

    public GamesDBLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading: ");
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<FavoriteGame> loadInBackground() {
        Log.d(TAG, "loadInBackground: " );
        // get data from DB
        ArrayList<FavoriteGame> favGames = (ArrayList<FavoriteGame>) GameDatabase.getInstance(getContext()).favoriteGameDao().selectAll();
        Log.d(TAG, "DB: " + favGames.size());
        return favGames;
    }
}