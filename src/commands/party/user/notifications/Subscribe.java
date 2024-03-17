package commands.party.user.notifications;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import environment.Library;
import environment.party.Party;
import environment.party.observer.SubjectParty;
import environment.party.user.User;

public final class Subscribe extends Command {
    @Override
    public Output execute(final Library library) {
        User currUser = library.getUser(getUsername());

        if (currUser == null) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    "The username " + getUsername() + " doesn't exist.");
        } else if (currUser.isOffline()) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    getUsername() + " is offline.");
        } else if (currUser.getSelectedParty() == null) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    "To subscribe you need to be on the page of an artist or host.");
        } else {
            Party party = currUser.getSelectedParty();

            if (((SubjectParty) party).addRemoveSubscriber(currUser)) {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        getUsername() + " subscribed to " + party.getName() + " successfully.");
            } else {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        getUsername() + " unsubscribed from " + party.getName()
                                + " successfully.");
            }
        }
    }
}
