package pl.rmakowiecki.simplemusicplayer.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class SquareImageView extends AppCompatImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int measuredWidth, int measuredHeight) {
        super.onMeasure(measuredWidth, measuredHeight);
        int commonMeasureSpec = measuredWidth > measuredHeight ? measuredHeight : measuredWidth;
        setMeasuredDimension(commonMeasureSpec, commonMeasureSpec);
    }
}
