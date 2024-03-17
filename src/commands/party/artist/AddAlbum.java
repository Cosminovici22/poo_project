package commands.party.artist;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import environment.Library;
import environment.party.artist.Artist;
import lombok.Setter;
import source.compilation.Album;
import source.track.Song;

import java.util.ArrayList;

@Setter
public final class AddAlbum extends Command {
    private String name;
    private int releaseYear;
    private String description;
    private ArrayList<Song> songs;

    @Override
    public Output execute(final Library library) {
        Artist currArtist = library.getArtist(getUsername());

        if (currArtist == null) {
            if (library.getUser(getUsername()) != null
                    || library.getHost(getUsername()) != null) {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        getUsername() + " is not an artist.");
            } else {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "The username " + getUsername() + " doesn't exist.");
            }
        } else {
            if (currArtist.getAlbum(name) != null) {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        getUsername() + " has another album with the same name.");
            } else {
                Album album = new Album(name, getUsername(), releaseYear, description,
                        (ArrayList) songs);
                if (currArtist.addAlbum(library, album)) {
                    return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                            getUsername() + " has added new album successfully.");
                } else {
                    return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                            getUsername() + " has the same song at least twice in this album.");
                }
            }
        }
    }
}
