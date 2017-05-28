package pl.rmakowiecki.simplemusicplayer.ui.screen_browse;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import butterknife.BindView;
import com.tbruyelle.rxpermissions.RxPermissions;
import java.util.ArrayList;
import java.util.List;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.background.OnClearFromRecentService;
import pl.rmakowiecki.simplemusicplayer.model.Album;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.provider.MusicProvider;
import pl.rmakowiecki.simplemusicplayer.ui.base.BaseActivity;
import pl.rmakowiecki.simplemusicplayer.ui.screen_album_details.AlbumDetailsActivity;
import pl.rmakowiecki.simplemusicplayer.ui.screen_browse.albums.AlbumsFragment;
import pl.rmakowiecki.simplemusicplayer.ui.screen_browse.songs.SongsFragment;
import pl.rmakowiecki.simplemusicplayer.ui.screen_play.MusicPlayActivity;
import pl.rmakowiecki.simplemusicplayer.ui.widget.ViewWrapper;
import pl.rmakowiecki.simplemusicplayer.util.Constants;

public class MusicBrowseActivity extends BaseActivity<MusicBrowsePresenter> implements MusicBrowseView, SongsFragment.SongClickListener, AlbumsFragment.AlbumCoverClickListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.appbar) AppBarLayout appBar;
    @BindView(R.id.container) ViewPager viewPager;
    @BindView(R.id.tab_layout) TabLayout tabLayout;

    private List<Song> songList;
    private List<Album> albumsList;
    private MusicProvider musicProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupAppBar();
        startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
        musicProvider = new MusicProvider(this);

        new RxPermissions(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(presenter::onPermissionsResponse);
    }

    private void setupAppBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
    }

    @Override
    protected void initPresenter() {
        presenter = new MusicBrowsePresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViewPager() {
        viewPager.setAdapter(new BrowseScreenPagerAdapter(getSupportFragmentManager(), this, songList, albumsList));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void showPermissionsNotGrantedMessageAndCloseApp() {
        Toast.makeText(this, "Granting permissions is required", Toast.LENGTH_SHORT).show();
        finish();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void retrieveSongsList() {
        songList = musicProvider.getDeviceSongList();
    }

    @Override
    public void retrieveAlbumsList() {
        albumsList = musicProvider.getDeviceAlbumList();
    }

    @Override
    public void onSongClicked(Song modelData, int position) {
        presenter.onSongClicked(position);
    }

    @Override
    public void launchMusicPlaybackScreen(int position) {
        Intent intent = new Intent(this, MusicPlayActivity.class);
        intent.putParcelableArrayListExtra(Constants.EXTRA_SONG_MODEL, (ArrayList<? extends Parcelable>) songList);
        intent.putExtra(Constants.EXTRA_CURRENT_SONG_POSITION, position);
        startActivity(intent);
    }

    @Override
    public void onAlbumClicked(Album album, int adapterPosition, View albumCoverImageView) {
        presenter.onAlbumClicked(album, new ViewWrapper(albumCoverImageView));
    }

    @Override
    public void launchAlbumDetailsScreen(Album album, ViewWrapper viewWrapper) {
        Intent intent = new Intent(this, AlbumDetailsActivity.class);
        intent.putExtra(Constants.EXTRA_ALBUM_MODEL, album);
        intent.putExtra(Constants.EXTRA_ALBUM_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(viewWrapper.getView()));

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                viewWrapper.getView(),
                ViewCompat.getTransitionName(viewWrapper.getView()));
        startActivity(intent, options.toBundle());
    }
}
