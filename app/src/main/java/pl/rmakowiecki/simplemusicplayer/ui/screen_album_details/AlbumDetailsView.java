package pl.rmakowiecki.simplemusicplayer.ui.screen_album_details;

import java.util.List;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.base.BaseView;

interface AlbumDetailsView extends BaseView {
    void fadeOutAlbumDetails();

    void fadeInAlbumDetails();

    void loadAlbumCoverImage();

    void setupSongsList();

    void loadHeaderBackground();

    void launchMusicPlaybackScreen(List<Song> songDataSource, int position);

    void showHeaderAlbumCoverFrameAnimated();

    void hideHeaderAlbumCoverFrame();
}
