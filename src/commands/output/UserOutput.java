package commands.output;

import lombok.Getter;

@Getter
public class UserOutput extends Output {
    private final String user;

    public UserOutput(final String command, final Integer timestamp, final String user) {
        super(command, timestamp);
        this.user = user;
    }
}
