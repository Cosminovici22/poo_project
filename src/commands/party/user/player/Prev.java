package commands.party.user.player;

import commands.Command;
import commands.output.Output;
import environment.Library;
import environment.party.user.Player;
import environment.party.user.User;
import commands.output.MessageOutput;

public final class Prev extends Command {
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
                    "Please load a source before returning to the previous track.");
        } else {
            if (player.getCompilation() != null) {
                player.getCompilation().prev(currUser, getTimestamp());
            } else {
                player.getTrack().prev(currUser, getTimestamp());
            }
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    "Returned to previous track successfully. The current track is "
                            + player.getTrack().getName() + ".");
        }
    }
}
