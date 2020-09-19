package art.manguste.android.gamesearch.get;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import art.manguste.android.gamesearch.core.Game;

public class JsonParser {

    private static final String TAG = JsonParser.class.getSimpleName();

    public static ArrayList<Game> extractData(String jsonStr) {
        ArrayList<Game> game = new ArrayList<>();

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                // getting json array node
                JSONArray gamesArray = jsonObj.getJSONArray("results");
                // looping through
                for (int i = 0; i < gamesArray.length(); i++) {
                    // common info
                    JSONObject gameJson = gamesArray.getJSONObject(i);
                    String slug = gameJson.getString("slug");
                    String name = gameJson.getString("name");
                    String releasedTmp = gameJson.getString("released");
                    Date released = null;
                    try {
                        released = (new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())).parse(releasedTmp);
                    } catch (ParseException ex) {
                        Log.e(TAG, "Exception "+ex.getLocalizedMessage());
                    }
                    String imgHttp =  gameJson.getString("background_image");
                    String rating = gameJson.getString("rating");
                    String metacritic = gameJson.getString("metacritic");

                    // genres
                    JSONArray genresArray = gameJson.getJSONArray("genres");
                    String[] genres = new String[genresArray.length()];
                    for (int j = 0; j < genresArray.length(); j++){
                        JSONObject genreJson = genresArray.getJSONObject(j);
                        genres[j] = genreJson.getString("name");
                    }

                    // platforms
                    JSONArray platformsArray = gameJson.getJSONArray("platforms");
                    String[] platforms = new String[platformsArray.length()];
                    for (int j = 0; j < platformsArray.length(); j++){
                        JSONObject tmpJson = platformsArray.getJSONObject(j);
                        JSONObject platformJson = tmpJson.getJSONObject("platform");
                        platforms[j] = platformJson.getString("name");
                    }

                    game.add(new Game(slug, name, null, released, imgHttp, rating, metacritic, genres, platforms, null, null));
                    Log.d(TAG, "Game: \n"+((Game) game.get(game.size()-1)).toString());
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }

        return game;
    }

    /*
    slug
name
released 2020-11-19
background_image https://media.rawg.io/media/games/26d/26d4437715bee60138dab4a7c8c59c92.jpg
rating :4.33,
metacritic:null,
genres":[
            {
               "id":4,
               "name":"Action",
               "slug":"action"
            },
            {
               "id":5,
               "name":"RPG",
               "slug":"role-playing-games-rpg"
            }
platforms:[
            {
               "platform":{
                  "id":4,
                  "name":"PC",
                  "slug":"pc"
               }
            },
            {
               "platform":{
                  "id":187,
                  "name":"PlayStation 5",
                  "slug":"playstation5"
               }
            },
            {
               "platform":{
                  "id":1,
                  "name":"Xbox One",
                  "slug":"xbox-one"
               }
            },
            {
               "platform":{
                  "id":18,
                  "name":"PlayStation 4",
                  "slug":"playstation4"
               }
            },
            {
               "platform":{
                  "id":186,
                  "name":"Xbox Series S/X",
                  "slug":"xbox-series-x"
               }
            }
         ]*
    * */

}
