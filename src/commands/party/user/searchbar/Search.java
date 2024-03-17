package commands.party.user.searchbar;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import commands.Command;
import commands.output.Output;
import environment.Library;
import environment.party.user.Player;
import environment.party.artist.Artist;
import environment.party.host.Host;
import environment.party.Party;
import source.Source;
import source.compilation.Album;
import source.compilation.Playlist;
import environment.party.user.User;
import lombok.Setter;
import commands.output.SearchOutput;
import source.compilation.Podcast;
import source.track.Song;

import java.util.ArrayList;

@Setter
public final class Search extends Command {
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
            property = "type",
            visible = true)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = SongFilters.class, name = "song"),
            @JsonSubTypes.Type(value = PlaylistFilters.class, name = "playlist"),
            @JsonSubTypes.Type(value = PodcastFilters.class, name = "podcast"),
            @JsonSubTypes.Type(value = AlbumFilters.class, name = "album"),
            @JsonSubTypes.Type(value = ArtistFilters.class, name = "artist"),
            @JsonSubTypes.Type(value = HostFilters.class, name = "host")
    })
    private Filters filters;
    private String type;

    @Override
    public Output execute(final Library library) {
        User currUser = library.getUser(getUsername());
        Player player = currUser.getPlayer();

        if (currUser.isOffline()) {
            return new SearchOutput(getCommand(), getUsername(), getTimestamp(),
                    getUsername() + " is offline.", new ArrayList<>());
        }

        player.update(currUser, getTimestamp());
        if (player.getCompilation() != null) {
            player.getCompilation().unload(currUser, getTimestamp());
        } else if (player.getTrack() != null) {
            player.getTrack().unload(currUser, getTimestamp());
        }

        final int maxResultSize = 5;
        currUser.setSearchedParties(null);
        currUser.setSearchedSources(null);
        ArrayList<Source> sources = new ArrayList<>();
        ArrayList<Party> parties = new ArrayList<>();
        ArrayList<String> results = new ArrayList<>();
        switch (type) {
            case "song":
                for (Song song : library.getSongs()) {
                    if (sources.size() == maxResultSize) {
                        break;
                    }
                    if (filters.matchSource(song)) {
                        sources.add(song);
                    }
                }
                currUser.setSearchedSources(sources);

                for (Source source : sources) {
                    results.add(source.getName());
                }
                return new SearchOutput(getCommand(), getUsername(), getTimestamp(),
                        "Search returned " + results.size() + " results", results);
            case "playlist":
                for (Playlist playlist : library.getPlaylists()) {
                    if (sources.size() == maxResultSize) {
                        break;
                    }
                    if ((playlist.isVisible() || playlist.getOwner().equals(getUsername()))
                            && filters.matchSource(playlist)) {
                        sources.add(playlist);
                    }
                }
                currUser.setSearchedSources(sources);

                for (Source source : sources) {
                    results.add(source.getName());
                }
                return new SearchOutput(getCommand(), getUsername(), getTimestamp(),
                        "Search returned " + results.size() + " results", results);
            case "podcast":
                for (Podcast podcast : library.getPodcasts()) {
                    if (sources.size() == maxResultSize) {
                        break;
                    }
                    if (filters.matchSource(podcast)) {
                        sources.add(podcast);
                    }
                }
                currUser.setSearchedSources(sources);

                for (Source source : sources) {
                    results.add(source.getName());
                }
                return new SearchOutput(getCommand(), getUsername(), getTimestamp(),
                        "Search returned " + results.size() + " results", results);
            case "album":
                for (Artist artist : library.getArtists()) {
                    for (Album album : artist.getAlbums()) {
                        if (sources.size() == maxResultSize) {
                            break;
                        }
                        if (filters.matchSource(album)) {
                            sources.add(album);
                        }
                    }
                }
                currUser.setSearchedSources(sources);

                for (Source source : sources) {
                    results.add(source.getName());
                }
                return new SearchOutput(getCommand(), getUsername(), getTimestamp(),
                        "Search returned " + results.size() + " results", results);
            case "artist":
                for (Artist artist : library.getArtists()) {
                    if (parties.size() == maxResultSize) {
                        break;
                    }
                    if (filters.matchParty(artist)) {
                        parties.add(artist);
                    }
                }
                currUser.setSearchedParties(parties);

                for (Party party : parties) {
                    results.add(party.getName());
                }
                return new SearchOutput(getCommand(), getUsername(), getTimestamp(),
                        "Search returned " + results.size() + " results", results);
            case "host":
                for (Host host : library.getHosts()) {
                    if (parties.size() == maxResultSize) {
                        break;
                    }
                    if (filters.matchParty(host)) {
                        parties.add(host);
                    }
                }
                currUser.setSearchedParties(parties);

                for (Party party : parties) {
                    results.add(party.getName());
                }
                return new SearchOutput(getCommand(), getUsername(), getTimestamp(),
                        "Search returned " + results.size() + " results", results);
            default:
                break;
        }

        return new SearchOutput(getCommand(), getUsername(), getTimestamp(),
                "Search returned 0 results", new ArrayList<>());
    }

    interface Filters {
        boolean matchSource(Source source);
        boolean matchParty(Party party);
    }

    @Setter
    public static final class SongFilters implements Filters {
        private String name;
        private String album;
        private ArrayList<String> tags;
        private String lyrics;
        private String genre;
        private String releaseYear;
        private String artist;
        private String owner;

        @Override
        public boolean matchSource(final Source source) {
            Song song = (Song) source;
            return genresMatch(song.getGenre()) && albumsMatch(song.getAlbum())
                    && tagsMatch(song.getTags()) && lyricsMatch(song.getLyrics())
                    && namesMatch(song.getName()) && artistsMatch(song.getArtist())
                    && releaseYearsMatch(song.getReleaseYear());
        }

        @Override
        public boolean matchParty(final Party party) {
            return false;
        }

        public boolean namesMatch(final String songName) {
            if (name == null) {
                return true;
            }
            return songName.toLowerCase().startsWith(name.toLowerCase());
        }

        public boolean albumsMatch(final String songAlbum) {
            if (album == null) {
                return true;
            }
            return songAlbum.equalsIgnoreCase(album);
        }

        public boolean tagsMatch(final ArrayList<String> songTags) {
            if (tags == null) {
                return true;
            }
            return songTags.containsAll(tags);
        }

        public boolean lyricsMatch(final String songLyrics) {
            if (lyrics == null) {
                return true;
            }
            return songLyrics.toLowerCase().contains(lyrics.toLowerCase());
        }

        public boolean genresMatch(final String songGenre) {
            if (genre == null) {
                return true;
            }
            return songGenre.equalsIgnoreCase(genre);
        }

        public boolean releaseYearsMatch(final int songReleaseYear) {
            if (releaseYear == null) {
                return true;
            }
            int year = Integer.parseInt(releaseYear.substring(1));
            if (releaseYear.startsWith("<")) {
                return songReleaseYear < year;
            } else if (releaseYear.startsWith(">")) {
                return songReleaseYear > year;
            }
            return false;
        }

        public boolean artistsMatch(final String songArtist) {
            if (artist == null) {
                return true;
            }
            return songArtist.equalsIgnoreCase(artist);
        }
    }

    @Setter
    public static final class PlaylistFilters implements Filters {
        private String name;
        private String owner;

        @Override
        public boolean matchSource(final Source source) {
            Playlist playlist = (Playlist) source;
            return namesMatch(playlist.getName()) && ownersMatch(playlist.getOwner());
        }

        @Override
        public boolean matchParty(final Party party) {
            return false;
        }

        public boolean namesMatch(final String playlistName) {
            if (name == null) {
                return true;
            }
            return playlistName.toLowerCase().startsWith(name.toLowerCase());
        }

        public boolean ownersMatch(final String playlistOwner) {
            if (owner == null) {
                return true;
            }
            return playlistOwner.equalsIgnoreCase(owner);
        }
    }

    @Setter
    public static final class PodcastFilters implements Filters {
        private String name;
        private String owner;

        @Override
        public boolean matchSource(final Source source) {
            Podcast podcast = (Podcast) source;
            return namesMatch(podcast.getName()) && ownersMatch(podcast.getOwner());
        }

        @Override
        public boolean matchParty(final Party party) {
            return false;
        }

        public boolean namesMatch(final String podcastName) {
            if (name == null) {
                return true;
            }
            return podcastName.toLowerCase().startsWith(name.toLowerCase());
        }

        public boolean ownersMatch(final String podcastOwner) {
            if (owner == null) {
                return true;
            }
            return podcastOwner.equalsIgnoreCase(owner);
        }
    }

    @Setter
    public static final class AlbumFilters implements Filters {
        private String name;
        private String owner;
        private String description;

        @Override
        public boolean matchSource(final Source source) {
            Album album = (Album) source;
            return namesMatch(album.getName()) && ownersMatch(album.getOwner())
                    && descriptionMatch(album.getDescription());
        }

        @Override
        public boolean matchParty(final Party party) {
            return false;
        }

        public boolean namesMatch(final String albumName) {
            if (name == null) {
                return true;
            }
            return albumName.toLowerCase().startsWith(name.toLowerCase());
        }

        public boolean ownersMatch(final String albumOwner) {
            if (owner == null) {
                return true;
            }
            return albumOwner.toLowerCase().startsWith(owner.toLowerCase());
        }

        public boolean descriptionMatch(final String albumDescription) {
            if (description == null) {
                return true;
            }
            return albumDescription.toLowerCase().startsWith(description.toLowerCase());
        }
    }

    @Setter
    public static final class ArtistFilters implements Filters {
        private String name;

        @Override
        public boolean matchSource(final Source source) {
            return false;
        }

        @Override
        public boolean matchParty(final Party party) {
            Artist artist = (Artist) party;
            if (name == null) {
                return true;
            }
            return artist.getName().toLowerCase().startsWith(name.toLowerCase());
        }
    }

    @Setter
    public static final class HostFilters implements Filters {
        private String name;
        private String owner;

        @Override
        public boolean matchSource(final Source source) {
            return false;
        }

        @Override
        public boolean matchParty(final Party party) {
            Host host = (Host) party;
            if (owner != null) {
                return false;
            }
            if (name == null) {
                return true;
            }
            return host.getName().toLowerCase().startsWith(name.toLowerCase());
        }
    }
}
