package pl.rmakowiecki.simplemusicplayer.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import pl.rmakowiecki.simplemusicplayer.R;

/*
 * https://github.com/andremion/Music-Player/blob/master/app/src/main/java/com/sample/andremion/musicplayer/view/ProgressView.java
 */

public class MorphingProgressView extends View {

    private static final float STROKE_SIZE = 4;
    private static final float START_ANGLE = 135;
    private static final float MIDDLE_ANGLE = 270;
    private static final float GAP_ANGLE = 90;
    private static final float FULL_PROGRESS_ANGLE = 360 - GAP_ANGLE;

    private final RectF bound = new RectF();
    private final Paint paint = new Paint();

    private final float strokeSize;
    private final int backgroundColor;
    private final int foregroundColor;

    private int currentProgress;
    private int max;
    private float morph;

    public MorphingProgressView(Context context) {
        this(context, null, 0);
    }

    public MorphingProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MorphingProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final float density = getResources().getDisplayMetrics().density;
        strokeSize = STROKE_SIZE * density;

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeSize);
        paint.setStrokeCap(Paint.Cap.ROUND);

        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, outValue, true);
        foregroundColor = outValue.data;
        backgroundColor = R.color.white_alpha;

        currentProgress = 0;
        max = 100;
    }

    public int getProgress() {
        return currentProgress;
    }

    public void setProgress(int progress) {

        if (progress < 0) {
            progress = 0;
        }

        if (progress > max) {
            progress = max;
        }

        if (progress != currentProgress) {
            currentProgress = progress;
            invalidate();
        }
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max < 0) {
            max = 0;
        }
        if (max != this.max) {
            this.max = max;
            if (currentProgress > max) {
                currentProgress = max;
            }
            invalidate();
        }
    }

    public float getMorph() {
        return morph;
    }

    public void setMorph(float morph) {
        if (morph != this.morph) {
            this.morph = morph;
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = Math.max(getSuggestedMinimumWidth(), (int) strokeSize);
        int height = Math.max(getSuggestedMinimumHeight(), (int) strokeSize);

        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();

        final int measuredWidth = resolveSizeAndState(width, widthMeasureSpec, 0);
        final int measuredHeight = resolveSizeAndState(height, heightMeasureSpec, 0);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // Extra padding to avoid cuttings on arc.
        float xPadding = strokeSize / 2f;

        bound.top = getPaddingTop() + xPadding;
        bound.bottom = h - getPaddingBottom() - xPadding;
        bound.left = getPaddingLeft() + xPadding;
        bound.right = w - getPaddingRight() - xPadding;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        final float scale = max > 0 ? currentProgress / (float) max : 0;
        float startAngle = MIDDLE_ANGLE - START_ANGLE * morph;
        float sweepAngle = FULL_PROGRESS_ANGLE * morph;

        if (bound.height() <= strokeSize) {
            paint.setColor(backgroundColor);
            canvas.drawLine(bound.left, bound.top, bound.right, bound.top, paint);

            float stopX = bound.width() * scale + bound.left;

            paint.setColor(foregroundColor);
            canvas.drawLine(bound.left, bound.top, stopX, bound.top, paint);
        } else if (startAngle < 180f) {
            paint.setColor(backgroundColor);
            canvas.drawArc(bound, startAngle, sweepAngle, false, paint);

            paint.setColor(foregroundColor);
            canvas.drawArc(bound, startAngle, sweepAngle * scale, false, paint);
        } else {
            paint.setColor(backgroundColor);
            canvas.drawArc(bound, 180f, 180f, false, paint);

            paint.setColor(foregroundColor);
            canvas.drawArc(bound, 180f, 180f * scale, false, paint);
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.progress = currentProgress;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        setProgress(ss.progress);
    }

    private static class SavedState extends BaseSavedState {

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int progress;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            progress = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(progress);
        }
    }
}
