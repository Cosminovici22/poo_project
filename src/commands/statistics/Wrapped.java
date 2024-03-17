package commands.statistics;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import commands.output.MapOutput;
import environment.Library;
import environment.party.Party;
import environment.party.user.Player;
import environment.party.user.User;
import environment.party.wrapped.WrapVisitable;
import environment.party.wrapped.WrapVisitor;
import environment.party.wrapped.Wrap;
import lombok.Setter;

import java.util.Map;

@Setter
public final class Wrapped extends Command {
    @Override
    public Output execute(final Library library) {
        Party currParty = library.getUser(getUsername());
        if (currParty == null) {
            currParty = library.getArtist(getUsername());
        }
        if (currParty == null) {
            currParty = library.getHost(getUsername());
        }

        for (User user : library.getUsers()) {
            Player player = user.getPlayer();
            player.update(user, getTimestamp());
        }

        if (currParty == null) {
            return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                    "No data to show for user " + getUsername() + ".");
        } else {
            WrapVisitor visitor = new Wrap();
            Map<String, ?> result = ((WrapVisitable) currParty).accept(visitor);
            if (result.isEmpty()) {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "No data to show for " + currParty.getType() + " " + getUsername() + ".");
            } else {
                return new MapOutput(getCommand(), getTimestamp(), getUsername(), result);
            }
        }
    }
}
