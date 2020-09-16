package art.manguste.android.gamesearch.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import art.manguste.android.gamesearch.GameSearchFragment;
import art.manguste.android.gamesearch.R;


/**
 * returns a fragment corresponding to one of the sections/tabs/pages
 */
public class MainTabsAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.category_search, R.string.category_hot,
            R.string.category_favorite};
    private final Context mContext;

    public MainTabsAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        // TODO make fragment for game search
        if (position == 0) {
            return new GameSearchFragment();
        } else if (position == 1) {
            return new ExpectedGamesFragment();
        } else return PlaceholderFragment.newInstance(position + 1);
        // TODO make fragment fo favorite games
        // TODO make DB for saving favorite games and info
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