package art.manguste.android.gamesearch

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import art.manguste.android.gamesearch.core.SearchType

/**
 * returns a fragment corresponding to one of the sections/tabs/pages
 */
class MainTabsAdapter(private val mContext: Context, fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> GamesListFragment.newInstance(SearchType.HOT)
            1 -> GamesListFragment.newInstance(SearchType.SEARCH)
            2 -> GamesListFragment.newInstance(SearchType.FAVORITE)
            else -> GamesListFragment.newInstance(SearchType.HOT)
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