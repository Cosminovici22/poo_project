package commands.party.user.page;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import environment.Library;
import environment.page.HomePageGenerator;
import environment.page.LikedContentPageGenerator;
import environment.party.artist.Artist;
import environment.party.host.Host;
import environment.party.user.Player;
import environment.party.user.User;
import lombok.Setter;
import source.compilation.Podcast;
import source.track.Song;

@Setter
public final class ChangePage extends Command {
    private String nextPage;

    @Override
    public Output execute(final Library library) {
        User currUser = library.getUser(getUsername());
        Player player = currUser.getPlayer();
        player.update(currUser, getTimestamp());

        if (currUser.isOffline()) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    getUsername() + " is offline.");
        }

        currUser.getNextPages().clear();
        switch (nextPage) {
            case "Home":
                currUser.changePage(new HomePageGenerator(currUser));
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        getUsername() + " accessed Home successfully.");
            case "LikedContent":
                currUser.changePage(new LikedContentPageGenerator(currUser));
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        getUsername() + " accessed LikedContent successfully.");
            case "Artist":
                Artist artist = library.getArtist(((Song) player.getTrack()).getArtist());
                currUser.changePage(artist.getPageGenerator());
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        getUsername() + " accessed Artist successfully.");
            case "Host":
                Host host = library.getHost(((Podcast) player.getCompilation()).getOwner());
                currUser.changePage(host.getPageGenerator());
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        getUsername() + " accessed Host successfully.");
            default:
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        getUsername() + " is trying to access a non-existent page.");
        }
    }
}
