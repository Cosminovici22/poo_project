package commands.output;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class SearchOutput extends MessageOutput {
    private ArrayList<String> results;

    public SearchOutput(final String command, final String user, final Integer timestamp,
                        final String message, final ArrayList<String> results) {
        super(command, timestamp, user, message);
        this.results = results;
    }
}
