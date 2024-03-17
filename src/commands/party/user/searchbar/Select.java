package commands.party.user.searchbar;

import commands.Command;
import commands.output.Output;
import environment.Library;
import environment.party.Party;
import environment.party.user.User;
import lombok.Setter;
import commands.output.MessageOutput;
import source.Source;

import java.util.ArrayList;

@Setter
public final class Select extends Command {
    private int itemNumber;

    @Override
    public Output execute(final Library library) {
        User currUser = library.getUser(getUsername());
        ArrayList<Source> sources = currUser.getSearchedSources();
        ArrayList<Party> parties = currUser.getSearchedParties();

        if (currUser.isOffline()) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    getUsername() + " is offline.");
        }

        if (sources == null && parties == null) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    "Please conduct a search before making a selection.");
        } else {
            if (parties == null && itemNumber > sources.size()
                    || sources == null && itemNumber > parties.size()) {
                currUser.setSelectedParty(null);
                currUser.setSelectedSource(null);
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "The selected ID is too high.");
            } else if (sources != null) {
                currUser.selectSource(sources.get(itemNumber - 1));
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "Successfully selected " + currUser.getSelectedSource().getName() + ".");
            } else {
                currUser.selectParty(parties.get(itemNumber - 1));
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "Successfully selected " + currUser.getSelectedParty().getName()
                                + "'s page.");
            }
        }
    }
}
