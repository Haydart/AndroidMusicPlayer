package pl.rmakowiecki.simplemusicplayer.ui.screen_album_songs_list;

import java.util.List;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.base.BasePresenter;

class AlbumSongsPresenter extends BasePresenter<AlbumSongsView> {

    private boolean isAlbumDetailsLayoutVisible = false;

    @Override
    protected void onViewCreated(AlbumSongsView view) {
        super.onViewCreated(view);
        view.loadHeaderBackground();
        view.loadAlbumCoverImage();
        view.populateSongsList();
    }

    void onListItemClicked(List<Song> songDataSource, int position) {
        view.launchMusicPlaybackScreen(songDataSource, position);
    }

    void onSharedAnimationStarted() {
        view.showHeaderAlbumCoverFrameAnimated();
    }

    void onBackButtonClicked() {
        view.hideHeaderAlbumCoverFrame();
    }

    void onShufflePlayButtonClicked(List<Song> songsList) {
        shuffleSongList();
        view.launchMusicPlaybackScreen(songsList, 0);
    }

    private void shuffleSongList() {
        // TODO: 23/05/2017 implement
    }

    void onHeaderOffsetChanged(int verticalOffset) {
        if (verticalOffset != 0) {
            if (isAlbumDetailsLayoutVisible) {
                isAlbumDetailsLayoutVisible = false;
                view.fadeOutAlbumDetails();
            }
        } else {
            isAlbumDetailsLayoutVisible = true;
            view.fadeInAlbumDetails();
        }
    }
}
