package commands.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.Map;

@Getter @JsonInclude(JsonInclude.Include.NON_NULL)
public final class MapOutput extends UserOutput {
    private Map<String, ?> result;

    public MapOutput(final String command, final Integer timestamp, final String user,
                     final Map<String, ?> result) {
        super(command, timestamp, user);
        this.result = result;
    }
}
