package commands.party.user.merch;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import commands.output.SeeMerchOutput;
import environment.Library;
import environment.party.artist.Merch;
import environment.party.user.User;
import lombok.Setter;

import java.util.ArrayList;

@Setter
public final class SeeMerch extends Command {
    @Override
    public Output execute(final Library library) {
        User currUser = library.getUser(getUsername());

        if (currUser == null) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    "The username " + getUsername() + " doesn't exist.");
        } else if (currUser.isOffline()) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    getUsername() + " is offline.");
        } else {
            ArrayList<String> result = new ArrayList<>();
            for (Merch merch : currUser.getOwnedMerch()) {
                result.add(merch.getName());
            }
            return new SeeMerchOutput(getCommand(), getTimestamp(), getUsername(), result);
        }
    }
}
