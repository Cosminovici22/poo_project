package commands.statistics;

import commands.Command;
import commands.output.Output;
import commands.output.ResultOutput;
import environment.Library;
import environment.party.artist.Artist;

import java.util.ArrayList;

public final class GetTop5Artists extends Command {
    @Override
    public Output execute(final Library library) {
        ArrayList<Artist> artists = new ArrayList<>(library.getArtists());
        artists.sort((artist1, artist2) -> artist2.getLikeCount() - artist1.getLikeCount());

        final int maxResultSize = 5;
        ArrayList<String> result = new ArrayList<>();
        for (Artist artist : artists) {
            if (result.size() == maxResultSize) {
                break;
            }
            result.add(artist.getName());
        }

        return new ResultOutput(getCommand(), getTimestamp(), result);
    }
}
