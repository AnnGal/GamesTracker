package art.manguste.android.gamesearch.db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.util.Date;
import art.manguste.android.gamesearch.api.HttpHandler;
import art.manguste.android.gamesearch.core.Game;
import art.manguste.android.gamesearch.core.JsonParser;
import art.manguste.android.gamesearch.core.SearchType;

import static art.manguste.android.gamesearch.api.URLMaker.formURL;

// I really should use Rx or kotlin coroutines next time
public class GameDBHelper {

    private static final String TAG = GameDBHelper.class.getSimpleName();


    /**
     * Check and change game status. If game was in favorite list - then remove it. And opposite.
     */
    public static void changeFavoriteStatus(Context context, Game game) {
        Log.d(TAG, "DB: saveGameAsFavorite ");
        (new SaveAsyncTask(context, game)).execute();
    }

    /**
     * Convert Game into FavoriteGame class for future saving into DB
     */
    public static FavoriteGame makeFavoriteGame(Game game){
        return new FavoriteGame(
                game.getId(),
                game.getName(),
                game.getGameAlias(),
                dateToTimestamp(new Date()),
                dateToTimestamp(game.getReleaseDateDef(Game.NULL_DATE)),
                Double.valueOf(game.getRating()),
                game.getJsonString());
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
                Game fullGameData;

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
                //Log.d(TAG, "DB: transform to favorite finished "+fullGameData.getGameAlias());
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
