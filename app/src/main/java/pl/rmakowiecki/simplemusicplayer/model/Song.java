package pl.rmakowiecki.simplemusicplayer.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public final class Song implements Parcelable {
    private final long id;
    private final String title;
    private final String artist;
    private final Uri albumCoverUri;

    public Song(long id, String title, String artist, Uri albumCoverPath) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.albumCoverUri = albumCoverPath;
    }

    protected Song(Parcel in) {
        id = in.readLong();
        title = in.readString();
        artist = in.readString();
        albumCoverUri = Uri.parse(in.readString());
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

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public Uri getAlbumCoverUri() {
        return albumCoverUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeString(albumCoverUri.toString());
    }
}
