package art.manguste.android.gamesearch.get;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import art.manguste.android.gamesearch.ui.viewcard.GameCard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GamesSearch {

    private String TAG = GamesSearch.class.getSimpleName();
    private ListView lv;

    private ArrayList<GameCard> games_list;

    public GamesSearch() {
    }

    public ArrayList<GameCard> makeSearch(){
        games_list  = new ArrayList<>();

        new GameLoaderOld().execute();

        return games_list;
    }


    private class GameLoaderOld extends AsyncTask<Void, Void, ArrayList<GameCard>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Toast.makeText(MainActivity.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected ArrayList<GameCard> doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            //String url = "https://api.androidhive.info/contacts/";
            String url = "https://api.rawg.io/api/games?page_size=5&search=dishonored";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("results");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        //String id = c.getString("id");
                        String name = c.getString("name");
                        String released = c.getString("released");

                        // adding contact to contact list
                        games_list.add(new GameCard(name, released));
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                }

            } else {
                Log.e(TAG, "Couldn't get json from the server.");

            }

            return null;
        }

    }

}
