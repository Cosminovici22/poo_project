package commands.party.artist;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import environment.Library;
import environment.party.artist.Artist;
import lombok.Setter;
import environment.party.artist.Merch;

@Setter
public final class AddMerch extends Command {
    private String name;
    private String description;
    private int price;

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
            if (price < 0) {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "Price for merchandise can not be negative.");
            } else {
                Merch merch = new Merch(name, description, price);
                if (currArtist.addMerch(merch)) {
                    return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                            getUsername() + " has added new merchandise successfully.");
                } else {
                    return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                            getUsername() + " has merchandise with the same name.");
                }
            }
        }
    }
}
