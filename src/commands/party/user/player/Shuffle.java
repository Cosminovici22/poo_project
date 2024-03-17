package commands.party.user.player;

import commands.Command;
import commands.output.Output;
import environment.Library;
import environment.party.user.Player;
import environment.party.user.User;
import lombok.Setter;
import commands.output.MessageOutput;
import source.compilation.Playlist;

@Setter
public final class Shuffle extends Command {
    private int seed;

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
                    "Please load a source before using the shuffle function.");
        } else {
            if (player.getCompilation() instanceof Playlist playlist) {
                if (playlist.shuffle(player, seed)) {
                    return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                            "Shuffle function activated successfully.");
                } else {
                    return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                            "Shuffle function deactivated successfully.");
                }
            } else {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "The loaded source is not a playlist or an album.");
            }
        }
    }
}
