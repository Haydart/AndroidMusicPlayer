package pl.rmakowiecki.simplemusicplayer.ui.screen_play;

import android.util.Log;
import java.util.List;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.base.BasePresenter;

class MusicPlaybackPresenter extends BasePresenter<MusicPlaybackView> {

    private static final double FLOAT_COMPARISON_EPSILON = 0.0001;
    private static final float HALF_ANGLE = 180f;
    private List<Song> songList;
    private int currentSongIndex;
    private boolean isMusicPlaying = false;
    private boolean isSongPlayingComponentInitialized = false;

    void onViewInitialized(List<Song> songPlaybackList, int currentSongIndex) {
        this.songList = songPlaybackList;
        this.currentSongIndex = currentSongIndex;
        view.loadAlbumCoverImage(currentSongIndex);
        view.initMusicProgressView();
        view.showSongInformation(songPlaybackList.get(currentSongIndex));
    }

    void onMusicPlayingComponentInitialized() {
        isSongPlayingComponentInitialized = true;
    }

    void onAlbumCoverImageLoaded() {
        view.fadeInAlbumCoverImage();
    }

    void onPlayPauseButtonClicked() {
        if (isMusicPlaying) {
            isMusicPlaying = false;
            view.animateButtonToPlayingState();
            view.morphAlbumCoverToPausedState();
            view.pauseCurrentSong();
        } else {
            isMusicPlaying = true;
            view.animateButtonToNotPlayingState();
            view.morphAlbumCoverToPlayingState();
            view.playCurrentSong();
        }

        view.setAlbumWallpaper();
    }

    void onScreenScrolledHorizontally(float positionOffset) {
        view.setRotationForAlbumComponents(
                (positionOffset < FLOAT_COMPARISON_EPSILON ? positionOffset : (1 - positionOffset)) * -HALF_ANGLE,
                (positionOffset < FLOAT_COMPARISON_EPSILON ? positionOffset : (1 - positionOffset)) * -HALF_ANGLE
        );
    }

    void onAlbumCoverPlayMorphComplete() {
        view.morphRevealProgressView();
    }

    void onMusicSwiped(int position) {
        Log.d(getClass().getSimpleName(), "Swiped to position" + position);
        if (isSongPlayingComponentInitialized) {
            view.loadAlbumCoverImage(++currentSongIndex);
            view.showSongInformation(songList.get(currentSongIndex));
            view.playNextSong(currentSongIndex);
        }
    }

    void onAlbumCoverImageLoadError() {
        // TODO: 17/06/2017 implement
    }

    void onNextSongButtonClicked() {
        view.animateToNextSong();
    }

    void onPreviousSongButtonClicked() {
        view.animateToPreviousSong();
    }
}
