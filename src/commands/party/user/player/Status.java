package commands.party.user.player;

import commands.Command;
import commands.output.Output;
import environment.Library;
import environment.party.user.Player;
import environment.party.user.User;
import commands.output.StatusOutput;

public final class Status extends Command {
    @Override
    public Output execute(final Library library) {
        User currUser = library.getUser(getUsername());
        Player player = currUser.getPlayer();
        player.update(currUser, getTimestamp());
        return new StatusOutput(getCommand(), getUsername(), getTimestamp(), player);
    }
}
