package pl.rmakowiecki.simplemusicplayer.ui.screen_play;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.List;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.background.MusicPlayerService;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.base.BaseActivity;
import pl.rmakowiecki.simplemusicplayer.ui.widget.MorphingProgressView;
import pl.rmakowiecki.simplemusicplayer.ui.widget.MusicCoverView;
import pl.rmakowiecki.simplemusicplayer.ui.widget.MusicPlaybackSliderView;
import pl.rmakowiecki.simplemusicplayer.util.Constants;
import pl.rmakowiecki.simplemusicplayer.util.ConversionUtils;

public class MusicPlaybackActivity extends BaseActivity<MusicPlaybackPresenter> implements MusicPlaybackView, MediaPlayerControl, ViewPagerEx.OnPageChangeListener {

    public static final int ALBUM_COVER_IMAGE_SIZE = 1024;
    public static final int COVER_PAUSE_MORPH_DELAY = 350;
    public static final String HEIGHT_PROPERTY_NAME = "height";
    public static final String PADDING_PROPERTY_NAME = "paddingTop";

    @BindView(R.id.album_cover_view) MusicCoverView albumCoverView;
    @BindView(R.id.morphing_progress_view) MorphingProgressView morphingProgressView;
    @BindView(R.id.song_title_text_view) TextView songTitleTextView;
    @BindView(R.id.song_artist_text_view) TextView songArtistTextView;
    @BindView(R.id.song_album_text_view) TextView songAlbumTextView;
    @BindView(R.id.play_pause_button) ImageView playPauseButton;
    @BindView(R.id.background_slider_layout) SliderLayout backgroundSliderLayout;

    private MusicPlayerService musicPlayerService;
    private Intent musicPlayIntent;
    private ServiceConnection musicServiceConnection;
    private List<Song> songPlaybackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrieveSongsPlaylist();
        initMusicPlaybackServiceConnection();
        startMusicPlaybackService();
        supportMarqueeTextViews();

        final MusicPlaybackSliderView leftBackgroundSliderView =
                new MusicPlaybackSliderView(this, R.layout.music_playback_diagonal_background_left);
        final MusicPlaybackSliderView rightBackgroundSliderView =
                new MusicPlaybackSliderView(this, R.layout.music_playback_diagonal_background_right);

        backgroundSliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        backgroundSliderLayout.addOnPageChangeListener(this);
        backgroundSliderLayout.addSlider(leftBackgroundSliderView);
        backgroundSliderLayout.addSlider(rightBackgroundSliderView);
    }

    private void supportMarqueeTextViews() {
        songTitleTextView.setSelected(true);
        songAlbumTextView.setSelected(true);
        songArtistTextView.setSelected(true);
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
                musicPlayerService.setCurrentSong(getCurrentSongPosition());
                musicPlayerService.prepareForPlaying();
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

    @OnClick(R.id.play_pause_button)
    public void onClick() {
        presenter.onPlayPauseButtonClicked();
    }

    @Override
    public void animateButtonToNotPlayingState() {
        AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) getDrawable(R.drawable.play_to_pause_vector_anim);
        playPauseButton.setImageDrawable(drawable);
        drawable.start();
    }

    @Override
    public void animateButtonToPlayingState() {
        AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) getDrawable(R.drawable.pause_to_play_vector_anim);
        playPauseButton.setImageDrawable(drawable);
        drawable.start();
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
    public void pauseSong(int currentSongIndex) {
        musicPlayerService.pauseCurrentSong();
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
                        presenter.onAlbumCoverImageLoadError();
                    }
                });
    }

    @Override
    public void morphAlbumCoverToPlayingState() {
        albumCoverView.setCallbacks(new MusicCoverView.Callbacks() {
            @Override
            public void onMorphEnd(MusicCoverView coverView) {
                if (MusicCoverView.SHAPE_CIRCLE == coverView.getShape()) {
                    coverView.start();
                    presenter.onAlbumCoverPlayMorphComplete();
                }
            }

            @Override
            public void onRotateEnd(MusicCoverView coverView) {
                coverView.morph();
            }
        });

        new Handler().post(() -> albumCoverView.morph());
    }

    @Override
    public void morphAlbumCoverToPausedState() {
        albumCoverView.setCallbacks(new MusicCoverView.Callbacks() {
            @Override
            public void onMorphEnd(MusicCoverView coverView) {
                //no-op
            }

            @Override
            public void onRotateEnd(MusicCoverView coverView) {
                morphCollapseProgressView();
                new Handler().postDelayed(coverView::morph, COVER_PAUSE_MORPH_DELAY);
            }
        });

        albumCoverView.stop();
    }

    @Override
    public void morphCollapseProgressView() {
        PropertyValuesHolder heightHolder = PropertyValuesHolder.ofInt(HEIGHT_PROPERTY_NAME,
                morphingProgressView.getMeasuredHeight(),
                (int) ConversionUtils.convertDpToPixel(320, this));
        PropertyValuesHolder paddingHolder = PropertyValuesHolder.ofInt(PADDING_PROPERTY_NAME,
                morphingProgressView.getPaddingTop(),
                (int) ConversionUtils.convertDpToPixel(312, this));

        ValueAnimator animator = ValueAnimator.ofPropertyValuesHolder(heightHolder, paddingHolder);
        animator.addUpdateListener(valueAnimator -> {
            int updatedHeight = (int) valueAnimator.getAnimatedValue(HEIGHT_PROPERTY_NAME);
            ViewGroup.LayoutParams layoutParams = morphingProgressView.getLayoutParams();
            layoutParams.height = updatedHeight;
            morphingProgressView.setLayoutParams(layoutParams);

            int updatedPadding = (int) valueAnimator.getAnimatedValue(PADDING_PROPERTY_NAME);
            morphingProgressView.setPadding(
                    morphingProgressView.getPaddingLeft(),
                    updatedPadding,
                    morphingProgressView.getPaddingBottom(),
                    morphingProgressView.getPaddingRight()
            );
        });
        animator.start();
    }

    @Override
    public void morphRevealProgressView() {
        PropertyValuesHolder heightHolder = PropertyValuesHolder.ofInt(HEIGHT_PROPERTY_NAME,
                morphingProgressView.getMeasuredHeight(),
                (int) ConversionUtils.convertDpToPixel(300, this));
        PropertyValuesHolder paddingHolder = PropertyValuesHolder.ofInt(PADDING_PROPERTY_NAME,
                morphingProgressView.getPaddingTop(),
                (int) ConversionUtils.convertDpToPixel(0, this));

        ValueAnimator animator = ValueAnimator.ofPropertyValuesHolder(heightHolder, paddingHolder);
        animator.addUpdateListener(valueAnimator -> {
            int updatedHeight = (int) valueAnimator.getAnimatedValue(HEIGHT_PROPERTY_NAME);
            ViewGroup.LayoutParams layoutParams = morphingProgressView.getLayoutParams();
            layoutParams.height = updatedHeight;
            morphingProgressView.setLayoutParams(layoutParams);

            int updatedPadding = (int) valueAnimator.getAnimatedValue(PADDING_PROPERTY_NAME);
            morphingProgressView.setPadding(
                    morphingProgressView.getPaddingLeft(),
                    updatedPadding,
                    morphingProgressView.getPaddingBottom(),
                    morphingProgressView.getPaddingRight()
            );
        });
        animator.start();
    }

    @Override
    public void start() {
        // TODO: 15/06/2017 implement
    }

    @Override
    public void pause() {
        // TODO: 15/06/2017 implement
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
        // TODO: 15/06/2017 implement
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        presenter.onScreenScrolledHorizontally(positionOffset);
    }

    @Override
    public void setRotationForAlbumComponents(float morphingProgressViewRotation, float albumCoverViewRotation) {
        morphingProgressView.setRotationY(morphingProgressViewRotation);
        albumCoverView.setRotationY(albumCoverViewRotation);
    }

    @Override
    public void onPageSelected(int position) {
        presenter.onMusicSwiped(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //no-op
    }
}
