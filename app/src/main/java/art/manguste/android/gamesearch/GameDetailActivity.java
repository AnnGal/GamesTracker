package art.manguste.android.gamesearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class GameDetailActivity extends AppCompatActivity {

    private static final String TAG = GameDetailActivity.class.getSimpleName();
    public static final String EXTRA_GAME_CODE = "game_site_code";
    public static final String EXTRA_GAME_NAME = "game_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        String gameCode = null;
        String gameName = null;
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_GAME_CODE)){
            gameCode = intent.getStringExtra(EXTRA_GAME_CODE);
        }
        if (intent.hasExtra(EXTRA_GAME_NAME)){
            gameName = intent.getStringExtra(EXTRA_GAME_NAME);
        }

        if (gameCode != null) {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, GameDetailFragment.createInstance(gameCode, gameName))
                        .commit();
            }
        } // TODO else write about mistake
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}