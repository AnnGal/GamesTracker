package art.manguste.android.gamesearch.db;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;

import art.manguste.android.gamesearch.api.HttpHandler;
import art.manguste.android.gamesearch.core.Game;
import art.manguste.android.gamesearch.core.JsonParser;
import art.manguste.android.gamesearch.core.SearchType;

import static art.manguste.android.gamesearch.api.URLMaker.formURL;


// I really should use Rx or kotlin coroutines next time
public class GameDBHelper {

    private static final String TAG = GameDBHelper.class.getSimpleName();

    public static void saveGameAsFavorite(Context context, Game game) {
        Log.d(TAG, "DB: saveGameAsFavorite ");
        (new SaveAsyncTask(context, game)).execute();
    }

    public static FavoriteGame makeFavoriteGame(Game game){
        FavoriteGame dbGame = new FavoriteGame(
                game.getId(),
                game.getName(),
                game.getSiteName(),
                dateToTimestamp(new Date()),
                dateToTimestamp(game.getReleaseDate()),
                Double.valueOf(game.getRating()),
                game.getJsonString());

        return dbGame;
    }

    public static Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
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
            Game fullGameData = null;

            if (game.getJsonString() == null) {
                // load full data
                String urlString = formURL(SearchType.GAME, game.getSiteName());
                HttpHandler sh = new HttpHandler();
                String jsonStr = sh.makeServiceCall(urlString);
                // parse response
                fullGameData = JsonParser.parseGameData(jsonStr);
            } else fullGameData = game;

            Log.d(TAG, "DB: try to save game "+fullGameData.toString());
            FavoriteGame favGame = makeFavoriteGame(fullGameData);
            GameDatabase.getInstance(context).favoriteGameDao().insert(favGame);
            Log.d(TAG, "DB: "+fullGameData.getSiteName()+" success! ");

            return null;
        }

        @Override
        protected void onPostExecute(Void agentsCount) {
        }
    }
}
