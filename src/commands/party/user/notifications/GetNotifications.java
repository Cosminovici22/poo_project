package commands.party.user.notifications;

import commands.Command;
import commands.output.GetNotificationsOutput;
import commands.output.MessageOutput;
import commands.output.Output;
import environment.Library;
import environment.party.user.User;

public final class GetNotifications extends Command {
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
            return new GetNotificationsOutput(getCommand(), getTimestamp(), getUsername(),
                    currUser.getNotifications());
        }
    }
}
