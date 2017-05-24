package pl.rmakowiecki.simplemusicplayer.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;

public final class Album extends DataModel implements Parcelable {
    private final long id;
    private final String name;
    private final String artist;
    private final List<Song> songs;
    private final Uri albumCoverUri;

    public Album(long id, String name, String artist, List<Song> songs, Uri albumCoverUri) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.songs = songs;
        this.albumCoverUri = albumCoverUri;
    }

    protected Album(Parcel in) {
        id = in.readLong();
        name = in.readString();
        artist = in.readString();
        songs = in.createTypedArrayList(Song.CREATOR);
        albumCoverUri = in.readParcelable(Uri.class.getClassLoader());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public Uri getAlbumCoverUri() {
        return albumCoverUri;
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(artist);
        dest.writeTypedList(songs);
        dest.writeParcelable(albumCoverUri, flags);
    }
}
