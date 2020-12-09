package art.manguste.android.gamesearch

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import art.manguste.android.gamesearch.core.SearchType
import art.manguste.android.gamesearch.gamesList.GamesListFragment
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

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
        findViewById<TabLayout>(R.id.tabs).setupWithViewPager(viewPager)
        // TODO check connection

        //pin tab icons
        for (i in 0 until tabs.tabCount) {
            tabs.getTabAt(i)!!.setIcon(icons[i])
        }

    }
}

private class MainTabsAdapter(private val mContext: Context, fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> GamesListFragment.newInstance(SearchType.HOT)
            1 -> GamesListFragment.newInstance(SearchType.SEARCH)
            2 -> GamesListFragment.newInstance(SearchType.FAVORITE)
            else -> throw Exception("Unknown fragment")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
                R.string.category_hot,
                R.string.category_search,
                R.string.category_favorite)
    }
}