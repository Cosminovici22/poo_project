package source.track;

import environment.Library;
import environment.party.artist.Artist;
import environment.party.user.Player;
import environment.party.user.User;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;
import source.compilation.Playlist;

import java.util.ArrayList;

@Getter @Setter
public final class Song extends Track {
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private int releaseYear;
    private String artist;
    private int likeCount;
    private double price;

    public Song() {
    }

    public Song(final SongInput songInput) {
        super(songInput.getName(), songInput.getDuration());
        this.album = songInput.getAlbum();
        this.tags = songInput.getTags();
        this.lyrics = songInput.getLyrics();
        this.genre = songInput.getGenre();
        this.releaseYear = songInput.getReleaseYear();
        this.artist = songInput.getArtist();
        this.likeCount = 0;
        price = 0;
    }

    /**
     * Like/dislike this song for the user given as parameter
     * @param user which liked/disliked this song
     * @return true if the user liked this song or false if the user disliked it
     */
    public boolean like(final User user) {
        if (user.getLikedSongs().contains(this)) {
            user.getLikedSongs().remove(this);
            likeCount--;
            return false;
        } else {
            user.getLikedSongs().add(this);
            likeCount++;
            return true;
        }
    }

    /**
     * Add/Remove this song in the playlist given as parameter
     * @param playlist in/from which to add/remove the song
     * @return true if the song was added or false if it was removed
     */
    public boolean addRemoveInPlaylist(final Playlist playlist) {
        if (playlist.contains(this)) {
            playlist.remove(this);
            return false;
        } else {
            playlist.add(this);
            return true;
        }
    }

    @Override
    public void unload(final User user, final Integer timestamp) {
        Player player = user.getPlayer();
        if (this == Library.getInstance().getSongs().get(0)) {
            for (Artist currArtist : Library.getInstance().getArtists()) {
                currArtist.addSongRevenue(user.getGeneratedRevenueFor(currArtist, price));
            }
            user.getSongHistory().clear();
        }
        player.setTrack(null);
        player.setLoadTimestamp(0);
        player.setElapsedTime(0);
        player.setRepeats(0);
        player.setPaused(true);
    }

    @Override
    public void load(final User user, final Integer timestamp) {
        Player player = user.getPlayer();
        if (this != Library.getInstance().getSongs().get(0)) {
            addListener(user.getName());
            user.getSongHistory().add(this);
        }
        player.setAd(null);
        player.setTrack(this);
        player.setLoadTimestamp(timestamp);
        player.setElapsedTime(0);
        player.setRepeats(0);
        player.setPaused(false);
    }

    @Override
    public void next(final User user, final Integer timestamp) {
        Player player = user.getPlayer();
        Track ad = player.getAd();

        unload(user, timestamp);
        if (ad != null) {
            ad.load(user, timestamp);
            return;
        }
        switch (player.getRepeats()) {
            case 0:
                break;
            case 1:
                load(user, timestamp);
                break;
            case 2:
                load(user, timestamp);
                player.setRepeats(2);
                break;
            default:
                break;
        }
    }

    @Override
    public void prev(final User user, final Integer timestamp) {
        unload(user, timestamp);
        load(user, timestamp);
    }
}
