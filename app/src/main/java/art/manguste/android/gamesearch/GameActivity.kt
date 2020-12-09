package art.manguste.android.gamesearch

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import art.manguste.android.gamesearch.core.SearchType
import art.manguste.android.gamesearch.gamesList.GamesListFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class GameActivity : AppCompatActivity() {

    private val icons = intArrayOf(
            R.drawable.ic_action_fire,
            R.drawable.ic_action_search,
            R.drawable.ic_action_star
    )

    private val TAB_TITLES = intArrayOf(
            R.string.category_hot,
            R.string.category_search,
            R.string.category_favorite)

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        // connect Tabs to ViewPager
/*        val tabs = findViewById<TabLayout>(R.id.tabs)
        findViewById<TabLayout>(R.id.tabs).setupWithViewPager(viewPager)*/
        // TODO check connection

        //pin tab icons
/*        for (i in 0 until tabs.tabCount) {
            tabs.getTabAt(i)!!.setIcon(icons[i])
        }*/

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
                return icons.size
            }
        }

        tabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        for (i in 0 until tabLayout.tabCount) {
            tabLayout.getTabAt(i)!!.setIcon(icons[i])
        }

  /*      findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }
}

