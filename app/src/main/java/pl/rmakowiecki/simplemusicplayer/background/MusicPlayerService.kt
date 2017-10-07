package pl.rmakowiecki.simplemusicplayer.background

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.app.WallpaperManager
import android.content.ContentUris
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.os.Parcelable
import android.os.PowerManager
import android.provider.MediaStore
import pl.rmakowiecki.simplemusicplayer.R
import pl.rmakowiecki.simplemusicplayer.model.Song
import pl.rmakowiecki.simplemusicplayer.ui.screen_play.MusicPlaybackActivity
import pl.rmakowiecki.simplemusicplayer.util.Constants
import java.io.IOException
import java.util.*

private const val NOTIFICATION_ID = 1

class MusicPlayerService : Service(), MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    private var player: MediaPlayer? = null
    private var songs: List<Song> = emptyList()
    private var currentSongPosition: Int = 0
    private val musicBinder = MusicBinder()
    private var wallpaperManager: WallpaperManager? = null
    private var shouldPlayImmediately = false

    override fun onBind(intent: Intent): IBinder? {
        return musicBinder
    }

    override fun onCreate() {
        super.onCreate()
        currentSongPosition = 0
        wallpaperManager = WallpaperManager.getInstance(this)
        initMediaPlayer()
    }

    override fun onCompletion(mediaPlayer: MediaPlayer) {
        // TODO: 05/06/2017 implement
    }

    override fun onError(mediaPlayer: MediaPlayer, what: Int, extra: Int) = false

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        if (shouldPlayImmediately) {
            player?.start()
            shouldPlayImmediately = false
        }

        showMusicPlayingNotification()
    }

    private fun showMusicPlayingNotification() {
        val notificationIntent = Intent(this, MusicPlaybackActivity::class.java)
        notificationIntent.putParcelableArrayListExtra(Constants.EXTRA_SONG_MODEL, songs as ArrayList<out Parcelable>?)
        notificationIntent.putExtra(Constants.EXTRA_CURRENT_SONG_POSITION, currentSongPosition)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = Notification.Builder(this)

        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.play_button)
                .setTicker(songs?.get(currentSongPosition)?.title)
                .setOngoing(true)
                .setContentTitle(songs[currentSongPosition].title)
                .setContentText(songs?[currentSongPosition].artist)
        val notification = builder.build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun initMediaPlayer() {
        player = MediaPlayer().apply {
            setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setOnPreparedListener { }
            setOnErrorListener { }
            setOnCompletionListener { }
        }
    }

    fun setPlaybackList(songs: List<Song>) {
        this.songs = songs
    }

    fun setCurrentSong(songIndex: Int) {
        currentSongPosition = songIndex
    }

    fun prepareAndPlay() {
        shouldPlayImmediately = true
        prepare()
    }

    fun prepare() {
        player?.reset()
        val (currentSongId) = songs?[currentSongPosition]
        val trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currentSongId)

        try {
            player?.setDataSource(applicationContext, trackUri)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        player?.prepareAsync()
    }

    fun getCurrentSongPosition() = player?.currentPosition

    val currentSongDuration: Int
        get() = player?.duration

    val isPlaying: Boolean
        get() = player?.isPlaying

    fun seek(position: Int) {
        player?.seekTo(position)
    }

    fun playCurrentSong() = player?.start()

    fun pauseCurrentSong() = player?.pause()

    fun setWallpaperToAlbumCover() {
        try {
            val albumCoverUri = songs[currentSongPosition].albumCoverUri
            val albumCoverBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, albumCoverUri)
            wallpaperManager?.setBitmap(albumCoverBitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun resetWallpaperToDefault() =
            try {
                wallpaperManager?.clear()
            } catch (e: IOException) {
                e.printStackTrace()
            }

    override fun onTaskRemoved(rootIntent: Intent) = stopSelf()

    override fun onUnbind(intent: Intent) = super.onUnbind(intent)

    override fun onDestroy() {
        stopForeground(true)
        player?.stop()
        player?.release()
        resetWallpaperToDefault()
        super.onDestroy()
    }

    inner class MusicBinder : Binder() {
        val service: MusicPlayerService
            get() = this@MusicPlayerService
    }
}