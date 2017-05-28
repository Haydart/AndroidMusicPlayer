package pl.rmakowiecki.simplemusicplayer.background;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import pl.rmakowiecki.simplemusicplayer.model.Song;

public class MusicPlayerService extends Service implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private MediaPlayer player;
    private List<Song> songs;
    private int currentSongPosition;
    private final IBinder musicBinder = new MusicBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        currentSongPosition = 0;
        initMediaPlayer();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
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

    public void playCurrentSong() {
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
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        player.stop();
        player.release();
        super.onDestroy();
    }

    public class MusicBinder extends Binder {
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }
}
