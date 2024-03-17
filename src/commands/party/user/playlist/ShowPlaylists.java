package commands.party.user.playlist;

import commands.Command;
import commands.output.Output;
import environment.Library;
import environment.party.user.User;
import commands.output.ShowPlaylistsOutput;

public final class ShowPlaylists extends Command {
    @Override
    public Output execute(final Library library) {
        User currUser = library.getUser(getUsername());
        return new ShowPlaylistsOutput(getCommand(), getUsername(), getTimestamp(),
                currUser.getPlaylists());
    }
}
