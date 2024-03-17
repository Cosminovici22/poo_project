package commands.party.host;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import environment.Library;
import environment.party.host.Host;
import lombok.Setter;
import source.compilation.Podcast;
import source.track.Episode;

import java.util.ArrayList;

@Setter
public final class AddPodcast extends Command {
    private String name;
    private ArrayList<Episode> episodes;

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
            if (currHost.getPodcast(name) != null) {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        getUsername() + " has another podcast with the same name.");
            } else {
                Podcast podcast = new Podcast(name, getUsername(), (ArrayList) episodes);
                if (currHost.addPodcast(library, podcast)) {
                    return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                            getUsername() + " has added new podcast successfully.");
                } else {
                    return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                            getUsername() + " has the same episode in this podcast.");
                }
            }
        }
    }
}
