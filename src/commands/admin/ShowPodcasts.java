package commands.admin;

import commands.Command;
import commands.output.Output;
import commands.output.ShowPodcastsOutput;
import environment.Library;
import environment.party.host.Host;
import lombok.Setter;

@Setter
public final class ShowPodcasts extends Command {
    private int playlistId;

    @Override
    public Output execute(final Library library) {
        Host host = library.getHost(getUsername());
        return new ShowPodcastsOutput(getCommand(), getTimestamp(), getUsername(),
                host.getPodcasts());
    }
}
