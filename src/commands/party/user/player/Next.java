package commands.party.user.player;

import commands.Command;
import commands.output.Output;
import environment.Library;
import environment.party.user.Player;
import environment.party.user.User;
import commands.output.MessageOutput;

public final class Next extends Command {
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
                    "Please load a source before skipping to the next track.");
        } else {
            if (player.getCompilation() != null) {
                player.getCompilation().next(currUser, getTimestamp());
            } else if (player.getTrack() != library.getSongs().get(0)) {
                player.getTrack().next(currUser, getTimestamp());
            }
            if (player.getTrack() == null) {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "Please load a source before skipping to the next track.");
            } else {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "Skipped to next track successfully. The current track is "
                                + player.getTrack().getName() + ".");
            }
        }
    }
}
