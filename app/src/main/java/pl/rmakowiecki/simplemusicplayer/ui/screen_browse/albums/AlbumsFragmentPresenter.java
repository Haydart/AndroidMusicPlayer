package pl.rmakowiecki.simplemusicplayer.ui.screen_browse.albums;

import pl.rmakowiecki.simplemusicplayer.ui.base.BasePresenter;

class AlbumsFragmentPresenter extends BasePresenter<AlbumsFragmentView> {
    @Override
    protected void onViewInit(AlbumsFragmentView view) {
        super.onViewInit(view);
        view.setupAlbumsList();
    }
}
