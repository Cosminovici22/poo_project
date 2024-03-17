package commands.party.host;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import environment.Library;
import environment.party.host.Host;
import lombok.Setter;

@Setter
public final class RemoveAnnouncement extends Command {
    private String name;

    @Override
    public Output execute(final Library library) {
        Host currHost = library.getHost(getUsername());

        if (currHost == null) {
            if (library.getUser(getUsername()) != null
                    || library.getArtist(getUsername()) != null) {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        getUsername() + " is not a host.");
            } else {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "The username " + getUsername() + " doesn't exist.");
            }
        } else {
            if (currHost.removeAnnouncement(name)) {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        getUsername() + " has successfully deleted the announcement.");
            } else {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        getUsername() + " has no announcement with the given name.");
            }
        }
    }
}
