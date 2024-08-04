package com.example.app;

import java.util.Objects;

public class Song {
    private String title;
    private String artist;
    private String videoId;
    private String albumArtUrl;

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getAlbumArtUrl() {
        return albumArtUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(title, song.title) &&
                Objects.equals(artist, song.artist) &&
                Objects.equals(videoId, song.videoId) &&
                Objects.equals(albumArtUrl, song.albumArtUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, artist, videoId, albumArtUrl);
    }
}
