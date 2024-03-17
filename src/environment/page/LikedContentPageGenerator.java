package environment.page;

import environment.party.user.User;
import source.compilation.Playlist;
import source.track.Song;

public final class LikedContentPageGenerator implements PageGeneratorStrategy {
    private User user;

    public LikedContentPageGenerator(final User user) {
        this.user = user;
    }

    @Override
    public String generatePage() {
        int commaCount = 0;
        String result = "Liked songs:\n\t[";

        for (Song song : user.getLikedSongs()) {
            result = result.concat(song.getName() + " - " + song.getArtist());
            if (commaCount < user.getLikedSongs().size() - 1) {
                result = result.concat(", ");
                commaCount++;
            }
        }
        result = result.concat("]\n\nFollowed playlists:\n\t[");
        commaCount = 0;
        for (Playlist playlist : user.getFollowedPlaylists()) {
            result = result.concat(playlist.getName() + " - " + playlist.getOwner());
            if (commaCount < user.getFollowedPlaylists().size() - 1) {
                result = result.concat(", ");
                commaCount++;
            }
        }
        result = result.concat("]");

        return result;
    }
}
