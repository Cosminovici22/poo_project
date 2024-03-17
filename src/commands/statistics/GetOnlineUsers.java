package commands.statistics;

import commands.Command;
import commands.output.Output;
import commands.output.ResultOutput;
import environment.Library;
import environment.party.user.User;

import java.util.ArrayList;

public final class GetOnlineUsers extends Command {
    @Override
    public Output execute(final Library library) {
        ArrayList<String> result = new ArrayList<>();

        for (User user : library.getUsers()) {
            if (!user.isOffline()) {
                result.add(user.getName());
            }
        }

        return new ResultOutput(getCommand(), getTimestamp(), result);
    }
}
