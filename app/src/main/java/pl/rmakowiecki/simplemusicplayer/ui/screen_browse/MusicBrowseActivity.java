package pl.rmakowiecki.simplemusicplayer.ui.screen_browse;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.ui.screen_browse.albums.AlbumsFragment;
import pl.rmakowiecki.simplemusicplayer.ui.screen_browse.albums.dummy.DummyContent;
import pl.rmakowiecki.simplemusicplayer.ui.screen_browse.tracks.SongsFragment;

public class MusicBrowseActivity extends AppCompatActivity implements SongsFragment.OnListFragmentInteractionListener, AlbumsFragment.OnListFragmentInteractionListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.container) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        viewPager.setAdapter(new BrowseScreenPagerAdapter(getSupportFragmentManager(), this));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onAlbumsListInteraction(DummyContent.DummyItem item) {
        //no-op
    }

    @Override
    public void onSongsListInteraction(pl.rmakowiecki.simplemusicplayer.ui.screen_browse.tracks.dummy.DummyContent.DummyItem item) {
        //no-op
    }
}
