package pl.rmakowiecki.simplemusicplayer.ui.screen_browse.songs;

import pl.rmakowiecki.simplemusicplayer.ui.base.BasePresenter;

class SongsFragmentPresenter extends BasePresenter<SongsFragmentView> {

    @Override
    protected void onViewInit(SongsFragmentView view) {
        super.onViewInit(view);
        view.setupSongsList();
    }
}
