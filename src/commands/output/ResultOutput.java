package commands.output;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class ResultOutput extends Output {
    private ArrayList<String> result;

    public ResultOutput(final String command, final Integer timestamp,
                        final ArrayList<String> result) {
        super(command, timestamp);
        this.result = result;
    }
}
