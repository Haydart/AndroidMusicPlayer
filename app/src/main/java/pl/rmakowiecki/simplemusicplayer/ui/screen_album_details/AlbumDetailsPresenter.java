package pl.rmakowiecki.simplemusicplayer.ui.screen_album_details;

import java.util.List;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.base.BasePresenter;

class AlbumDetailsPresenter extends BasePresenter<AlbumDetailsView> {

    private boolean isAlbumDetailsLayoutVisible = false;

    @Override
    protected void onViewInit(AlbumDetailsView view) {
        super.onViewInit(view);
        view.loadHeaderBackground();
        view.loadAlbumCoverImage();
        view.setupSongsList();
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
