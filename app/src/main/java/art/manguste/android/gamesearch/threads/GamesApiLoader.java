package art.manguste.android.gamesearch.threads;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;

import art.manguste.android.gamesearch.api.HttpHandler;
import art.manguste.android.gamesearch.core.Game;
import art.manguste.android.gamesearch.core.JsonParser;
import art.manguste.android.gamesearch.core.SearchType;
import art.manguste.android.gamesearch.db.FavoriteGame;
import art.manguste.android.gamesearch.db.GameDatabase;

public class GamesApiLoader extends AsyncTaskLoader<ArrayList<Game>> {

    private static final String TAG = GamesApiLoader.class.getSimpleName()+"CheckLoader";

    private String mUrl;
    private SearchType mSearchType;
    private Boolean alreadyAsked = false;

    public GamesApiLoader(Context context, String url, SearchType searchType) {
        super(context);
        mUrl = url;
        mSearchType = searchType;
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading: " + mSearchType.toString());

        forceLoad();
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

        if (games != null && games.size() > 0){
            ArrayList<FavoriteGame> favGames = (ArrayList<FavoriteGame>) GameDatabase.getInstance(getContext()).favoriteGameDao().selectAll();
            for (Game game : games) {
                for (FavoriteGame gameFavorite : favGames) {
                    if (game.getGameAlias().equals(gameFavorite.getApiAlias())){
                        Log.d(TAG, "Favorite: " + game.getGameAlias());
                        game.setFavorite(true);
                        break;
                    }
                }
            }
        }

/*        for (Game game : games) {
            if (isGameInFavorite(game.getGameAlias())){
                game.setFavorite(true);
            }
        }*/

        return games;
    }

    private boolean isGameInFavorite(String gameAlias){
        boolean gameInDB = false;
        int count = GameDatabase.getInstance(getContext()).favoriteGameDao().IsGameInFavorite(gameAlias);
        if (count > 0){
            gameInDB = true;
        }
        return gameInDB;
    }

}
