package commands.output;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class ShowPreferredSongsOutput extends UserOutput {
    private ArrayList<String> result;

    public ShowPreferredSongsOutput(final String command, final Integer timestamp,
                                    final String user, final ArrayList<String> result) {
        super(command, timestamp, user);
        this.result = result;
    }
}
