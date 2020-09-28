package art.manguste.android.gamesearch;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import art.manguste.android.gamesearch.core.MainTabsAdapter;

public class MainActivity extends AppCompatActivity {

    private int[] icons = {
            R.drawable.ic_action_search,
            R.drawable.ic_action_fire,
            R.drawable.ic_action_star
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // make an adapter
        MainTabsAdapter mainTabsAdapter = new MainTabsAdapter(this, getSupportFragmentManager());
        // add the adapter to ViewPager
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(mainTabsAdapter);

        // connect Tabs to ViewPager
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        // TODO check connection

        // pin tab icons
        for (int i = 0; i < tabs.getTabCount(); i++) {
            tabs.getTabAt(i).setIcon(icons[i]);
        }


    }
}