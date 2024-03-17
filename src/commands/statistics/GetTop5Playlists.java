package commands.statistics;

import commands.Command;
import commands.output.Output;
import commands.output.ResultOutput;
import environment.Library;
import source.compilation.Playlist;

import java.util.ArrayList;

public final class GetTop5Playlists extends Command {
    @Override
    public Output execute(final Library library) {
        ArrayList<Playlist> playlists = new ArrayList<>(library.getPlaylists());
        playlists.sort((list1, list2) -> list2.getFollowerCount() - list1.getFollowerCount());

        final int maxResultSize = 5;
        ArrayList<String> result = new ArrayList<>();
        for (Playlist playlist : playlists) {
            if (result.size() == maxResultSize) {
                break;
            }
            result.add(playlist.getName());
        }

        return new ResultOutput(getCommand(), getTimestamp(), result);
    }
}
