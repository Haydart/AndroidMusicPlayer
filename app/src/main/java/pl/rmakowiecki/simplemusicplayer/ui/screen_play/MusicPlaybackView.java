package pl.rmakowiecki.simplemusicplayer.ui.screen_play;

import pl.rmakowiecki.simplemusicplayer.ui.base.BaseView;

interface MusicPlaybackView extends BaseView {

    void playSong(int currentSongIndex);

    void fadeInAlbumCoverImage();

    void loadAlbumCoverImage();

    void morphProgressView();
}
