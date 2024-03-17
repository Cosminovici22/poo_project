package commands.party.user.player;

import commands.Command;
import commands.output.Output;
import environment.Library;
import environment.party.user.Player;
import environment.party.user.User;
import commands.output.MessageOutput;

public final class PlayPause extends Command {
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
                    "Please load a source before attempting to pause or resume playback.");
        } else {
            player.playPause(getTimestamp());
            if (player.isPaused()) {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "Playback paused successfully.");
            } else {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "Playback resumed successfully.");
            }
        }
    }
}
