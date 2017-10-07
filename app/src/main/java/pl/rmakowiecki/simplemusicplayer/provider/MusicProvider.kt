package pl.rmakowiecki.simplemusicplayer.provider

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.BaseColumns._ID
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AudioColumns.*
import android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
import android.provider.MediaStore.MediaColumns.TITLE
import pl.rmakowiecki.simplemusicplayer.model.Album
import pl.rmakowiecki.simplemusicplayer.model.Song
import java.util.*

const val ALBUM_COVER_DIRECTORY = "content://media/external/audio/albumart"

class MusicProvider(private val context: Context) {
    private var songList by lazy { retrieveDeviceSongList() }
    private var albumList = mutableListOf<Album>()


    private fun retrieveDeviceSongList() {
        val list = mutableListOf<Song>()
        val musicResolver = context.contentResolver
        val musicCursor = musicResolver.query(EXTERNAL_CONTENT_URI, null, null, null, null).use {
            if (it != null && it.moveToFirst()) {
                val titleColumn = it.getColumnIndex(TITLE)
                val idColumn = it.getColumnIndex(_ID)
                val artistColumn = it.getColumnIndex(ARTIST)
                val albumCoverColumn = it.getColumnIndex(ALBUM_ID)
                val albumNameColumn = it.getColumnIndex(ALBUM)
                val durationColumn = it.getColumnIndex(DURATION)
                do {
                    val thisId = it.getLong(idColumn)
                    val songTitle = it.getString(titleColumn)
                    val songArtist = it.getString(artistColumn)
                    val songAlbum = it.getString(albumNameColumn)
                    val albumCoverId = it.getLong(albumCoverColumn)
                    val albumCoverUriPath = Uri.parse(ALBUM_COVER_DIRECTORY)
                    val albumArtUri = ContentUris.withAppendedId(albumCoverUriPath, albumCoverId)
                    val songDuration = it.getLong(durationColumn)
                    songList!!.add(Song(thisId, songTitle, songArtist, songAlbum, albumArtUri, songDuration.toInt()))
                } while (it.moveToNext())
            }
        }
        return Collections.sort(songList!!) { a, b -> a.title.compareTo(b.title) }
    }

    private fun retrieveDeviceAlbumList() {
        albumList = ArrayList<Album>()
        val musicResolver = context.contentResolver
        val projection = arrayOf(MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ARTIST, MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.NUMBER_OF_SONGS)
        val sortOrder = ALBUM + " COLLATE NOCASE ASC"
        val musicCursor = musicResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder)

        if (musicCursor != null && musicCursor.moveToFirst()) {
            val titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)
            val idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Albums._ID)
            val artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST)
            do {
                val id = musicCursor.getLong(idColumn)
                val name = musicCursor.getString(titleColumn)
                val artist = musicCursor.getString(artistColumn)
                val albumCoverUriPath = Uri.parse(ALBUM_COVER_DIRECTORY)
                val albumArtUri = ContentUris.withAppendedId(albumCoverUriPath, id)
                albumList!!.add(Album(id, name, artist, ArrayList<Song>(), albumArtUri))
            } while (musicCursor.moveToNext())
        }
        musicCursor!!.close()
        Collections.sort(albumList!!) { a, b -> a.artist.compareTo(b.artist) }
        matchSongsWithAlbums()
    }


    private fun matchSongsWithAlbums() =
            songList!!.forEach {
                addSongToAlbum(it)
            }

    private fun addSongToAlbum(song: Song) =
            albumList!!
                    .filter { it.name == song.albumName }
                    .forEach { it.songs.add(song) }
}
