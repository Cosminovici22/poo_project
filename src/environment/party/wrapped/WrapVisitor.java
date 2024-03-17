package environment.party.wrapped;

import environment.party.artist.Artist;
import environment.party.host.Host;
import environment.party.user.User;

import java.util.Map;

public interface WrapVisitor {
    /**
     * Visit the given user and determine its wrapped page
     * @param user on whom the wrapped command is called
     * @returna map containing all the elements of the wrapped command for the given user
     */
    Map<String, ?> visit(User user);

    /**
     * Visit the given artist and determine its wrapped page
     * @param artist on whom the wrapped command is called
     * @returna map containing all the elements of the wrapped command for the given artist
     */
    Map<String, ?> visit(Artist artist);

    /**
     * Visit the given host and determine its wrapped page
     * @param host on whom the wrapped command is called
     * @returna map containing all the elements of the wrapped command for the given host
     */
    Map<String, ?> visit(Host host);

}
