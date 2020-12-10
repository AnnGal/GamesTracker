package art.manguste.android.gamesearch

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import art.manguste.android.gamesearch.core.SearchType
import art.manguste.android.gamesearch.gamesList.GamesListFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class GameActivity : AppCompatActivity() {

    private val tabsIcons = linkedMapOf(
            R.string.category_hot to R.drawable.ic_action_fire,
            R.string.category_search to R.drawable.ic_action_search,
            R.string.category_favorite to R.drawable.ic_action_star
    )

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // TODO check connection

        // set view pager
        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> GamesListFragment.newInstance(SearchType.HOT)
                    1 -> GamesListFragment.newInstance(SearchType.SEARCH)
                    2 -> GamesListFragment.newInstance(SearchType.FAVORITE)
                    else -> throw Exception("Unknown fragment")
                }
            }

            override fun getItemCount(): Int {
                return tabsIcons.size
            }
        }

        // set tabs
        tabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
        // set tabs icon and text
        var indx = 0
        for (Pair in tabsIcons) {
            Log.d(GameActivity::class.java.simpleName, "$Pair")
            tabLayout.getTabAt(indx)!!.apply {
                setIcon(Pair.value)
                setText(Pair.key)
            }
            indx++
        }
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }
}

