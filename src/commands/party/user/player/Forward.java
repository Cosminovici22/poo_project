package commands.party.user.player;

import commands.Command;
import commands.output.Output;
import environment.Library;
import environment.party.user.Player;
import environment.party.user.User;
import commands.output.MessageOutput;
import source.compilation.Podcast;

public final class Forward extends Command {
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
                    "Please load a source before attempting to forward.");
        } else {
            if (player.getCompilation() instanceof Podcast podcast) {
                podcast.forward(player);
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "Skipped forward successfully.");
            } else {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "The loaded source is not a podcast.");
            }
        }
    }
}
