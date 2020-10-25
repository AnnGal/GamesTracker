package art.manguste.android.gamesearch;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import art.manguste.android.gamesearch.GamesListFragment;
import art.manguste.android.gamesearch.R;
import art.manguste.android.gamesearch.core.SearchType;


/**
 * returns a fragment corresponding to one of the sections/tabs/pages
 */
public class MainTabsAdapter extends FragmentStatePagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{
            R.string.category_hot,
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
            case 2: return GamesListFragment.newInstance(SearchType.FAVORITE);
            default: return null;
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