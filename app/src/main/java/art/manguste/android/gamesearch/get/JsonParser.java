package art.manguste.android.gamesearch.get;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import art.manguste.android.gamesearch.ui.viewcard.GameCard;

public class JsonParser {

    private static final String TAG = JsonParser.class.getSimpleName();

    public static ArrayList<GameCard> extractData(String jsonStr) {
        ArrayList<GameCard> cardList = new ArrayList<>();
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                // getting json array node
                JSONArray contacts = jsonObj.getJSONArray("results");
                // looping through
                for (int i = 0; i < contacts.length(); i++) {
                    JSONObject c = contacts.getJSONObject(i);
                    String name = c.getString("name");
                    String released = c.getString("released");

                    // adding contact to contact list
                    cardList.add(new GameCard(name, released));
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }

        return cardList;
    }
}
