package commands.party.user.player;

import commands.Command;
import commands.output.Output;
import environment.Library;
import environment.party.user.Player;
import environment.party.user.User;
import commands.output.MessageOutput;
import source.compilation.Podcast;

public final class Backward extends Command {
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
                    "Please select a source before rewinding.");
        } else {
            if (player.getCompilation() instanceof Podcast podcast) {
                podcast.backward(player);
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "Rewound successfully.");
            } else {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "The loaded source is not a podcast.");
            }
        }
    }
}
