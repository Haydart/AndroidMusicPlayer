package pl.rmakowiecki.simplemusicplayer.ui.screen_play;

import java.util.List;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.base.BasePresenter;

class MusicPlaybackPresenter extends BasePresenter<MusicPlaybackView> {

    private List<Song> songList;
    private int currentSongIndex;
    private boolean isMusicPlaying = false;

    void onViewInitialized(List<Song> songPlaybackList, int currentSongIndex) {
        this.songList = songPlaybackList;
        this.currentSongIndex = currentSongIndex;
        view.loadAlbumCoverImage(currentSongIndex);
        view.initMusicProgressView();
        view.showSongInformation(songPlaybackList.get(currentSongIndex));
    }

    void onAlbumCoverImageLoaded() {
        view.fadeInAlbumCoverImage();
    }

    void onPlayPauseButtonClicked() {
        if (isMusicPlaying) {
            isMusicPlaying = false;
            view.animateButtonToPlayingState();
            view.morphAlbumCoverToPausedState();
            view.pauseSong(currentSongIndex);
        } else {
            isMusicPlaying = true;
            view.animateButtonToNotPlayingState();
            view.morphAlbumCoverToPlayingState();
            view.playSong(currentSongIndex);
        }

        view.setAlbumWallpaper();
    }

    void onScreenScrolledHorizontally(float positionOffsetPixels) {
        view.setRotationForAlbumComponents(positionOffsetPixels / 3, -positionOffsetPixels / 3);
    }

    void onAlbumCoverPlayMorphComplete() {
        view.morphRevealProgressView();
    }

    void onMusicSwiped(int position) {
        // TODO: 17/06/2017 implement
    }

    void onAlbumCoverImageLoadError() {
        // TODO: 17/06/2017 implement
    }
}
