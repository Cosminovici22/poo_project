package commands.output;

import lombok.Getter;

@Getter
public class MessageOutput extends UserOutput {
    private final String message;

    public MessageOutput(final String command, final Integer timestamp, final String user,
                         final String message) {
        super(command, timestamp, user);
        this.message = message;
    }
}
