package pl.rmakowiecki.simplemusicplayer.ui.screen_browse;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore.Audio.Albums;
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
import java.util.Collections;
import java.util.List;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.model.Album;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.base.BaseActivity;
import pl.rmakowiecki.simplemusicplayer.ui.screen_album_details.AlbumDetailsActivity;
import pl.rmakowiecki.simplemusicplayer.ui.screen_browse.albums.AlbumsFragment;
import pl.rmakowiecki.simplemusicplayer.ui.screen_browse.songs.SongsFragment;
import pl.rmakowiecki.simplemusicplayer.ui.screen_play.MusicPlayActivity;
import pl.rmakowiecki.simplemusicplayer.util.Constants;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Audio.AudioColumns.ALBUM;
import static android.provider.MediaStore.Audio.AudioColumns.ALBUM_ID;
import static android.provider.MediaStore.Audio.AudioColumns.ARTIST;
import static android.provider.MediaStore.Audio.AudioColumns.DURATION;
import static android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
import static android.provider.MediaStore.MediaColumns.TITLE;

public class MusicBrowseActivity extends BaseActivity<MusicBrowsePresenter> implements MusicBrowseView, SongsFragment.SongClickListener, AlbumsFragment.AlbumCoverClickListener {

    public static final String ALBUM_COVER_DIRECTORY = "content://media/external/audio/albumart";
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.appbar) AppBarLayout appBar;
    @BindView(R.id.container) ViewPager viewPager;
    @BindView(R.id.tab_layout) TabLayout tabLayout;

    private List<Song> songList;
    private List<Album> albumsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupAppBar();

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
        songList = new ArrayList<>();
        ContentResolver musicResolver = getContentResolver();
        Cursor musicCursor = musicResolver.query(EXTERNAL_CONTENT_URI, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex(TITLE);
            int idColumn = musicCursor.getColumnIndex(_ID);
            int artistColumn = musicCursor.getColumnIndex(ARTIST);
            int albumCoverColumn = musicCursor.getColumnIndex(ALBUM_ID);
            int albumNameColumn = musicCursor.getColumnIndex(ALBUM);
            int durationColumn = musicCursor.getColumnIndex(DURATION);
            do {
                long thisId = musicCursor.getLong(idColumn);
                String songTitle = musicCursor.getString(titleColumn);
                String songArtist = musicCursor.getString(artistColumn);
                String songAlbum = musicCursor.getString(albumNameColumn);
                long albumCoverId = musicCursor.getLong(albumCoverColumn);
                Uri albumCoverUriPath = Uri.parse(ALBUM_COVER_DIRECTORY);
                Uri albumArtUri = ContentUris.withAppendedId(albumCoverUriPath, albumCoverId);
                long songDuration = musicCursor.getLong(durationColumn);
                songList.add(new Song(thisId, songTitle, songArtist, songAlbum, albumArtUri, (int) songDuration));
            }
            while (musicCursor.moveToNext());
        }
        musicCursor.close();
        Collections.sort(songList, (a, b) -> a.getTitle().compareTo(b.getTitle()));
    }

    @Override
    public void retrieveAlbumsList() {
        albumsList = new ArrayList<>();
        ContentResolver musicResolver = getContentResolver();
        String[] projection = new String[] { Albums._ID, Albums.ALBUM, Albums.ARTIST, Albums.ALBUM_ART, Albums.NUMBER_OF_SONGS };
        String sortOrder = ALBUM + " COLLATE NOCASE ASC";
        Cursor musicCursor = musicResolver.query(Albums.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex(Albums.ALBUM);
            int idColumn = musicCursor.getColumnIndex(Albums._ID);
            int artistColumn = musicCursor.getColumnIndex(Albums.ARTIST);
            do {
                long id = musicCursor.getLong(idColumn);
                String name = musicCursor.getString(titleColumn);
                String artist = musicCursor.getString(artistColumn);
                Uri albumCoverUriPath = Uri.parse(ALBUM_COVER_DIRECTORY);
                Uri albumArtUri = ContentUris.withAppendedId(albumCoverUriPath, id);
                albumsList.add(new Album(id, name, artist, new ArrayList<>(), albumArtUri));
            }
            while (musicCursor.moveToNext());
        }
        musicCursor.close();
        Collections.sort(albumsList, (a, b) -> a.getArtist().compareTo(b.getArtist()));
        matchSongsWithAlbums();
    }

    private void matchSongsWithAlbums() {
        for (Song song : songList) {
            addSongToAlbum(song);
        }
    }

    private void addSongToAlbum(Song song) {
        for (Album album : albumsList) {
            if (album.getName().equals(song.getAlbumName())) {
                album.getSongs().add(song);
            }
        }
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
