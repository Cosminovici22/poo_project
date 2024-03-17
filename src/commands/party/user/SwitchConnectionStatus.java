package commands.party.user;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import environment.Library;
import environment.party.user.Player;
import environment.party.user.User;
import lombok.Setter;

@Setter
public final class SwitchConnectionStatus extends Command {
    @Override
    public Output execute(final Library library) {
        User currUser = library.getUser(getUsername());

        if (currUser == null) {
            if (library.getArtist(getUsername()) != null
                    || library.getHost(getUsername()) != null) {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        getUsername() + " is not a normal user.");
            } else {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "The username " + getUsername() + " doesn't exist.");
            }
        } else {
            Player player = currUser.getPlayer();
            player.update(currUser, getTimestamp());
            currUser.switchStatus(getTimestamp());
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    getUsername() + " has changed status successfully.");
        }
    }
}
