package pl.rmakowiecki.simplemusicplayer.model;

public final class Song {
    private final long id;
    private final String title;
    private final String artist;

    public Song(long id, String title, String artist) {
        this.id = id;
        this.title = title;
        this.artist = artist;
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
}
