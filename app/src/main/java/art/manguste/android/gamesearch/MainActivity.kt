package art.manguste.android.gamesearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    var viewModel: AppViewModel? = null
    private val icons = intArrayOf(
            R.drawable.ic_action_fire,
            R.drawable.ic_action_search,
            R.drawable.ic_action_star
    )

    // TODO Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //make an adapter
        val mainTabsAdapter = MainTabsAdapter(this, supportFragmentManager)
        // add the adapter to ViewPager
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        viewPager.adapter = mainTabsAdapter

        // connect Tabs to ViewPager
        val tabs = findViewById<TabLayout>(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        // TODO check connection

        //pin tab icons
        for (i in 0 until tabs.tabCount) {
            tabs.getTabAt(i)!!.setIcon(icons[i])
        }
        viewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)
    }
}