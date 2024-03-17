package commands.statistics;

import commands.Command;
import commands.output.Output;
import commands.output.ResultOutput;
import environment.Library;
import source.compilation.Album;

import java.util.ArrayList;

public final class GetTop5Albums extends Command {
    @Override
    public Output execute(final Library library) {
        ArrayList<Album> albums = new ArrayList<>(library.getAlbums());
        albums.sort((album1, album2) -> album2.getLikeCount() - album1.getLikeCount() != 0
                ? album2.getLikeCount() - album1.getLikeCount()
                : album1.getName().compareTo(album2.getName())
        );

        final int maxResultSize = 5;
        ArrayList<String> result = new ArrayList<>();
        for (Album album : albums) {
            if (result.size() == maxResultSize) {
                break;
            }
            result.add(album.getName());
        }

        return new ResultOutput(getCommand(), getTimestamp(), result);
    }
}
