package commands.party.user.premium;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import environment.Library;
import environment.party.user.User;

public final class BuyPremium extends Command {
    @Override
    public Output execute(final Library library) {
        User currUser = library.getUser(getUsername());

        if (currUser == null) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    "The username " + getUsername() + " doesn't exist.");
        } else if (currUser.isOffline()) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    getUsername() + " is offline.");
        } else if (currUser.setPremium(true)) {
            currUser.setSongHistory(currUser.getPremiumSongHistory());
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    getUsername() + " bought the subscription successfully.");
        } else {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    getUsername() + " is already a premium user.");
        }
    }
}
