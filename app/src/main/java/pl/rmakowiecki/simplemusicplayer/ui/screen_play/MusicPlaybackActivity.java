package pl.rmakowiecki.simplemusicplayer.ui.screen_play;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnClick;
import com.andremion.music.MusicCoverView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.List;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.background.MusicPlayerService;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.base.BaseActivity;
import pl.rmakowiecki.simplemusicplayer.ui.widget.MorphingProgressView;
import pl.rmakowiecki.simplemusicplayer.util.Constants;
import pl.rmakowiecki.simplemusicplayer.util.ConversionUtils;

public class MusicPlaybackActivity extends BaseActivity<MusicPlaybackPresenter> implements MusicPlaybackView, MediaPlayerControl {

    public static final int ALBUM_COVER_IMAGE_SIZE = 1024;

    @BindView(R.id.album_cover_view) MusicCoverView albumCoverView;
    @BindView(R.id.morping_progress_view) MorphingProgressView morphingProgressView;
    @BindView(R.id.song_title_text_view) TextView songTitleTextView;
    @BindView(R.id.song_artist_text_view) TextView songArtistTextView;
    @BindView(R.id.song_album_text_view) TextView songAlbumTextView;
    @BindView(R.id.play_pause_button) ImageView playPauseButton;

    private MusicPlayerService musicPlayerService;
    private Intent musicPlayIntent;
    private ServiceConnection musicServiceConnection;
    private List<Song> songPlaybackList;

    @OnClick(R.id.play_button)
    public void onPlayButtonClicked() {
        presenter.onPlayButtonClicked();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrieveSongsPlaylist();
        initMusicPlaybackServiceConnection();
        startMusicPlaybackService();
    }

