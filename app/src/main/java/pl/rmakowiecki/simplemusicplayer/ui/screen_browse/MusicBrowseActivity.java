package pl.rmakowiecki.simplemusicplayer.ui.screen_browse;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.tbruyelle.rxpermissions.RxPermissions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.screen_browse.albums.AlbumsFragment;
import pl.rmakowiecki.simplemusicplayer.ui.screen_browse.albums.dummy.DummyContent;
import pl.rmakowiecki.simplemusicplayer.ui.screen_browse.tracks.SongsFragment;

public class MusicBrowseActivity extends AppCompatActivity implements SongsFragment.SongClickListener, AlbumsFragment.OnListFragmentInteractionListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.container) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    private List<Song> songList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        new RxPermissions(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(this::managePermissionResponse);
    }

    private void managePermissionResponse(Boolean granted) {
        if (granted) {
            getSongsList();
            viewPager.setAdapter(new BrowseScreenPagerAdapter(getSupportFragmentManager(), this, songList));
            tabLayout.setupWithViewPager(viewPager);
        } else {
            Toast.makeText(this, "Granting permissions is required", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void getSongsList() {
        songList = new ArrayList<>();
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
        musicCursor.close();
        Collections.sort(songList, (a, b) -> a.getTitle().compareTo(b.getTitle()));
    }

    @Override
    public void onSongClicked(Song item) {
        // TODO: 19/04/2017 implement
    }

    @Override
    public void onAlbumsListInteraction(DummyContent.DummyItem item) {
        // TODO: 19/04/2017 implement
    }
}
