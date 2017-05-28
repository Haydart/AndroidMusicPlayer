package pl.rmakowiecki.simplemusicplayer.ui.widget;

import android.view.View;

public final class ViewWrapper {
    private final View view;

    public ViewWrapper(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }
}
