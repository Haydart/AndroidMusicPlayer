package pl.rmakowiecki.simplemusicplayer.ui.screen_play;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import java.util.List;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.background.MusicPlayerService;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.util.Constants;

public class MusicPlayActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.appbar) AppBarLayout appBar;

    private MusicPlayerService musicPlayerService;
    private Intent musicPlayIntent;
    private boolean musicBound = false;
    private ServiceConnection musicServiceConnection;
    private List<Song> songPlaybackList;

    @OnClick(R.id.play_button)
    public void onPlayButtonClicked() {
        musicPlayerService.setCurrentSong(0);
        musicPlayerService.playCurrentSong();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        retrieveSongsPlaylist();
        initMusicPlaybackServiceConnection();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (musicPlayIntent == null) {
            musicPlayIntent = new Intent(this, MusicPlayerService.class);
            bindService(musicPlayIntent, musicServiceConnection, Context.BIND_AUTO_CREATE);
            startService(musicPlayIntent);
        }
    }

    private void retrieveSongsPlaylist() {
        songPlaybackList = getIntent().getParcelableArrayListExtra(Constants.EXTRA_SONGS_LIST);
    }

    private void initMusicPlaybackServiceConnection() {
        musicServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MusicPlayerService.MusicBinder binder = (MusicPlayerService.MusicBinder) service;
                musicPlayerService = binder.getService();
                musicPlayerService.setPlaybackList(songPlaybackList);
                musicBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                musicBound = false;
            }
        };
    }

    @Override
    protected void onDestroy() {
        if (musicServiceConnection != null) {
            unbindService(musicServiceConnection);
        }
        stopService(musicPlayIntent);
        musicPlayerService = null;
        super.onDestroy();
    }
}
