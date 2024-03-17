package commands.output;

import lombok.Getter;

@Getter
public abstract class Output {
    private final String command;
    private final Integer timestamp;

    public Output(final String command, final Integer timestamp) {
        this.command = command;
        this.timestamp = timestamp;
    }
}
