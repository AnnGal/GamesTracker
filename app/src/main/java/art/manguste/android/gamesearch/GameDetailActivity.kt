package art.manguste.android.gamesearch

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

class GameDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)
        var gameCode: String? = null
        var gameName: String? = null
        val intent = intent
        if (intent.hasExtra(EXTRA_GAME_CODE)) {
            gameCode = intent.getStringExtra(EXTRA_GAME_CODE)
        }
        if (intent.hasExtra(EXTRA_GAME_NAME)) {
            gameName = intent.getStringExtra(EXTRA_GAME_NAME)
        }
        if (gameCode != null) {
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                        .add(R.id.container, GameDetailFragment.Companion.createInstance(gameCode, gameName))
                        .commit()
            }
        } // TODO else write about mistake
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private val TAG = GameDetailActivity::class.java.simpleName
        const val EXTRA_GAME_CODE = "game_site_code"
        const val EXTRA_GAME_NAME = "game_name"
    }
}