package pl.rmakowiecki.simplemusicplayer.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import pl.rmakowiecki.simplemusicplayer.R;

public class MusicPlaybackSliderView extends BaseSliderView {

    private final Activity activity;
    private View view;
    private ImageView target;
    private int layout;

    public MusicPlaybackSliderView(Context context, int layout) {
        super(context);
        this.activity = (Activity) context;
        this.layout = layout;
    }

    @Override
    public View getView() {
        view = LayoutInflater.from(getContext()).inflate(layout, null);
        target = (ImageView) view.findViewById(R.id.slider_target_image);
        bindEventAndShow(view, target);
        return view;
    }
}
