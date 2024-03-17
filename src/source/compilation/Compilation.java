package source.compilation;

import lombok.Getter;
import lombok.Setter;
import source.Source;
import source.track.Track;

import java.util.ArrayList;

@Getter @Setter
public abstract class Compilation extends Source {
    private ArrayList<Track> tracks;

    public Compilation(final String name, final ArrayList<Track> tracks) {
        super(name);
        this.tracks = tracks;
    }

    /**
     * Checks if a song is in this collection
     * @param track to be looked for in the collection
     * @return true if the track is in the collection or false otherwise
     */
    public boolean contains(final Track track) {
        for (Track currTrack : tracks) {
            if (currTrack == track) {
                return true;
            }
        }
        return false;
    }
}
