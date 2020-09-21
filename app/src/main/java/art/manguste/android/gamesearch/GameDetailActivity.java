package art.manguste.android.gamesearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class GameDetailActivity extends AppCompatActivity {

    public static final String EXTRA_GAME_CODE = "game_site_code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        String gameCode = null;
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_GAME_CODE)){
            gameCode = intent.getStringExtra(EXTRA_GAME_CODE);
        }

        if (gameCode != null) {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, GameDetailFragment.createInstance(gameCode))
                        .commit();
            }
        } // TODO else write about mistake
    }
}