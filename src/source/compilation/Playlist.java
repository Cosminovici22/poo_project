package source.compilation;

import environment.party.user.Player;
import environment.party.user.User;
import lombok.Getter;
import lombok.Setter;
import source.track.Song;
import source.track.Track;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

@Getter @Setter
public class Playlist extends Compilation {
    private String owner;
    private boolean visible;
    private int followerCount;

    public Playlist(final String name, final String owner, final ArrayList<Track> songs) {
        super(name, songs);
        this.owner = owner;
        visible = true;
        followerCount = 0;
    }

    /**
     * Toggle the boolean variable visible
     * @return new value of visible
     */
    public boolean switchVisibility() {
        visible = !visible;
        return visible;
    }

    /**
     * Add a song to this playlist
     * @param song to be added in this playlist
     */
    public void add(final Track song) {
        getTracks().add(song);
    }

    /**
     * Remove a song from this playlist
     * @param song to be removed from this playlist
     */
    public void remove(final Song song) {
        getTracks().remove(song);
    }

    /**
     * Toggles the shuffle feature of a player on or off
     * @param player player for which to toggle the shuffle feature
     * @param seed to be used for shuffling of the playlist songs
     * @return true if the shuffle feature was toggled on or false if it was toggled off
     */
    public boolean shuffle(final Player player, final int seed) {
        ArrayList<Song> shuffledPlaylist = player.getShuffledPlaylist();
        if (shuffledPlaylist.isEmpty()) {
            for (Track track : getTracks()) {
                shuffledPlaylist.add((Song) track);
            }
            Collections.shuffle(shuffledPlaylist, new Random(seed));
            return true;
        } else {
            shuffledPlaylist.clear();
            return false;
        }
    }

    /**
     * Make a user follow/unfollow this playlist
     * @param user which wishes to follow this playlist
     * @return true if the user followed this playlist or false if they unfollowed it
     */
    public boolean follow(final User user) {
        if (user.getFollowedPlaylists().contains(this)) {
            user.getFollowedPlaylists().remove(this);
            followerCount--;
            return false;
        } else {
            user.getFollowedPlaylists().add(this);
            followerCount++;
            return true;
        }
    }

    /**
     * Fetches the added like count of all the songs in this playlist.
     * @return the added like count of all the songs in this playlist.
     */
    public int getLikeCount() {
        int likeCount = 0;

        for (Track track : getTracks()) {
            Song song = (Song) track;
            likeCount += song.getLikeCount();
        }

        return likeCount;
    }

    @Override
    public final void unload(final User user, final Integer timestamp) {
        Player player = user.getPlayer();
        player.setCompilation(null);
        player.setTrack(null);
        player.setLoadTimestamp(0);
        player.setElapsedTime(0);
        player.setRepeats(0);
        player.setPaused(true);
        player.getShuffledPlaylist().clear();
    }

    @Override
    public final void load(final User user, final Integer timestamp) {
        Player player = user.getPlayer();
        player.setAd(null);
        player.setCompilation(this);
        getTracks().get(0).load(user, timestamp);
        player.getShuffledPlaylist().clear();
    }

    @Override
    public final void next(final User user, final Integer timestamp) {
        Player player = user.getPlayer();
        ArrayList<? extends Track> tracks = player.getShuffledPlaylist().isEmpty()
                ? getTracks()
                : player.getShuffledPlaylist();
        Track ad = player.getAd();
        int idx = tracks.indexOf(player.getTrack());

        if (idx == -1) {
            idx = tracks.indexOf(ad);
            player.setAd(null);
            ad = null;
        }

        player.getTrack().unload(user, timestamp);
        if (ad != null) {
            ad.load(user, timestamp);
            player.setAd((Song) tracks.get(idx));
            return;
        }

        switch (player.getRepeats()) {
            case 0:
                if (idx == tracks.size() - 1) {
                    unload(user, timestamp);
                } else {
                    tracks.get(idx + 1).load(user, timestamp);
                }
                break;
            case 1:
                if (idx == tracks.size() - 1) {
                    tracks.get(0).load(user, timestamp);
                } else {
                    tracks.get(idx + 1).load(user, timestamp);
                }
                player.setRepeats(1);
                break;
            case 2:
                tracks.get(idx).load(user, timestamp);
                player.setRepeats(2);
                break;
            default:
                break;
        }
    }

    @Override
    public final void prev(final User user, final Integer timestamp) {
        Player player = user.getPlayer();
        ArrayList<? extends Track> tracks = player.getShuffledPlaylist().isEmpty()
                ? getTracks()
                : player.getShuffledPlaylist();
        int idx = tracks.indexOf(player.getTrack());
        int repeat = player.getRepeats();

        if (idx == 0 || player.getElapsedTime() >= 1) {
            player.getTrack().unload(user, timestamp);
            tracks.get(idx).load(user, timestamp);
        } else {
            player.getTrack().unload(user, timestamp);
            tracks.get(idx - 1).load(user, timestamp);
        }
        player.setRepeats(repeat);
    }
}
