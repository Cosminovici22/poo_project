package commands.output;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class SeeMerchOutput extends UserOutput {
    private ArrayList<String> result;

    public SeeMerchOutput(final String command, final Integer timestamp, final String user,
                          final ArrayList<String> result) {
        super(command, timestamp, user);
        this.result = result;
    }
}
