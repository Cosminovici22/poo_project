package commands.party.host;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import environment.Library;
import environment.party.host.Host;
import lombok.Setter;
import source.compilation.Podcast;

@Setter
public final class RemovePodcast extends Command {
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
            Podcast podcast = currHost.getPodcast(name);
            if (podcast == null) {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        getUsername() + " doesn't have a podcast with the given name.");
            } else {
                if (currHost.removePodcast(library, podcast)) {
                    return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                            getUsername() + " deleted the podcast successfully.");
                } else {
                    return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                            getUsername() + " can't delete this podcast.");
                }
            }
        }
    }
}
