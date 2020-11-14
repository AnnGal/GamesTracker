package art.manguste.android.gamesearch

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import art.manguste.android.gamesearch.api.URLMaker.formURL
import art.manguste.android.gamesearch.core.SearchType
import kotlinx.android.synthetic.main.activity_test_connection.*

class TestURLActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_connection)

        ed_game_title.setText("dishonored")

        tv_request_txt.setText("")
    }

    fun onStartSearch(view: View?) {
        val searchTxt = ed_game_title.text.toString()
        tv_request_txt.setText(formURL(SearchType.GAME, searchTxt))
    }
}