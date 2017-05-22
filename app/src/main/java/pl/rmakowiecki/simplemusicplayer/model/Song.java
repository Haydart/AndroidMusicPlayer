package pl.rmakowiecki.simplemusicplayer.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public final class Song implements Parcelable {
    private final long id;
    private final String title;
    private final String artist;
    private final String albumName;
    private final Uri albumCoverUri;
    private final int durationMillis;

    public Song(long id, String title, String artist, String albumName, Uri albumCoverUri, int durationMillis) {
        this.id = id;

        this.title = title;
        this.artist = artist;
        this.albumName = albumName;
        this.albumCoverUri = albumCoverUri;
        this.durationMillis = durationMillis;
    }

    protected Song(Parcel in) {
        id = in.readLong();
        title = in.readString();
        artist = in.readString();
        albumName = in.readString();
        albumCoverUri = in.readParcelable(Uri.class.getClassLoader());
        durationMillis = in.readInt();
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbumName() {
        return albumName;
    }

    public Uri getAlbumCoverUri() {
        return albumCoverUri;
    }

    public int getDurationMillis() {
        return durationMillis;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeString(albumName);
        dest.writeParcelable(albumCoverUri, flags);
        dest.writeInt(durationMillis);
    }
}
