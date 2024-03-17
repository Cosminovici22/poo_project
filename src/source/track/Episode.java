package source.track;

import environment.party.user.Player;
import environment.party.user.User;
import fileio.input.EpisodeInput;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class Episode extends Track {
    private String description;

    public Episode() {
    }

    public Episode(final EpisodeInput episodeInput) {
        super(episodeInput.getName(), episodeInput.getDuration());
        this.description = episodeInput.getDescription();
    }

    @Override
    public void unload(final User user, final Integer timestamp) {
        Player player = user.getPlayer();
        player.setTrack(null);
        player.setLoadTimestamp(0);
        player.setElapsedTime(0);
        player.setRepeats(0);
        player.setPaused(true);
    }

    @Override
    public void load(final User user, final Integer timestamp) {
        addListener(user.getName());
        Player player = user.getPlayer();
        player.setAd(null);
        player.setTrack(this);
        player.setLoadTimestamp(timestamp);
        player.setElapsedTime(0);
        player.setRepeats(0);
        player.setPaused(false);
    }

    @Override
    public void next(final User user, final Integer timestamp) {
    }

    @Override
    public void prev(final User user, final Integer timestamp) {
    }
}
