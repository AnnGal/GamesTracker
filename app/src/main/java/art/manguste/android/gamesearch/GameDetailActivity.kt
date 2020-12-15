package art.manguste.android.gamesearch

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import art.manguste.android.gamesearch.gamesList.GameDetailFragment
import kotlinx.android.synthetic.main.fragment_game_detail.*

class GameDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        Log.d(TAG, "OnClick into activity ")
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

        Log.d(TAG, "OnClick before Fragment ")
        if (gameCode != null) {
            val bundle = Bundle().apply {
                putString(EXTRA_GAME_CODE, gameCode)
                putString(EXTRA_GAME_NAME, gameName)
            }

            supportFragmentManager.commit {
                add<GameDetailFragment>(containerViewId = android.R.id.content, args = bundle)
            }
        }
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