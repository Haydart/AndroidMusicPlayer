package pl.rmakowiecki.simplemusicplayer.ui.screen_play;

import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.base.BaseView;

interface MusicPlaybackView extends BaseView {

    void initMusicProgressView();

    void showSongInformation(Song currentSong);

    void animateButtonToNotPlayingState();

    void animateButtonToPlayingState();

    void fadeInAlbumCoverImage();

    void playCurrentSong();

    void pauseCurrentSong();

    void setAlbumWallpaper();

    void loadAlbumCoverImage(int songPosition);

    void morphAlbumCoverToPlayingState();

    void morphAlbumCoverToPausedState();

    void morphCollapseProgressView();

    void morphRevealProgressView();

    void playNextSong(int songIndex, boolean playImmediately);

    void playPreviousSong(int songIndex, boolean playImmediately);

    void setRotationForAlbumComponents(float morphingProgressViewRotation, float albumCoverViewRotation);

    void animateToNextSong();

    void animateToPreviousSong();
}
