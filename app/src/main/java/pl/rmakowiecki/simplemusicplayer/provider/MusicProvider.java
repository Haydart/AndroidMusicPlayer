package pl.rmakowiecki.simplemusicplayer.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pl.rmakowiecki.simplemusicplayer.model.Album;
import pl.rmakowiecki.simplemusicplayer.model.Song;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Audio.AudioColumns.ALBUM;
import static android.provider.MediaStore.Audio.AudioColumns.ALBUM_ID;
import static android.provider.MediaStore.Audio.AudioColumns.ARTIST;
import static android.provider.MediaStore.Audio.AudioColumns.DURATION;
import static android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
import static android.provider.MediaStore.MediaColumns.TITLE;

public class MusicProvider {
    private Context context;
    private List<Song> songList;
    private List<Album> albumList;

    public MusicProvider(Context context) {
        this.context = context;
    }

    public static final String ALBUM_COVER_DIRECTORY = "content://media/external/audio/albumart";

    public List<Song> getDeviceSongList() {
        if(songList == null) retrieveDeviceSongList();
        return songList;
    }

    private void retrieveDeviceSongList() {
        songList = new ArrayList<>();
        ContentResolver musicResolver = context.getContentResolver();
        Cursor musicCursor = musicResolver.query(EXTERNAL_CONTENT_URI, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex(TITLE);
            int idColumn = musicCursor.getColumnIndex(_ID);
            int artistColumn = musicCursor.getColumnIndex(ARTIST);
            int albumCoverColumn = musicCursor.getColumnIndex(ALBUM_ID);
            int albumNameColumn = musicCursor.getColumnIndex(ALBUM);
            int durationColumn = musicCursor.getColumnIndex(DURATION);
            do {
                long thisId = musicCursor.getLong(idColumn);
                String songTitle = musicCursor.getString(titleColumn);
                String songArtist = musicCursor.getString(artistColumn);
                String songAlbum = musicCursor.getString(albumNameColumn);
                long albumCoverId = musicCursor.getLong(albumCoverColumn);
                Uri albumCoverUriPath = Uri.parse(ALBUM_COVER_DIRECTORY);
                Uri albumArtUri = ContentUris.withAppendedId(albumCoverUriPath, albumCoverId);
                long songDuration = musicCursor.getLong(durationColumn);
                songList.add(new Song(thisId, songTitle, songArtist, songAlbum, albumArtUri, (int) songDuration));
            }
            while (musicCursor.moveToNext());
        }
        musicCursor.close();
        Collections.sort(songList, (a, b) -> a.getTitle().compareTo(b.getTitle()));
    }

    public List<Album> getDeviceAlbumList() {
        if(albumList == null) retrieveDeviceAlbumList();
        return albumList;
    }


    private void retrieveDeviceAlbumList() {
        albumList = new ArrayList<>();
        ContentResolver musicResolver = context.getContentResolver();
        String[] projection = new String[] { MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ARTIST, MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.NUMBER_OF_SONGS };
        String sortOrder = ALBUM + " COLLATE NOCASE ASC";
        Cursor musicCursor = musicResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Albums._ID);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
            do {
                long id = musicCursor.getLong(idColumn);
                String name = musicCursor.getString(titleColumn);
                String artist = musicCursor.getString(artistColumn);
                Uri albumCoverUriPath = Uri.parse(ALBUM_COVER_DIRECTORY);
                Uri albumArtUri = ContentUris.withAppendedId(albumCoverUriPath, id);
                albumList.add(new Album(id, name, artist, new ArrayList<>(), albumArtUri));
            }
            while (musicCursor.moveToNext());
        }
        musicCursor.close();
        Collections.sort(albumList, (a, b) -> a.getArtist().compareTo(b.getArtist()));
        matchSongsWithAlbums();
    }


    private void matchSongsWithAlbums() {
        for (Song song : songList) {
            addSongToAlbum(song);
        }
    }

    private void addSongToAlbum(Song song) {
        for (Album album : albumList) {
            if (album.getName().equals(song.getAlbumName())) {
                album.getSongs().add(song);
            }
        }
    }
}
