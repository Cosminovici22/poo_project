package source.track;

import lombok.Getter;
import lombok.Setter;
import source.Source;

import java.util.HashMap;

@Getter @Setter
public abstract class Track extends Source {
    private Integer duration;
    private HashMap<String, Integer> listeners;

    public Track() {
        this.listeners = new HashMap<>();
    }

    public Track(final String name, final int duration) {
        super(name);
        this.duration = duration;
        this.listeners = new HashMap<>();
    }

    protected final void addListener(final String username) {
        Integer listens = listeners.putIfAbsent(username, 1);
        if (listens != null) {
            listeners.put(username, listens + 1);
        }
    }
}
