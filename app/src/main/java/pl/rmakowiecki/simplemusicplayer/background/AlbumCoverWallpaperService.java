package pl.rmakowiecki.simplemusicplayer.background;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.support.v4.content.ContextCompat;
import android.view.SurfaceHolder;
import android.widget.ImageView;
import com.flaviofaria.kenburnsview.KenBurnsView;
import pl.rmakowiecki.simplemusicplayer.R;

public class AlbumCoverWallpaperService extends WallpaperService {

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public Engine onCreateEngine() {
        return new AlbumCoverWallpaperEngine();
    }

    private class AlbumCoverWallpaperEngine extends Engine {
        private final Handler handler = new Handler();
        private final Runnable drawRunner = this::draw;

        private Paint paint;
        private int[] colors = { 0xFFFF0000, 0xFF0000FF, 0xFFA2BC13 };
        private int bgColor;
        private int width;
        private int height;
        private boolean visible = true;
        private boolean displayHandSec;
        private ImageView albumCoverImageView;
        private SharedPreferences sharedPreferences;

        AlbumCoverWallpaperEngine() {
            albumCoverImageView = new ImageView(AlbumCoverWallpaperService.this);
            albumCoverImageView.setImageDrawable(ContextCompat.getDrawable(AlbumCoverWallpaperService.this, R.drawable.dark_background_pattern));
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(AlbumCoverWallpaperService.this);
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            bgColor = Color.parseColor("#C0C0C0");
            albumCoverImageView = new KenBurnsView(getApplicationContext());
            handler.post(drawRunner);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                handler.post(drawRunner);
            } else {
                handler.removeCallbacks(drawRunner);
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(drawRunner);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                int width, int height) {
            this.width = width;
            this.height = height;
            super.onSurfaceChanged(holder, format, width, height);
        }

        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    draw(canvas);
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }

            handler.removeCallbacks(drawRunner);

            if (visible) {
                handler.postDelayed(drawRunner, 1000);
            }
        }

        private void draw(Canvas canvas) {
            canvas.drawColor(bgColor);
            albumCoverImageView.draw(canvas);
        }
    }
}
