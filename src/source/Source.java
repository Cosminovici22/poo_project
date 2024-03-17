package source;

import environment.party.user.User;
import lombok.Getter;

@Getter
public abstract class Source {
    private String name;

    public Source() {
    }

    public Source(final String name) {
        this.name = name;
    }

    /**
     * Unload this source from the given user's player
     * @param user whose player this source should be unloaded from
     * @param timestamp at which to unload the source
     */
    public abstract void unload(User user, Integer timestamp);

    /**
     * Load this source in the given user's player
     * @param user whose player this source should be loaded in
     * @param timestamp at which to load the source
     */
    public abstract void load(User user, Integer timestamp);

    /**
     * Load next source in the given user's player
     * @param user whose player the next source should be loaded in
     * @param timestamp at which to load the source
     */
    public abstract void next(User user, Integer timestamp);

    /**
     * Load previous source in the given user's player
     * @param user whose player the previous source should be loaded in
     * @param timestamp at which to load the source
     */
    public abstract void prev(User user, Integer timestamp);
}
