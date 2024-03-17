package commands.party.user.player;

import commands.Command;
import commands.output.Output;
import environment.Library;
import environment.party.user.Player;
import environment.party.user.User;
import lombok.Setter;
import commands.output.MessageOutput;
import source.compilation.Playlist;
import source.track.Song;

import java.util.ArrayList;

@Setter
public final class AddRemoveInPlaylist extends Command {
    private int playlistId;

    @Override
    public Output execute(final Library library) {
        User currUser = library.getUser(getUsername());
        Player player = currUser.getPlayer();
        player.update(currUser, getTimestamp());

        if (currUser.isOffline()) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    getUsername() + " is offline.");
        }

        if (player.getTrack() == null) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    "Please load a source before adding to or removing from the playlist.");
        } else {
            ArrayList<Playlist> playlists = currUser.getPlaylists();
            if (playlistId > playlists.size() || playlists.get(playlistId - 1) == null) {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "The specified playlist does not exist.");
            } else {
                if (player.getTrack() instanceof Song song) {
                    if (song.addRemoveInPlaylist(playlists.get(playlistId - 1))) {
                        return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                                "Successfully added to playlist.");
                    } else {
                        return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                                "Successfully removed from playlist.");
                    }
                } else {
                    return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                            "The loaded source is not a song.");
                }
            }
        }
    }
}
