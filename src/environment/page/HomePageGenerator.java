package environment.page;

import environment.party.user.User;
import source.compilation.Playlist;
import source.track.Song;

import java.util.ArrayList;

public final class HomePageGenerator implements PageGeneratorStrategy {
    private User user;

    public HomePageGenerator(final User user) {
        this.user = user;
    }

    @Override
    public String generatePage() {
        final int maxCommaCount = 4;
        int commaCount = 0;
        String result = "Liked songs:\n\t[";

        ArrayList<Song> sortedSongs = new ArrayList<>(user.getLikedSongs());
        sortedSongs.sort((song1, song2) -> song2.getLikeCount() - song1.getLikeCount());

        ArrayList<Playlist> sortedPlaylists = new ArrayList<>(user.getFollowedPlaylists());
        sortedPlaylists.sort((list1, list2) -> list2.getLikeCount() - list1.getLikeCount());

        for (Song song : sortedSongs) {
            result = result.concat(song.getName());
            if (commaCount < maxCommaCount && commaCount < sortedSongs.size() - 1) {
                result = result.concat(", ");
                commaCount++;
            } else {
                break;
            }
        }
        result = result.concat("]\n\nFollowed playlists:\n\t[");
        commaCount = 0;
        for (Playlist playlist : sortedPlaylists) {
            result = result.concat(playlist.getName());
            if (commaCount < maxCommaCount && commaCount < sortedPlaylists.size() - 1) {
                result = result.concat(", ");
                commaCount++;
            } else {
                break;
            }
        }
        result = result.concat("]\n\nSong recommendations:\n\t[");
        if (user.getRandomSong() != null) {
            result = result.concat(user.getRandomSong().getName());
        }
        result = result.concat("]\n\nPlaylists recommendations:\n\t[");
        if (user.getRandomPlaylist() != null) {
            result = result.concat(user.getRandomPlaylist().getName());
        }
        if (user.getFansPlaylist() != null) {
            if (user.getRandomPlaylist() != null) {
                result = result.concat(", ");
            }
            result = result.concat(user.getFansPlaylist().getName());
        }
        result = result.concat("]");

        return result;
    }
}
