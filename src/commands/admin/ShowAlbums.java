package commands.admin;

import commands.Command;
import commands.output.Output;
import commands.output.ShowAlbumsOutput;
import environment.Library;
import environment.party.artist.Artist;
import lombok.Setter;

@Setter
public final class ShowAlbums extends Command {
    @Override
    public Output execute(final Library library) {
        Artist artist = library.getArtist(getUsername());
        return new ShowAlbumsOutput(getCommand(), getTimestamp(), getUsername(),
                artist.getAlbums());
    }
}
