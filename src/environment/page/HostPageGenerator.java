package environment.page;

import environment.party.host.Announcement;
import environment.party.host.Host;
import source.compilation.Podcast;
import source.track.Episode;
import source.track.Track;

public final class HostPageGenerator implements PageGeneratorStrategy {
    private Host host;

    public HostPageGenerator(final Host host) {
        this.host = host;
    }

    @Override
    public String generatePage() {
        int commaCount = 0;
        String result = "Podcasts:\n\t[";

        for (Podcast podcast : host.getPodcasts()) {
            result = result.concat(podcast.getName() + ":\n\t[");
            int episodeCommaCount = 0;
            for (Track track : podcast.getTracks()) {
                Episode episode = (Episode) track;
                result = result.concat(episode.getName() + " - " + episode.getDescription());
                if (episodeCommaCount < podcast.getTracks().size() - 1) {
                    result = result.concat(", ");
                    episodeCommaCount++;
                }
            }
            result = result.concat("]\n");
            if (commaCount < host.getPodcasts().size() - 1) {
                result = result.concat(", ");
                commaCount++;
            }
        }
        result = result.concat("]\n\nAnnouncements:\n\t[");
        commaCount = 0;
        for (Announcement announcement : host.getAnnouncements()) {
            result = result.concat(announcement.getName() + ":\n\t"
                    + announcement.getDescription() + "\n");
            if (commaCount < host.getAnnouncements().size() - 1) {
                result = result.concat(", ");
                commaCount++;
            }
        }
        result = result.concat("]");

        return result;
    }
}
