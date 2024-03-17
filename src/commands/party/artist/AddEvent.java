package commands.party.artist;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import environment.Library;
import environment.party.artist.Artist;
import lombok.Setter;
import environment.party.artist.Event;

@Setter
public final class AddEvent extends Command {
    private String name;
    private String description;
    private String date;

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
            final int february = 2;
            final int februaryDayCount = 28;
            final int monthDayCount = 31;
            final int monthCount = 12;
            final int minYear = 1900;
            final int maxYear = 2023;

            String[] components = date.split("-");
            int day = Integer.parseInt(components[0]);
            int month = Integer.parseInt(components[1]);
            int year = Integer.parseInt(components[2]);
            if (month == february && day > februaryDayCount || day > monthDayCount
                    || month > monthCount || year < minYear || year > maxYear) {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "Event for " + getUsername() + " does not have a valid date.");
            } else {
                Event event = new Event(name, description, date);
                if (currArtist.addEvent(event)) {
                    return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                            getUsername() + " has added new event successfully.");
                } else {
                    return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                            getUsername() + " has another event with the same name.");
                }
            }
        }
    }
}
