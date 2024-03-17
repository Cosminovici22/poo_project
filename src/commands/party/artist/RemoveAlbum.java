package commands.party.artist;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import environment.Library;
import environment.party.artist.Artist;
import lombok.Setter;
import source.compilation.Album;

@Setter
public final class RemoveAlbum extends Command {
    private String name;

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
            Album album = currArtist.getAlbum(name);
            if (album == null) {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        getUsername() + " doesn't have an album with the given name.");
            } else {
                if (currArtist.removeAlbum(library, album)) {
                    return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                            getUsername() + " deleted the album successfully.");
                } else {
                    return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                            getUsername() + " can't delete this album.");
                }
            }
        }
    }
}
