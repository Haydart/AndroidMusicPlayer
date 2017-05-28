package pl.rmakowiecki.simplemusicplayer.ui.screen_play;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController.MediaPlayerControl;
import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.andremion.music.MusicCoverView;
import com.squareup.picasso.Picasso;
import java.util.List;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.background.MusicPlayerService;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.util.Constants;

public class MusicPlayActivity extends AppCompatActivity implements MediaPlayerControl {

    public static final int ALBUM_COVER_IMAGE_SIZE = 1024;

    @BindView(R.id.album_cover_view) MusicCoverView albumCoverView;
    //@BindView(R.id.ken_burns_background_image) KenBurnsView backgroundAnimatedAlbumCoverView;

    @BindColor(R.color.colorAccent) int accentColor;

    private MusicPlayerService musicPlayerService;
    private Intent musicPlayIntent;
    private boolean musicBound = false;
    private ServiceConnection musicServiceConnection;
    private List<Song> songPlaybackList;
    private int currentSongIndex;

    @OnClick(R.id.play_button)
    public void onPlayButtonClicked() {
        musicPlayerService.setCurrentSong(currentSongIndex);
        musicPlayerService.playCurrentSong();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        ButterKnife.bind(this);
        retrieveSongsPlaylist();
        initMusicPlaybackServiceConnection();

        Picasso.with(this)
                .load(songPlaybackList.get(currentSongIndex).getAlbumCoverUri())
                .noFade()
                .resize(ALBUM_COVER_IMAGE_SIZE, ALBUM_COVER_IMAGE_SIZE)
                .centerInside()
                .into(albumCoverView);

        /*List<Transformation> backgroundImageTransformations = new ArrayList<>(5);
        backgroundImageTransformations.add(new KuwaharaFilterTransformation(this, 16));
        backgroundImageTransformations.add(new GrayscaleTransformation());

        Picasso.with(this)
                .load(songPlaybackList.get(currentSongIndex).getAlbumCoverUri())
                .transform(backgroundImageTransformations)
                .into(backgroundAnimatedAlbumCoverView);*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (musicPlayIntent == null) {
            musicPlayIntent = new Intent(this, MusicPlayerService.class);
            bindService(musicPlayIntent, musicServiceConnection, Context.BIND_AUTO_CREATE);
            startService(musicPlayIntent);
        }

        albumCoverView.setCallbacks(new MusicCoverView.Callbacks() {
            @Override
            public void onMorphEnd(MusicCoverView coverView) {
                if (MusicCoverView.SHAPE_CIRCLE == coverView.getShape()) {
                    coverView.start();
                }
            }

            @Override
            public void onRotateEnd(MusicCoverView coverView) {
                coverView.morph();
            }
        });

        if (((AudioManager) getSystemService(Context.AUDIO_SERVICE)).isMusicActive()) {
            albumCoverView.morph();
        } else {
            new Handler().postDelayed(() -> albumCoverView.morph(), 1000);
        }
    }

    private void retrieveSongsPlaylist() {
        Intent intent = getIntent();
        songPlaybackList = intent.getParcelableArrayListExtra(Constants.EXTRA_SONG_MODEL);
        currentSongIndex = intent.getIntExtra(Constants.EXTRA_CURRENT_SONG_POSITION, 0);
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
        super.onDestroy();
    }

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
