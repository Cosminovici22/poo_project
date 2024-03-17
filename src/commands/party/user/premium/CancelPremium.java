package commands.party.user.premium;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import environment.Library;
import environment.party.artist.Artist;
import environment.party.user.User;

public final class CancelPremium extends Command {
    @Override
    public Output execute(final Library library) {
        User currUser = library.getUser(getUsername());

        if (currUser == null) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    "The username " + getUsername() + " doesn't exist.");
        } else if (currUser.isOffline()) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    getUsername() + " is offline.");
        } else if (currUser.setPremium(false)) {
            final double premiumPrice = 1000000;
            for (Artist artist : library.getArtists()) {
                artist.addSongRevenue(currUser.getGeneratedRevenueFor(artist, premiumPrice));
            }
            currUser.getSongHistory().clear();
            currUser.setSongHistory(currUser.getFreeSongHistory());
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    getUsername() + " cancelled the subscription successfully.");
        } else {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    getUsername() + " is not a premium user.");
        }
    }
}
