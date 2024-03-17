package environment;

import environment.party.artist.Artist;
import environment.party.host.Host;
import environment.party.user.User;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;
import lombok.Getter;
import lombok.Setter;
import source.compilation.Album;
import source.compilation.Playlist;
import source.compilation.Podcast;
import source.track.Song;

import java.util.ArrayList;

@Getter @Setter
public final class Library {
    private ArrayList<Song> songs;
    private ArrayList<Podcast> podcasts;
    private ArrayList<Playlist> playlists;
    private ArrayList<Album> albums;
    private ArrayList<User> users;
    private ArrayList<Artist> artists;
    private ArrayList<Host> hosts;

    private static Library instance = null;

    private Library() {
    }

    /**
     * Gets the instance of this singleton.
     * Creates it if it is null.
     * @return instance of this singleton
     */
    public static Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    /**
     * Copies song, podcast and user data from a LibraryInput object.
     * @param libraryInput from which to copy data
     */
    public void copyFrom(final LibraryInput libraryInput) {
        songs = new ArrayList<>();
        for (SongInput songInput : libraryInput.getSongs()) {
            songs.add(new Song(songInput));
        }
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : libraryInput.getPodcasts()) {
            podcasts.add(new Podcast(podcastInput));
        }
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        users = new ArrayList<>();
        for (UserInput userInput : libraryInput.getUsers()) {
            users.add(new User(userInput));
        }
        artists = new ArrayList<>();
        hosts = new ArrayList<>();
    }

    /**
     * Fetches the user with the given name, if one exists.
     * @param name of the user to fetch
     * @return the user with the given name, or null if one does not exist
     */
    public User getUser(final String name) {
        for (User user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Fetches the artist with the given name, if one exists.
     * @param name of the artist to fetch
     * @return the artist with the given name, or null if one does not exist
     */
    public Artist getArtist(final String name) {
        for (Artist artist : artists) {
            if (artist.getName().equals(name)) {
                return artist;
            }
        }
        return null;
    }

    /**
     * Fetches the host with the given name, if one exists.
     * @param name of the host to fetch
     * @return the host with the given name, or null if one does not exist
     */
    public Host getHost(final String name) {
        for (Host host : hosts) {
            if (host.getName().equals(name)) {
                return host;
            }
        }
        return null;
    }
}
