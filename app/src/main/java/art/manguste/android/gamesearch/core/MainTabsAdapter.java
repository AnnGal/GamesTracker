package art.manguste.android.gamesearch.core;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import art.manguste.android.gamesearch.GamesListFragment;
import art.manguste.android.gamesearch.R;


/**
 * tabs for main screen
 */
public class MainTabsAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{
            R.string.category_hot,
            R.string.category_search,
            R.string.category_favorite};
    private final Context mContext;

    // TODO move to ViewPager2
    public MainTabsAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return GamesListFragment.newInstance(SearchType.HOT);
            case 1: return GamesListFragment.newInstance(SearchType.SEARCH);
            default: return GamesListFragment.newInstance(SearchType.FAVORITE);
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