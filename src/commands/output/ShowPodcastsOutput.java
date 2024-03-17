package commands.output;

import lombok.Getter;
import source.compilation.Podcast;
import source.track.Track;

import java.util.ArrayList;

@Getter
public final class ShowPodcastsOutput extends UserOutput {
    private ArrayList<DummyPodcast> result;

    public ShowPodcastsOutput(final String command, final Integer timestamp, final String user,
                            final ArrayList<Podcast> podcasts) {
        super(command, timestamp, user);
        result = new ArrayList<>();
        for (Podcast podcast : podcasts) {
            result.add(new DummyPodcast(podcast));
        }
    }

    @Getter
    class DummyPodcast {
        private String name;
        private ArrayList<String> episodes;

        DummyPodcast(final Podcast podcast) {
            name = podcast.getName();
            episodes = new ArrayList<>();
            for (Track track : podcast.getTracks()) {
                episodes.add(track.getName());
            }
        }
    }
}
