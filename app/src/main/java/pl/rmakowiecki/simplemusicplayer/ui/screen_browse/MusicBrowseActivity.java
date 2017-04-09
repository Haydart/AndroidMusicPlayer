package pl.rmakowiecki.simplemusicplayer.ui.screen_browse;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rmakowiecki.simplemusicplayer.R;

public class MusicBrowseActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.container) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.app_name);
        viewPager.setAdapter(new MainScreenPagerAdapter(this));
        tabLayout.setupWithViewPager(viewPager);
    }
}
