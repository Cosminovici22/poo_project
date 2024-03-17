package commands.party.user.playlist;

import commands.Command;
import commands.output.Output;
import environment.Library;
import environment.party.user.User;
import lombok.Setter;
import commands.output.MessageOutput;

@Setter
public final class CreatePlaylist extends Command {
    private String playlistName;

    @Override
    public Output execute(final Library library) {
        User currUser = library.getUser(getUsername());

        if (currUser.isOffline()) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    getUsername() + " is offline.");
        }

        if (currUser.createPlaylist(library, playlistName)) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    "Playlist created successfully.");
        } else {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    "A playlist with the same name already exists.");
        }
    }
}
