package commands.party.user.page;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import environment.Library;
import environment.party.user.User;

public final class NextPage extends Command {
    @Override
    public Output execute(final Library library) {
        User currUser = library.getUser(getUsername());

        if (currUser.isOffline()) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    getUsername() + " is offline.");
        } else if (currUser.nextPage()) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    "The user " + getUsername() + " has navigated successfully to the next page.");
        } else {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    "There are no pages left to go forward.");
        }
    }
}
