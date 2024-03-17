package commands.party.user.page;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import environment.Library;
import environment.party.user.Player;
import environment.party.user.User;

public final class LoadRecommendations extends Command {
    @Override
    public Output execute(final Library library) {
        User currUser = library.getUser(getUsername());

        if (currUser.isOffline()) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    getUsername() + " is offline.");
        } else {
            Player player = currUser.getPlayer();
            player.update(currUser, getTimestamp());
            if (player.getTrack() != null) {
                player.getTrack().unload(currUser, getTimestamp());
            }
            if (currUser.getLatestRecommendation() == null) {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "No recommendations available.");
            } else {
                currUser.getLatestRecommendation().load(currUser, getTimestamp());
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "Playback loaded successfully.");
            }
        }
    }
}
