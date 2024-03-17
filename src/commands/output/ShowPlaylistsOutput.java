package commands.output;

import source.compilation.Playlist;
import lombok.Getter;
import source.track.Track;

import java.util.ArrayList;

@Getter
public final class ShowPlaylistsOutput extends UserOutput {
    private final ArrayList<DummyPlaylist> result;

    public ShowPlaylistsOutput(final String command, final String user, final Integer timestamp,
                               final ArrayList<Playlist> playlists) {
        super(command, timestamp, user);
        result = new ArrayList<>();
        for (Playlist playlist : playlists) {
            result.add(new DummyPlaylist(playlist));
        }
    }

    @Getter
    class DummyPlaylist {
        private final String name;
        private final ArrayList<String> songs;
        private final String visibility;
        private final int followers;

        DummyPlaylist(final Playlist playlist) {
            name = playlist.getName();
            songs = new ArrayList<>();
            for (Track track : playlist.getTracks()) {
                songs.add(track.getName());
            }
            visibility = playlist.isVisible() ? "public" : "private";
            followers = playlist.getFollowerCount();
        }
    }
}
