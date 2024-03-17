package commands.statistics;

import commands.Command;
import commands.output.Output;
import commands.output.ResultOutput;
import environment.Library;
import environment.party.artist.Artist;
import environment.party.host.Host;
import environment.party.user.User;

import java.util.ArrayList;

public final class GetAllUsers extends Command {
    @Override
    public Output execute(final Library library) {
        ArrayList<String> result = new ArrayList<>();

        for (User user : library.getUsers()) {
            result.add(user.getName());
        }
        for (Artist artist : library.getArtists()) {
            result.add(artist.getName());
        }
        for (Host host : library.getHosts()) {
            result.add(host.getName());
        }

        return new ResultOutput(getCommand(), getTimestamp(), result);
    }
}
