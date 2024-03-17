package commands.admin;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import environment.Library;
import environment.party.Party;
import lombok.Setter;

@Setter
public final class DeleteUser extends Command {
    @Override
    public Output execute(final Library library) {
        Party currParty = library.getUser(getUsername());
        if (currParty == null) {
            currParty = library.getArtist(getUsername());
        }
        if (currParty == null) {
            currParty = library.getHost(getUsername());
        }

        if (currParty == null) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    "The username " + getUsername() + " doesn't exist.");
        } else {
            if (currParty.delete(library, getTimestamp())) {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        getUsername() + " was successfully deleted.");
            } else {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        getUsername() + " can't be deleted.");
            }
        }
    }
}
