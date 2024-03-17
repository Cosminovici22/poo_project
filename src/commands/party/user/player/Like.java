package commands.party.user.player;

import commands.Command;
import commands.output.Output;
import environment.Library;
import environment.party.user.Player;
import environment.party.user.User;
import commands.output.MessageOutput;
import source.track.Song;

public final class Like extends Command {
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
                    "Please load a source before liking or unliking.");
        } else {
            if (player.getTrack() instanceof Song song) {
                if (song.like(currUser)) {
                    return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                            "Like registered successfully.");
                } else {
                    return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                            "Unlike registered successfully.");
                }
            } else {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "Loaded source is not a song.");
            }
        }
    }
}