    @OnClick(R.id.play_pause_button)
    public void onClick(ImageView playPauseButton) {
        Toast.makeText(this, "clicked play button", Toast.LENGTH_SHORT).show();

        AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) getDrawable(R.drawable.play_to_pause_vector_anim);
        playPauseButton.setImageDrawable(drawable);
        drawable.start();
    }

    private void startMusicPlaybackService() {
        if (musicPlayIntent == null) {
            musicPlayIntent = new Intent(this, MusicPlayerService.class);
            bindService(musicPlayIntent, musicServiceConnection, Context.BIND_AUTO_CREATE);
            startService(musicPlayIntent);
        }
    }

    private void initMusicPlaybackServiceConnection() {
        musicServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MusicPlayerService.MusicBinder binder = (MusicPlayerService.MusicBinder) service;
                musicPlayerService = binder.getService();
                musicPlayerService.setPlaybackList(songPlaybackList);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                //no-op
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewInitialized(songPlaybackList, getCurrentSongPosition());
    }

    @Override
    public void initMusicProgressView() {
        morphingProgressView.setProgress(75);
        morphingProgressView.setMorph(1);
    }

    @Override
    public void showSongInformation(Song currentSong) {
        songTitleTextView.setText(currentSong.getTitle());
        songArtistTextView.setText(currentSong.getArtist());
        songAlbumTextView.setText(currentSong.getAlbumName());
    }

    private void retrieveSongsPlaylist() {
        songPlaybackList = getIntent().getParcelableArrayListExtra(Constants.EXTRA_SONG_MODEL);
    }

    private int getCurrentSongPosition() {
        return getIntent().getIntExtra(Constants.EXTRA_CURRENT_SONG_POSITION, 0);
    }

    @Override
    public void fadeInAlbumCoverImage() {
        albumCoverView.animate()
                .withStartAction(() -> albumCoverView.setVisibility(View.VISIBLE))
                .alpha(1f)
                .start();
    }

    @Override
    public void playSong(int currentSongIndex) {
        musicPlayerService.setCurrentSong(currentSongIndex);
        musicPlayerService.playCurrentSong();
    }

    @Override
    public void setAlbumWallpaper() {
        musicPlayerService.setWallpaperToAlbumCover();
    }

    @Override
    public void loadAlbumCoverImage(int songPosition) {
        Picasso.with(this)
                .load(songPlaybackList.get(songPosition).getAlbumCoverUri())
                .noFade()
                .resize(ALBUM_COVER_IMAGE_SIZE, ALBUM_COVER_IMAGE_SIZE)
                .into(albumCoverView, new Callback() {
                    @Override
                    public void onSuccess() {
                        presenter.onAlbumCoverImageLoaded();
                    }

                    @Override
                    public void onError() {
                        //no-op
                    }
                });
    }

    @Override
    public void morphAlbumCoverView() {
        albumCoverView.setCallbacks(new MusicCoverView.Callbacks() {
            @Override
            public void onMorphEnd(MusicCoverView coverView) {
                if (MusicCoverView.SHAPE_CIRCLE == coverView.getShape()) {
                    coverView.start();
                }
                presenter.onAlbumViewMorphComplete();
            }

            @Override
            public void onRotateEnd(MusicCoverView coverView) {
                coverView.morph();
            }
        });

        if (((AudioManager) getSystemService(Context.AUDIO_SERVICE)).isMusicActive()) {
            albumCoverView.morph();
        } else {
            albumCoverView.morph();
        }
    }

    @Override
    public void morphCollapseProgressView() {
        PropertyValuesHolder heightHolder = PropertyValuesHolder.ofInt("height",
                morphingProgressView.getMeasuredHeight(),
                (int) ConversionUtils.convertDpToPixel(320, this));
        PropertyValuesHolder paddingHolder = PropertyValuesHolder.ofInt("paddingTop",
                morphingProgressView.getPaddingTop(),
                (int) ConversionUtils.convertDpToPixel(312, this));

        ValueAnimator animator = ValueAnimator.ofPropertyValuesHolder(heightHolder, paddingHolder);
        animator.addUpdateListener(valueAnimator -> {
            int updatedHeight = (int) valueAnimator.getAnimatedValue("height");
            ViewGroup.LayoutParams layoutParams = morphingProgressView.getLayoutParams();
            layoutParams.height = updatedHeight;
            morphingProgressView.setLayoutParams(layoutParams);

            int updatedPadding = (int) valueAnimator.getAnimatedValue("paddingTop");
            morphingProgressView.setPadding(
                    morphingProgressView.getPaddingLeft(),
                    updatedPadding,
                    morphingProgressView.getPaddingBottom(),
                    morphingProgressView.getPaddingRight()
            );
        });
        animator.setDuration(300);
        animator.start();
    }

    @Override
    public void morphRevealProgressView() {
        PropertyValuesHolder heightHolder = PropertyValuesHolder.ofInt("height",
                morphingProgressView.getMeasuredHeight(),
                (int) ConversionUtils.convertDpToPixel(300, this));
        PropertyValuesHolder paddingHolder = PropertyValuesHolder.ofInt("paddingTop",
                morphingProgressView.getPaddingTop(),
                (int) ConversionUtils.convertDpToPixel(0, this));

        ValueAnimator animator = ValueAnimator.ofPropertyValuesHolder(heightHolder, paddingHolder);
        animator.addUpdateListener(valueAnimator -> {
            int updatedHeight = (int) valueAnimator.getAnimatedValue("height");
            ViewGroup.LayoutParams layoutParams = morphingProgressView.getLayoutParams();
            layoutParams.height = updatedHeight;
            morphingProgressView.setLayoutParams(layoutParams);

            int updatedPadding = (int) valueAnimator.getAnimatedValue("paddingTop");
            morphingProgressView.setPadding(
                    morphingProgressView.getPaddingLeft(),
                    updatedPadding,
                    morphingProgressView.getPaddingBottom(),
                    morphingProgressView.getPaddingRight()
            );
        });
        animator.setDuration(300);
        animator.start();
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

    @Override
    protected void initPresenter() {
        presenter = new MusicPlaybackPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_music_play;
    }

    @Override
    protected void onDestroy() {
        if (musicServiceConnection != null) {
            unbindService(musicServiceConnection);
        }
        super.onDestroy();
    }
}
