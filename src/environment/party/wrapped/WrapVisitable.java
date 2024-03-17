package environment.party.wrapped;

import java.util.Map;

public interface WrapVisitable {
    /**
     * Call the given visitor on this visitable object
     * @param visitor to call
     * @return a map containing all the elements of the wrapped command
     */
    Map<String, ?> accept(WrapVisitor visitor);
}
