package pl.rmakowiecki.simplemusicplayer.ui.screen_play;

import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.base.BaseView;

interface MusicPlaybackView extends BaseView {

    void playSong(int currentSongIndex);

    void initMusicProgressView();

    void showSongInformation(Song currentSong);

    void animateButtonToNotPlayingState();

    void animateButtonToPlayingState();

    void fadeInAlbumCoverImage();

    void pauseSong(int currentSongIndex);

    void setAlbumWallpaper();

    void loadAlbumCoverImage(int songPosition);

    void morphAlbumCoverToPlayingState();

    void morphAlbumCoverToPausedState();

    void morphCollapseProgressView();

    void morphRevealProgressView();
}
