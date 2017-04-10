package pl.rmakowiecki.simplemusicplayer.ui.screen_browse;

import pl.rmakowiecki.simplemusicplayer.R;

public enum BrowseScreenPagerEnum {
    ALBUMS(R.string.albums, R.layout.view_albums),
    TRACKS(R.string.tracks, R.layout.view_tracks);

    private int titleResId;
    private int layoutResId;

    BrowseScreenPagerEnum(int titleResId, int layoutResId) {
        this.titleResId = titleResId;
        this.layoutResId = layoutResId;
    }

    public int getTitleResId() {
        return titleResId;
    }

    public int getLayoutResId() {
        return layoutResId;
    }
}
