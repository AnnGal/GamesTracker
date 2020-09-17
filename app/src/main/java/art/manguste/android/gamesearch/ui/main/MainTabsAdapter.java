package art.manguste.android.gamesearch.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import art.manguste.android.gamesearch.GamesListFragment;
import art.manguste.android.gamesearch.R;
import art.manguste.android.gamesearch.get.SearchType;


/**
 * returns a fragment corresponding to one of the sections/tabs/pages
 */
public class MainTabsAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.category_hot,
            R.string.category_search,
            R.string.category_favorite};
    private final Context mContext;

    public MainTabsAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return GamesListFragment.newInstance(SearchType.HOT);
            case 1: return GamesListFragment.newInstance(SearchType.SEARCH);
            // TODO make fragment fo favorite games
            // TODO make DB for saving favorite games and info
            default: return PlaceholderFragment.newInstance(position + 1);
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}