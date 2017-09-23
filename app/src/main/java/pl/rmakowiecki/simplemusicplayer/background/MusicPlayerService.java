package pl.rmakowiecki.simplemusicplayer.background;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import pl.rmakowiecki.simplemusicplayer.R;
import pl.rmakowiecki.simplemusicplayer.model.Song;
import pl.rmakowiecki.simplemusicplayer.ui.screen_play.MusicPlaybackActivity;
import pl.rmakowiecki.simplemusicplayer.util.Constants;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MusicPlayerService extends Service implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    public static final int NOTIFICATION_ID = 1;
    private MediaPlayer player;
    private List<Song> songs;
    private int currentSongPosition;
    private final IBinder musicBinder = new MusicBinder();
    private WallpaperManager wallpaperManager;
    private boolean shouldPlayImmediately = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        currentSongPosition = 0;
        wallpaperManager = WallpaperManager.getInstance(this);
        initMediaPlayer();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        // TODO: 05/06/2017 implement
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (shouldPlayImmediately) {
            player.start();
            shouldPlayImmediately = false;
        }

        showMusicPlayingNotification();
    }

    private void showMusicPlayingNotification() {
        Intent notificationIntent = new Intent(this, MusicPlaybackActivity.class);
        notificationIntent.putParcelableArrayListExtra(Constants.EXTRA_SONG_MODEL, (ArrayList<? extends Parcelable>) songs);
        notificationIntent.putExtra(Constants.EXTRA_CURRENT_SONG_POSITION, currentSongPosition);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.play_button)
                .setTicker(songs.get(currentSongPosition).getTitle())
                .setOngoing(true)
                .setContentTitle(songs.get(currentSongPosition).getTitle())
                .setContentText(songs.get(currentSongPosition).getArtist());
        Notification notification = builder.build();

        startForeground(NOTIFICATION_ID, notification);
    }

    private void initMediaPlayer() {
        player = new MediaPlayer();
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnErrorListener(this);
        player.setOnCompletionListener(this);
    }

    public void setPlaybackList(List<Song> songs) {
        this.songs = songs;
    }

    public void setCurrentSong(int songIndex) {
        currentSongPosition = songIndex;
    }

    public void prepareAndPlay() {
        shouldPlayImmediately = true;
        prepare();
    }

    public void prepare() {
        Observable
                .just(null)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ignored -> {
                    player.reset();
                    Song currentlyPlayedSong = songs.get(currentSongPosition);
                    long currentSongId = currentlyPlayedSong.getId();
                    Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currentSongId);

                    try {
                        player.setDataSource(getApplicationContext(), trackUri);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    player.prepareAsync();
                });
    }

    public int getCurrentSongPosition() {
        return player.getCurrentPosition();
    }

    public int getCurrentSongDuration() {
        return player.getDuration();
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public void seek(int posn) {
        player.seekTo(posn);
    }

    public void playCurrentSong() {
        player.start();
    }

    public void pauseCurrentSong() {
        player.pause();
    }

    public void setWallpaperToAlbumCover() {
        Observable
                .just(null)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ignored -> {
                    try {
                        final Uri albumCoverUri = songs.get(currentSongPosition).getAlbumCoverUri();
                        final Bitmap albumCoverBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), albumCoverUri);
                        wallpaperManager.setBitmap(albumCoverBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void resetWallpaperToDefault() {
        try {
            wallpaperManager.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        player.stop();
        player.release();
        resetWallpaperToDefault();
        super.onDestroy();
    }

    public class MusicBinder extends Binder {
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }
}
