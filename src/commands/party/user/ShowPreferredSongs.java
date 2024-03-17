package commands.party.user;

import commands.Command;
import commands.output.Output;
import environment.Library;
import environment.party.user.User;
import commands.output.ShowPreferredSongsOutput;
import lombok.Setter;
import source.track.Song;

import java.util.ArrayList;

@Setter
public final class ShowPreferredSongs extends Command {
    @Override
    public Output execute(final Library library) {
        User currUser = library.getUser(getUsername());

        ArrayList<String> result = new ArrayList<>();
        for (Song song : currUser.getLikedSongs()) {
            result.add(song.getName());
        }

        return new ShowPreferredSongsOutput(getCommand(), getTimestamp(), getUsername(), result);
    }
}
