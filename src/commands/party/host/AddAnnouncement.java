package commands.party.host;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import environment.Library;
import environment.party.host.Host;
import lombok.Setter;
import environment.party.host.Announcement;

@Setter
public final class AddAnnouncement extends Command {
    private String name;
    private String description;

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
            Announcement announcement = new Announcement(name, description);
            if (currHost.addAnnouncement(announcement)) {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        getUsername() + " has successfully added new announcement.");
            } else {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        getUsername() + " has already added an announcement with this name.");
            }
        }
    }
}
