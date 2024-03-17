package commands.party.user.page;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import environment.Library;
import environment.party.user.User;

public final class PrintCurrentPage extends Command {
    @Override
    public Output execute(final Library library) {
        User currUser = library.getUser(getUsername());

        if (currUser.isOffline()) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    getUsername() + " is offline.");
        }

        return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                currUser.generateCurrentPage());
    }
}
