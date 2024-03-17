package commands.admin;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import environment.Library;
import environment.party.user.Player;
import environment.party.user.User;
import lombok.Setter;

@Setter
public final class AdBreak extends Command {
    private int price;

    @Override
    public Output execute(final Library library) {
        User currUser = library.getUser(getUsername());

        if (currUser == null) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    "The username " + getUsername() + " doesn't exist.");
        } else if (currUser.isOffline()) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    getUsername() + " is offline.");
        } else {
            Player player = currUser.getPlayer();
            player.update(currUser, getTimestamp());

            if (player.getTrack() == null) {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        getUsername() + " is not playing any music.");
            } else {
                player.setAd(library.getSongs().get(0));
                player.getAd().setPrice(price);
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "Ad inserted successfully.");
            }
        }
    }
}
