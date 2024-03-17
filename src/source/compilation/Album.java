package source.compilation;

import lombok.Getter;
import lombok.Setter;
import source.track.Track;

import java.util.ArrayList;

@Getter @Setter
public final class Album extends Playlist {
    private int releaseYear;
    private String description;

    public Album(final String name, final String owner, final int releaseYear,
                 final String description, final ArrayList<Track> songs) {
        super(name, owner, songs);
        this.releaseYear = releaseYear;
        this.description = description;
    }
}
