package pl.rmakowiecki.simplemusicplayer.ui.screen_play;

import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.base.BaseView;

interface MusicPlaybackView extends BaseView {

    void playSong(int currentSongIndex);

    void initMusicProgressView();

    void showSongInformation(Song currentSong);

    void fadeInAlbumCoverImage();

    void setAlbumWallpaper();

    void loadAlbumCoverImage(int songPosition);

    void morphAlbumCoverView();

    void morphCollapseProgressView();

    void morphRevealProgressView();
}
