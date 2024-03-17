package commands.party.user.merch;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import environment.Library;
import environment.party.artist.Artist;
import environment.party.artist.Merch;
import environment.party.user.User;
import lombok.Setter;

@Setter
public final class BuyMerch extends Command {
    private String name;

    @Override
    public Output execute(final Library library) {
        User currUser = library.getUser(getUsername());

        if (currUser == null) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    "The username " + getUsername() + " doesn't exist.");
        } else if (currUser.isOffline()) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    getUsername() + " is offline.");
        } else if (!(currUser.getSelectedParty() instanceof Artist)) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    "Cannot buy merch from this page.");
        } else {
            for (Artist artist : library.getArtists()) {
                for (Merch merch : artist.getMerchandise()) {
                    if (merch.getName().equals(name)) {
                        merch.buy(currUser, artist);
                        return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                                getUsername() + " has added new merch successfully.");
                    }
                }
            }
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    "The merch " + name + " doesn't exist.");
        }
    }
}
