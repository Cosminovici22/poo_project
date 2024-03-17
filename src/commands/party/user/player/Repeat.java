package commands.party.user.player;

import commands.Command;
import commands.output.Output;
import environment.Library;
import environment.party.user.Player;
import environment.party.user.User;
import commands.output.MessageOutput;
import source.compilation.Playlist;

public final class Repeat extends Command {
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
                    "Please load a source before setting the repeat status.");
        } else {
            player.cycleRepeats();
            String repeatState = null;
            if (player.getCompilation() instanceof Playlist) {
                repeatState = player.getRepeats() == 0
                        ? "no repeat."
                        : player.getRepeats() == 1
                        ? "repeat all."
                        : "repeat current song.";
            } else {
                repeatState = player.getRepeats() == 0
                        ? "no repeat."
                        : player.getRepeats() == 1
                        ? "repeat once."
                        : "repeat infinite.";
            }
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    "Repeat mode changed to " + repeatState);
        }
    }
}
