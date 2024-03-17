package source.compilation;

import environment.party.user.Player;
import environment.party.user.User;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import lombok.Getter;
import lombok.Setter;
import source.track.Episode;
import source.track.Song;
import source.track.Track;

import java.util.ArrayList;

@Getter @Setter
public final class Podcast extends Compilation {
    private String owner;

    public Podcast(final PodcastInput podcastInput) {
        super(podcastInput.getName(), new ArrayList<>());
        for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
            getTracks().add(new Episode(episodeInput));
        }
        this.owner = podcastInput.getOwner();
    }

    public Podcast(final String name, final String owner, final ArrayList<Track> episodes) {
        super(name, episodes);
        this.owner = owner;
    }

    /**
     * Skip the loaded episode in a player forward by 90 seconds.
     * Achieved by decreasing the loaded timestamp of the loaded episode.
     * @param player for which to skip forward 90 seconds
     */
    public void forward(final Player player) {
        final int skipTime = 90;
        int remaining = player.getTrack().getDuration() - player.getElapsedTime();
        player.setLoadTimestamp(player.getLoadTimestamp()
                - (remaining < skipTime ? remaining : skipTime));
    }

    /**
     * Rewind the loaded episode in a player by 90 seconds.
     * Achieved by increasing the loaded timestamp of the loaded episode.
     * @param player for which to rewind 90 seconds
     */
    public void backward(final Player player) {
        final int skipTime = 90;
        int elapsed = player.getElapsedTime();
        player.setLoadTimestamp(player.getLoadTimestamp() + (elapsed < skipTime ? 0 : skipTime));
    }

    @Override
    public void unload(final User user, final Integer timestamp) {
        Player player = user.getPlayer();
        if (player.getTrack() != null) {
            player.getEpisodeElapsedTimes().put((Episode) player.getTrack(),
                    player.getElapsedTime());
        }
        player.setCompilation(null);
        player.setTrack(null);
        player.setLoadTimestamp(0);
        player.setElapsedTime(0);
        player.setRepeats(0);
        player.setPaused(true);
    }

    @Override
    public void load(final User user, final Integer timestamp) {
        Player player = user.getPlayer();
        player.setAd(null);
        player.setCompilation(this);
        getTracks().get(0).load(user, timestamp);
        for (Track track : getTracks()) {
            Integer elapsedTime = player.getEpisodeElapsedTimes().get((Episode) track);
            if (elapsedTime != null) {
                player.setTrack(track);
                player.setLoadTimestamp(timestamp - elapsedTime);
                player.setElapsedTime(elapsedTime);
                player.getEpisodeElapsedTimes().remove((Episode) track);
                return;
            }
        }
    }

    @Override
    public void next(final User user, final Integer timestamp) {
        Player player = user.getPlayer();
        Track ad = player.getAd();
        int idx = getTracks().indexOf(player.getTrack());

        if (idx == -1) {
            idx = getTracks().indexOf(ad);
            player.setAd(null);
            ad = null;
        }

        player.getTrack().unload(user, timestamp);
        if (ad != null) {
            ad.load(user, timestamp);
            player.setAd((Song) getTracks().get(idx));
            return;
        }

        switch (player.getRepeats()) {
            case 0:
                if (idx == getTracks().size() - 1) {
                    unload(user, timestamp);
                } else {
                    getTracks().get(idx + 1).load(user, timestamp);
                }
                break;
            case 1:
                getTracks().get(idx).load(user, timestamp);
                break;
            case 2:
                getTracks().get(idx).load(user, timestamp);
                player.setRepeats(2);
                break;
            default:
                break;
        }
    }

    @Override
    public void prev(final User user, final Integer timestamp) {
        Player player = user.getPlayer();
        int idx = getTracks().indexOf(player.getTrack());

        if (idx == 0 || player.getElapsedTime() >= 1) {
            player.getTrack().unload(user, timestamp);
            getTracks().get(idx).load(user, timestamp);
        } else {
            player.getTrack().unload(user, timestamp);
            getTracks().get(idx - 1).load(user, timestamp);
        }
    }
}
