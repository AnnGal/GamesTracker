package art.manguste.android.gamesearch.db;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.TypeConverter;

import java.lang.ref.WeakReference;
import java.util.Date;

import art.manguste.android.gamesearch.api.HttpHandler;
import art.manguste.android.gamesearch.core.Game;
import art.manguste.android.gamesearch.core.JsonParser;
import art.manguste.android.gamesearch.core.SearchType;

import static art.manguste.android.gamesearch.api.URLMaker.formURL;


// I really should use Rx or kotlin coroutines next time
public class DBUtils {

    private static final String TAG = DBUtils.class.getSimpleName();
    private GameDatabase gameDatabase;


    // Async save the game in db
    /*public static void changeFavoriteStatus(Context context, Game game) {
        Log.d(TAG, "DB: saveGameAsFavorite ");
        (new SaveAsyncTask(context, game)).execute();
    }*/

    // Async save the game in db
    public static void changeFavoriteStatus(Context context, Game game) {
        Log.d(TAG, "DB: saveGameAsFavorite ");
        (new SaveAsyncTask(context, game)).execute();
    }

    // Convert Game.class into FavoriteGame.class
    public static FavoriteGame makeFavoriteGame(Game game){
        FavoriteGame dbGame = new FavoriteGame(
                game.getId(),
                game.getName(),
                game.getGameAlias(),
                new Date(),
                game.getReleaseDate(),
                Double.valueOf(game.getRating()),
                game.getJsonString());

        return dbGame;
    }

    private static class SaveAsyncTask extends AsyncTask<Void, Void, Void> {
        private final Game game;
        private final Context context;

        private WeakReference<Activity> weakActivity;

        public SaveAsyncTask(Context context, Game game) {
            this.game = game;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (isGameInFavorite(game.getGameAlias())){
            //if (game.isFavorite()){
                GameDatabase.getInstance(context).favoriteGameDao().deleteByAlias(game.getGameAlias());
                Log.d(TAG, "DB: removed "+game.getGameAlias()+" successfully! ");
            } else {
                Game fullGameData = null;

                if (game.getJsonString() == null) {
                    // load full data
                    String urlString = formURL(SearchType.GAME, game.getGameAlias());
                    HttpHandler sh = new HttpHandler();
                    String jsonStr = sh.makeServiceCall(urlString);
                    // parse response
                    fullGameData = JsonParser.parseGameData(jsonStr, true);
                } else fullGameData = game;

                Log.d(TAG, "DB: try to save game "+fullGameData.getGameAlias());
                FavoriteGame favGame = makeFavoriteGame(fullGameData);
                GameDatabase.getInstance(context).favoriteGameDao().insert(favGame);
                Log.d(TAG, "DB: "+fullGameData.getGameAlias()+" successfully added! ");
            }
            return null;
        }

        private boolean isGameInFavorite(String gameAlias){
            boolean gameInDB = false;
            int count = GameDatabase.getInstance(context).favoriteGameDao().IsGameInFavorite(gameAlias);
            if (count > 0){
                gameInDB = true;
            }
            return gameInDB;
        }

        @Override
        protected void onPostExecute(Void agentsCount) {
        }
    }


}
