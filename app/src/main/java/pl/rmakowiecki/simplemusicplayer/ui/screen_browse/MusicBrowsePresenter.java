package pl.rmakowiecki.simplemusicplayer.ui.screen_browse;

import pl.rmakowiecki.simplemusicplayer.model.Album;
import pl.rmakowiecki.simplemusicplayer.ui.base.BasePresenter;

class MusicBrowsePresenter extends BasePresenter<MusicBrowseView> {

    void onSongClicked(int position) {
        view.launchMusicPlaybackScreen(position);
    }

    void onAlbumClicked(Album album, ViewWrapper viewWrapper) {
        view.launchAlbumDetailsScreen(album, viewWrapper);
    }

    void onPermissionsResponse(Boolean granted) {
        if (granted) {
            view.retrieveSongsList();
            view.retrieveAlbumsList();
            view.initViewPager();
        } else {
            view.showPermissionsNotGrantedMessageAndCloseApp();
        }
    }
}
