package commands.party.user.playlist;

import commands.Command;
import commands.output.Output;
import environment.Library;
import lombok.Setter;
import environment.party.user.User;
import commands.output.MessageOutput;

@Setter
public final class SwitchVisibility extends Command {
    private int playlistId;

    @Override
    public Output execute(final Library library) {
        User currUser = library.getUser(getUsername());

        if (currUser.isOffline()) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    getUsername() + " is offline.");
        }

        if (playlistId > currUser.getPlaylists().size()) {
            return new MessageOutput(this.getCommand(), this.getTimestamp(), this.getUsername(),
                    "The specified playlist ID is too high.");
        } else {
            if (currUser.getPlaylists().get(playlistId - 1).switchVisibility()) {
                return new MessageOutput(this.getCommand(), this.getTimestamp(), this.getUsername(),
                        "Visibility status updated successfully to public.");
            } else {
                return new MessageOutput(this.getCommand(), this.getTimestamp(), this.getUsername(),
                        "Visibility status updated successfully to private.");
            }
        }
    }
}
