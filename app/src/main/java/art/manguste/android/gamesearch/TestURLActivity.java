package art.manguste.android.gamesearch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import java.net.MalformedURLException;
import java.net.URL;

import art.manguste.android.gamesearch.get.SearchType;

import static art.manguste.android.gamesearch.get.URLMaker.formURL;

public class TestURLActivity extends AppCompatActivity {

    EditText mGameForSearchEditText;
    TextView mSearchResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_connection);

        mGameForSearchEditText = findViewById(R.id.ed_game_title);
        mGameForSearchEditText.setText("Dishonored");

        mSearchResultTextView = findViewById(R.id.tv_request_txt);
        mSearchResultTextView.setText("");
    }

    public void onStartSearch(View view) {
        String searchTxt = String.valueOf(mGameForSearchEditText.getText());
        mSearchResultTextView.setText(formURL(SearchType.HOT, searchTxt));
    }

    public String getURL_1(String searchWord){
        String url = null;
/*        http://api.example.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7
        To build the Uri you can use this:

        final String FORECAST_BASE_URL =
                "http://api.example.org/data/2.5/forecast/daily?";
        final String QUERY_PARAM = "q";
        final String FORMAT_PARAM = "mode";
        final String UNITS_PARAM = "units";
        final String DAYS_PARAM = "cnt";
        You can declare all this the above way or even inside the Uri.parse() and appendQueryParameter()

        android.net.Uri builtUri = Uri.parse(FORECAST_BASE_URL)
                .buildUpon()
                .appendQueryParameter(QUERY_PARAM, params[0])
                .appendQueryParameter(FORMAT_PARAM, "json")
                .appendQueryParameter(UNITS_PARAM, "metric")
                .appendQueryParameter(DAYS_PARAM, Integer.toString(7))
                .build();
        At last

        //URL url = new URL(builtUri.toString());*/
        return "url1";
    }

    public String getURL_2(String searchWord) throws MalformedURLException {
        URL url = null;

      /*  Let's say that I want to create the following URL:

        https://www.myawesomesite.com/turtles/types?type=1&sort=relevance#section-name
        To build this with the Uri.Builder I would do the following.

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.myawesomesite.com")
                .appendPath("turtles")
                .appendPath("types")
                .appendQueryParameter("type", "1")
                .appendQueryParameter("sort", "relevance")
                .fragment("section-name");
        url = new URL(builder.build().toString());    */

        return "url2";
    }

}