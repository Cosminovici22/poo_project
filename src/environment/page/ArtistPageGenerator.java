package environment.page;

import environment.party.artist.Artist;
import environment.party.artist.Event;
import environment.party.artist.Merch;
import source.compilation.Album;

public final class ArtistPageGenerator implements PageGeneratorStrategy {
    private Artist artist;

    public ArtistPageGenerator(final Artist artist) {
        this.artist = artist;
    }

    @Override
    public String generatePage() {
        int commaCount = 0;
        String result = "Albums:\n\t[";

        for (Album album : artist.getAlbums()) {
            result = result.concat(album.getName());
            if (commaCount < artist.getAlbums().size() - 1) {
                result = result.concat(", ");
                commaCount++;
            }
        }
        result = result.concat("]\n\nMerch:\n\t[");
        commaCount = 0;
        for (Merch merch : artist.getMerchandise()) {
            result = result.concat(merch.getName() + " - " + merch.getPrice() + ":\n\t"
                    + merch.getDescription());
            if (commaCount < artist.getMerchandise().size() - 1) {
                result = result.concat(", ");
                commaCount++;
            }
        }
        result = result.concat("]\n\nEvents:\n\t[");
        commaCount = 0;
        for (Event event : artist.getEvents()) {
            result = result.concat(event.getName() + " - " + event.getDate()  + ":\n\t"
                    + event.getDescription());
            if (commaCount < artist.getEvents().size() - 1) {
                result = result.concat(", ");
                commaCount++;
            }
        }
        result = result.concat("]");

        return result;
    }
}
