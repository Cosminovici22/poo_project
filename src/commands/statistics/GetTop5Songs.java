package commands.statistics;

import commands.Command;
import commands.output.Output;
import commands.output.ResultOutput;
import environment.Library;
import source.track.Song;

import java.util.ArrayList;

public final class GetTop5Songs extends Command {
    @Override
    public Output execute(final Library library) {
        ArrayList<Song> songs = new ArrayList<>(library.getSongs());
        songs.sort((song1, song2) -> song2.getLikeCount() - song1.getLikeCount());

        final int maxResultSize = 5;
        ArrayList<String> result = new ArrayList<>();
        for (Song song : songs) {
            if (result.size() == maxResultSize) {
                break;
            }
            result.add(song.getName());
        }

        return new ResultOutput(getCommand(), getTimestamp(), result);
    }
}
