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

    public static ArrayList<Game> extractData(String jsonStr, SearchType searchType) {
        ArrayList<Game> game = new ArrayList<>();

        if (jsonStr != null) {
            if (SearchType.GAME.equals(searchType)) {
                parseGameData(jsonStr, game);
            } else  {
                parseDateFromMassiveRequest(jsonStr, game);
            }
        }

        //parseDateFromMassiveRequest(jsonStr, game);
        return game;
    }

    private static void parseGameData(String jsonStr, ArrayList<Game> game) {

        try {
            JSONObject gameJson = new JSONObject(jsonStr);

/*
            String result = "";
            result += "id="+gameJson.getString("id");
            Log.d(TAG, "Result: "+result);*/

            Integer id = gameJson.getInt("id");
            String slug = gameJson.getString("slug");
            String description = gameJson.getString("description");  //exists also "description_raw"
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
            String website = gameJson.getString("website");

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

            // developers
            JSONArray developersArray = gameJson.getJSONArray("developers");
            String[] developers = new String[developersArray.length()];
            for (int j = 0; j < developersArray.length(); j++){
                JSONObject developersJson = developersArray.getJSONObject(j);
                developers[j] = developersJson.getString("name");
            }

            // publishers
            JSONArray publishersArray = gameJson.getJSONArray("publishers");
            String[] publishers = new String[publishersArray.length()];
            for (int j = 0; j < publishersArray.length(); j++){
                JSONObject publishersJson = publishersArray.getJSONObject(j);
                publishers[j] = publishersJson.getString("name");
            }

            game.add(new Game(id, slug, name, description, released, imgHttp, rating, metacritic, website, genres, platforms , developers, publishers));
            Log.d(TAG, "Game: \n"+((Game) game.get(0)).toString());
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }


    }

    private static void parseDateFromMassiveRequest(String jsonStr, ArrayList<Game> game) {
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

                game.add(new Game(slug, name, released, imgHttp, rating, metacritic, genres, platforms));
                Log.d(TAG, "Game: \n"+((Game) game.get(game.size()-1)).toString());
            }
        } catch (final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }
    }

}
