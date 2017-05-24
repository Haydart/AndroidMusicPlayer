package pl.rmakowiecki.simplemusicplayer.ui.screen_browse;

import pl.rmakowiecki.simplemusicplayer.model.Album;
import pl.rmakowiecki.simplemusicplayer.ui.base.BaseView;

interface MusicBrowseView extends BaseView {

    void retrieveSongsList();

    void retrieveAlbumsList();

    void launchMusicPlaybackScreen(int position);

    void launchAlbumDetailsScreen(Album album, ViewWrapper viewWrapper);

    void initViewPager();

    void showPermissionsNotGrantedMessageAndCloseApp();
}
