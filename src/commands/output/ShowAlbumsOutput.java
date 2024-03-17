package commands.output;

import source.compilation.Album;
import lombok.Getter;
import source.track.Track;

import java.util.ArrayList;

@Getter
public final class ShowAlbumsOutput extends UserOutput {
    private ArrayList<DummyAlbum> result;

    public ShowAlbumsOutput(final String command, final Integer timestamp, final String user,
                            final ArrayList<Album> albums) {
        super(command, timestamp, user);
        result = new ArrayList<>();
        for (Album album : albums) {
            result.add(new DummyAlbum(album));
        }
    }

    @Getter
    class DummyAlbum {
        private String name;
        private ArrayList<String> songs;

        DummyAlbum(final Album album) {
            name = album.getName();
            songs = new ArrayList<>();
            for (Track track : album.getTracks()) {
                songs.add(track.getName());
            }
        }
    }
}
