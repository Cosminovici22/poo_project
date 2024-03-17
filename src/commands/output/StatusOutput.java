package commands.output;

import environment.party.user.Player;
import lombok.Getter;
import source.compilation.Playlist;

@Getter
public final class StatusOutput extends UserOutput {
    private final Stats stats;

    public StatusOutput(final String command, final String user, final Integer timestamp,
                        final Player player) {
        super(command, timestamp, user);
        if (player.getTrack() == null) {
            stats = new Stats("", 0, "No Repeat", false, player.isPaused());
        } else if (player.getCompilation() instanceof Playlist) {
            stats = new Stats(
                    player.getTrack().getName(),
                    player.getTrack().getDuration() - player.getElapsedTime(),
                    player.getRepeats() == 0
                            ? "No Repeat"
                            : player.getRepeats() == 1
                            ? "Repeat All"
                            : "Repeat Current Song",
                    !player.getShuffledPlaylist().isEmpty(),
                    player.isPaused()
            );
        } else {
            stats = new Stats(
                    player.getTrack().getName(),
                    player.getTrack().getDuration() - player.getElapsedTime(),
                    player.getRepeats() == 0
                            ? "No Repeat"
                            : player.getRepeats() == 1
                            ? "Repeat Once"
                            : "Repeat Infinite",
                    !player.getShuffledPlaylist().isEmpty(),
                    player.isPaused()
            );
        }
    }

    @Getter
    class Stats {
        private final String name;
        private final Integer remainedTime;
        private final String repeat;
        private final boolean shuffle;
        private final boolean paused;

        Stats(final String name, final Integer remainedTime, final String repeat,
              final boolean shuffle, final boolean paused) {
            this.name = name;
            this.remainedTime = remainedTime;
            this.repeat = repeat;
            this.shuffle = shuffle;
            this.paused = paused;
        }
    }
}
