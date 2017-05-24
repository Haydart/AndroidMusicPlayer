package pl.rmakowiecki.simplemusicplayer.ui.screen_browse;

import android.view.View;

public final class ViewWrapper {
    private final View view;

    ViewWrapper(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }
}
