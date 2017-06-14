package pl.rmakowiecki.simplemusicplayer.ui.screen_play;

import java.util.List;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.base.BasePresenter;

class MusicPlaybackPresenter extends BasePresenter<MusicPlaybackView> {

    private List<Song> songList;
    private int currentSongIndex;

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

    void onPlayButtonClicked() {
        view.morphAlbumCoverView();
        view.playSong(currentSongIndex);
        view.setAlbumWallpaper();
    }

    void onAlbumViewMorphComplete() {
        view.morphRevealProgressView();
    }
}
