package commands.party.user.player;

import commands.Command;
import commands.output.Output;
import environment.Library;
import environment.party.user.Player;
import environment.party.user.User;
import commands.output.MessageOutput;
import source.compilation.Playlist;

public final class Load extends Command {
    @Override
    public Output execute(final Library library) {
        User currUser = library.getUser(getUsername());

        if (currUser.isOffline()) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    getUsername() + " is offline.");
        }

        if (currUser.getSelectedSource() == null) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    "Please select a source before attempting to load.");
        } else {
            if (currUser.getSelectedSource() instanceof Playlist playlist
                    && playlist.getTracks().isEmpty()) {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "You can't load an empty audio collection!");
            } else {
                Player player = currUser.getPlayer();
                player.update(currUser, getTimestamp());
                if (player.getTrack() != null) {
                    player.getTrack().unload(currUser, getTimestamp());
                }
                currUser.getSelectedSource().load(currUser, getTimestamp());
                currUser.setSelectedSource(null);
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "Playback loaded successfully.");
            }
        }
    }
}
