package commands.admin;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import environment.Library;
import environment.party.artist.Artist;
import environment.party.host.Host;
import environment.party.user.User;
import lombok.Setter;

@Setter
public final class AddUser extends Command {
    private String type;
    private int age;
    private String city;

    @Override
    public Output execute(final Library library) {
        boolean success = false;

        switch (type) {
            case "user":
                User user = new User(getUsername(), age, city);
                success = user.add(library);
                break;
            case "artist":
                Artist artist = new Artist(getUsername(), age, city);
                success = artist.add(library);
                break;
            case "host":
                Host host = new Host(getUsername(), age, city);
                success = host.add(library);
                break;
            default:
                break;
        }

        return success
                ? new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                "The username " + getUsername() + " has been added successfully.")
                : new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                "The username " + getUsername() + " is already taken.");
    }
}
