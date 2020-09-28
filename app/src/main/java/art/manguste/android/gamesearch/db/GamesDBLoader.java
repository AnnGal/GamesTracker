package art.manguste.android.gamesearch.db;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;


/**
 * For async access to DB
 */
public class GamesDBLoader extends AsyncTaskLoader<ArrayList<FavoriteGame>> {

    private static final String TAG = GamesDBLoader.class.getSimpleName()+"CheckLoader";

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