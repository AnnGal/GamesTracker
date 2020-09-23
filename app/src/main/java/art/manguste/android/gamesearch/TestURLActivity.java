package art.manguste.android.gamesearch;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
        mGameForSearchEditText.setText("dishonored");

        mSearchResultTextView = findViewById(R.id.tv_request_txt);
        mSearchResultTextView.setText("");
    }

    public void onStartSearch(View view) {
        String searchTxt = String.valueOf(mGameForSearchEditText.getText());
        mSearchResultTextView.setText(formURL(SearchType.GAME, searchTxt));
    }

}