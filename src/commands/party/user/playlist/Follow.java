package commands.party.user.playlist;

import commands.Command;
import commands.output.Output;
import environment.Library;
import source.compilation.Playlist;
import environment.party.user.User;
import commands.output.MessageOutput;

public final class Follow extends Command {
    @Override
    public Output execute(final Library library) {
        User currUser = library.getUser(getUsername());

        if (currUser.isOffline()) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    getUsername() + " is offline.");
        }

        if (currUser.getSelectedSource() == null) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    "Please select a source before following or unfollowing.");
        } else {
            if (currUser.getSelectedSource() instanceof Playlist playlist) {
                if (currUser.getPlaylists().contains(playlist)) {
                    return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                            "You cannot follow or unfollow your own playlist.");
                } else if (playlist.follow(currUser)) {
                    return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                            "Playlist followed successfully.");
                } else {
                    return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                            "Playlist unfollowed successfully.");
                }
            } else {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "The selected source is not a playlist.");
            }
        }
    }
}
