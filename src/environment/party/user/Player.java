package environment.party.user;

import lombok.Getter;
import lombok.Setter;
import source.compilation.Compilation;
import source.track.Episode;
import source.track.Song;
import source.track.Track;

import java.util.ArrayList;
import java.util.HashMap;

@Getter @Setter
public final class Player {
    private Compilation compilation;
    private Track track;
    private int loadTimestamp;
    private int elapsedTime;
    private int repeats;
    private boolean paused;
    private HashMap<Episode, Integer> episodeElapsedTimes;
    private ArrayList<Song> shuffledPlaylist;
    private Song ad;

    public Player() {
        compilation = null;
        track = null;
        loadTimestamp = 0;
        elapsedTime = 0;
        repeats = 0;
        paused = true;
        episodeElapsedTimes = new HashMap<>();
        shuffledPlaylist = new ArrayList<>();
        ad = null;
    }

    /**
     * Cycle through the repeat states of this player
     */
    public void cycleRepeats() {
        repeats++;
        if (repeats > 2) {
            repeats = 0;
        }
    }

    /**
     * Play/pause the loaded track in this player
     * @param timestamp at which the play/pause occurred
     */
    public void playPause(final Integer timestamp) {
        if (paused) {
            loadTimestamp = timestamp - elapsedTime;
        }
        paused = !paused;
    }

    /**
     * Fetch the status of this player at a timestamp
     * @param user for whose player the update is realised
     * @param timestamp at which the status of this player is desired
     */
    public void update(final User user, final Integer timestamp) {
        if (track == null || paused || user.isOffline()) {
            return;
        }
        while (track != null && timestamp - loadTimestamp >= track.getDuration()) {
            if (compilation != null) {
                compilation.next(user, loadTimestamp + track.getDuration());
            } else {
                track.next(user, loadTimestamp + track.getDuration());
            }
        }
        if (track != null) {
            elapsedTime = timestamp - loadTimestamp;
        }
    }
}
