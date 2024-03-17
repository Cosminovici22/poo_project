package environment.party.user;

import environment.Library;
import environment.page.HomePageGenerator;
import environment.page.PageGeneratorStrategy;
import environment.party.Party;
import environment.party.observer.ObserverUser;
import environment.party.observer.SubjectParty;
import environment.party.wrapped.WrapVisitable;
import environment.party.wrapped.WrapVisitor;
import environment.party.artist.Artist;
import environment.party.artist.Merch;
import fileio.input.UserInput;
import lombok.Getter;
import lombok.Setter;
import source.Source;
import source.compilation.Album;
import source.compilation.Playlist;
import source.track.Song;
import source.track.Track;

import java.util.*;

@Getter @Setter
public final class User extends Party implements WrapVisitable, ObserverUser {
    private Player player;

    private ArrayList<Playlist> playlists;

    private ArrayList<Playlist> followedPlaylists;
    private ArrayList<Song> likedSongs;

    private ArrayList<Source> searchedSources;
    private Source selectedSource;
    private ArrayList<Party> searchedParties;
    private Party selectedParty;

    private boolean offline;

    private ArrayList<Merch> ownedMerch;

    private boolean premium;

    private ArrayList<Song> freeSongHistory;
    private ArrayList<Song> premiumSongHistory;
    private ArrayList<Song> songHistory;

    private ArrayList<Notification> notifications;

    private Song randomSong;
    private Playlist randomPlaylist;
    private Playlist fansPlaylist;
    private Source latestRecommendation;

    private PageGeneratorStrategy currPage;
    private LinkedList<PageGeneratorStrategy> nextPages;
    private LinkedList<PageGeneratorStrategy> prevPages;

    public User(final String name, final int age, final String city) {
        super(name, age, city);
        player = new Player();
        playlists = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        searchedSources = null;
        selectedSource = null;
        searchedParties = null;
        selectedParty = null;
        offline = false;
        ownedMerch = new ArrayList<>();
        premium = false;
        freeSongHistory = new ArrayList<>();
        premiumSongHistory = new ArrayList<>();
        songHistory = freeSongHistory;
        notifications = new ArrayList<>();
        randomSong = null;
        randomPlaylist = null;
        fansPlaylist = null;
        latestRecommendation = null;
        currPage = new HomePageGenerator(this);
        nextPages = new LinkedList<>();
        prevPages = new LinkedList<>();
    }

    public User(final UserInput userInput) {
        super(userInput.getUsername(), userInput.getAge(), userInput.getCity());
        player = new Player();
        playlists = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        searchedSources = null;
        selectedSource = null;
        searchedParties = null;
        selectedParty = null;
        offline = false;
        ownedMerch = new ArrayList<>();
        premium = false;
        freeSongHistory = new ArrayList<>();
        premiumSongHistory = new ArrayList<>();
        songHistory = freeSongHistory;
        notifications = new ArrayList<>();
        randomSong = null;
        randomPlaylist = null;
        fansPlaylist = null;
        latestRecommendation = null;
        currPage = new HomePageGenerator(this);
        nextPages = new LinkedList<>();
        prevPages = new LinkedList<>();
    }

    @Override
    public boolean add(final Library library) {
        if (library.getUser(getName()) != null || library.getArtist(getName()) != null
                || library.getHost(getName()) != null) {
            return false;
        }
        library.getUsers().add(this);
        return true;
    }

    @Override
    public boolean delete(final Library library, final int timestamp) {
        if (premium) {
            return false;
        }
        if (player.getTrack() != null) {
            return false;
        }
        for (User user : library.getUsers()) {
            user.getPlayer().update(user, timestamp);
            if (user != this) {
                for (Playlist thisPlaylist : playlists) {
                    if (user.player.getCompilation() == thisPlaylist) {
                        return false;
                    }
                }
            }
        }

        for (User user : library.getUsers()) {
            for (Playlist playlist : playlists) {
                user.followedPlaylists.remove(playlist);
            }
            while (!followedPlaylists.isEmpty()) {
                followedPlaylists.get(0).follow(this);
            }
        }
        library.getUsers().remove(this);
        return true;
    }

    @Override
    public PageGeneratorStrategy getPageGenerator() {
        return null;
    }

    @Override
    public Map<String, ?> accept(final WrapVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getType() {
        return "user";
    }

    /**
     * Select a source for this user
     * @param source to select
     */
    public void selectSource(final Source source) {
        searchedSources = null;
        selectedSource = source;
    }

    /**
     * Select a source for this user
     * @param party to select
     */
    public void selectParty(final Party party) {
        currPage = party.getPageGenerator();
        searchedParties = null;
        selectedParty = party;
    }

    /**
     * Create a playlist for this user
     * @param library in which to add the playlist in chronological order of creation time
     * @param playlistName to name the playlist
     * @return true if the playlist was created for this user or false if it already existed
     */
    public boolean createPlaylist(final Library library, final String playlistName) {
        for (Playlist playlist : playlists) {
            if (playlist.getName().equals(playlistName)) {
                return false;
            }
        }
        Playlist playlist = new Playlist(playlistName, getName(), new ArrayList<>());
        playlists.add(playlist);
        library.getPlaylists().add(playlist);
        return true;
    }

    /**
     * Toggle the offline status of this user
     * @param timestamp at which status switch occurred
     */
    public void switchStatus(final int timestamp) {
        if (offline) {
            player.setLoadTimestamp(timestamp - player.getElapsedTime());
        }
        offline = !offline;
    }

    /**
     * Change the currPage and record it in this user's page history
     * @param pageGenerator to which to change the currPage
     */
    public void changePage(final PageGeneratorStrategy pageGenerator) {
        prevPages.push(currPage);
        currPage = pageGenerator;
    }

    /**
     * Load the previously accessed page
     * @return false if the previous page history is empty
     */
    public boolean prevPage() {
        if (prevPages.isEmpty()) {
            return false;
        }
        nextPages.push(currPage);
        currPage = prevPages.pop();
        return true;
    }

    /**
     * Load a previously accessed page on which the prevPage command was applied
     * @return false if the forward page history is empty
     */
    public boolean nextPage() {
        if (nextPages.isEmpty()) {
            return false;
        }
        prevPages.push(currPage);
        currPage = nextPages.pop();
        return true;
    }

    /**
     * The `execute` method of this strategy
     * @return a string representing the currently loaded page
     */
    public String generateCurrentPage() {
        return currPage.generatePage();
    }

    /**
     * Toggle the premium status of this user to the desired state if they differ
     * @param state to which to toggle the premium field
     * @return false if premium is already set to the given state or true otherwise
     */
    public boolean setPremium(final boolean state) {
        if (premium == state) {
            return false;
        }
        premium = state;
        return true;
    }

    /**
     * Acquires the revenue this user generated for a given artist
     * @param artist for whom to determine the generated revenue
     * @param price of premium or an ad this user listened to
     * @return the generated revenue for the given artist
     */
    public double getGeneratedRevenueFor(final Artist artist, final double price) {
        int songArtist = 0;
        for (Song song : songHistory) {
            if (song.getArtist().equals(artist.getName())) {
                songArtist++;
                Double profit = artist.getProfit().putIfAbsent(
                        song.getName(),
                        price / songHistory.size()
                );
                if (profit != null) {
                    artist.getProfit().put(
                            song.getName(),
                            profit + price / songHistory.size()
                    );
                }
            }
        }
        return songHistory.isEmpty() ? 0 : price / songHistory.size() * songArtist;
    }

    @Override
    public void update(final Notification.NotifType notifType, final SubjectParty subjectParty) {
        notifications.add(new Notification(notifType, (Party) subjectParty));
    }

    /**
     * Generates a random song in accordance with the currently playing track and its elapsed time
     * @param currTrack on which to base the generation
     * @param elapsedTime on which to base the generation
     * @return true if the generation was successful
     */
    public boolean generateRandomSong(final Track currTrack, final Integer elapsedTime) {
        final int maxElapsedTime = 30;

        if (elapsedTime < maxElapsedTime || !(currTrack instanceof Song currSong)) {
            randomSong = null;
            return false;
        }

        Random random = new Random(elapsedTime);
        ArrayList<Song> searchResults = new ArrayList<>();

        for (Song song : Library.getInstance().getSongs()) {
            if (song.getGenre().equalsIgnoreCase(currSong.getGenre())) {
                searchResults.add(song);
            }
        }

        randomSong = searchResults.get(random.nextInt(searchResults.size()));
        return true;
    }

    /**
     * Generate a random playlist based on the currently playing track
     * @param currTrack on which to base the generation
     * @return true if the generation was successful
     */
    public boolean generateRandomPlaylist(final Track currTrack) {
        final int maxSongCount = 5;
        final int maxGenreCount = 3;

        if (!(currTrack instanceof Song currSong)) {
            randomSong = null;
            return false;
        }

        HashMap<String, Integer> genreAppearances = new HashMap<>();
        for (Song song : likedSongs) {
            Integer appearances = genreAppearances.putIfAbsent(song.getGenre(), 1);
            if (appearances != null) {
                genreAppearances.put(song.getGenre(), appearances + 1);
            }
        }
        for (Playlist playlist : playlists) {
            for (Track track : playlist.getTracks()) {
                Song song = (Song) track;
                Integer appearances = genreAppearances.putIfAbsent(song.getGenre(), 1);
                if (appearances != null) {
                    genreAppearances.put(song.getGenre(), appearances + 1);
                }
            }
        }
        for (Playlist playlist : followedPlaylists) {
            for (Track track : playlist.getTracks()) {
                Song song = (Song) track;
                Integer appearances = genreAppearances.putIfAbsent(song.getGenre(), 1);
                if (appearances != null) {
                    genreAppearances.put(song.getGenre(), appearances + 1);
                }
            }
        }

        ArrayList<Map.Entry<String, Integer>> genres = new ArrayList<>(genreAppearances.entrySet());
        genres.sort((x, y) -> y.getValue() > x.getValue()
                ? 1
                : y.getValue() < x.getValue()
                ? -1
                : x.getKey().compareTo(y.getKey())
        );

        ArrayList<Track> songs = new ArrayList<>();
        int songCount = maxSongCount;
        int genreCounter = 1;
        for (Map.Entry<String, Integer> entry : genres) {
            String genre = entry.getKey();
            ArrayList<Song> topSongs = new ArrayList<>();
            for (Song song : Library.getInstance().getSongs()) {
                if (song.getGenre().equalsIgnoreCase(genre)) {
                    boolean isSingular = true;
                    for (Song topSong : topSongs) {
                        if (topSong.getName().equals(song.getName())) {
                            isSingular = false;
                            break;
                        }
                    }
                    if (isSingular) {
                        topSongs.add(song);
                    }
                }
            }
            topSongs.sort((x, y) -> y.getLikeCount() > x.getLikeCount()
                    ? 1
                    : y.getLikeCount() < x.getLikeCount()
                    ? -1
                    : x.getName().compareTo(y.getName())
            );
            int songCounter = 1;
            for (Track song : topSongs) {
                songs.add(song);
                if (songCounter == songCount) {
                    if (songCount == maxSongCount) {
                        songCount -= 2;
                    } else {
                        songCount--;
                    }
                    break;
                }
                songCounter++;
            }

            if (genreCounter == maxGenreCount) {
                break;
            }
            genreCounter++;
        }

        randomPlaylist = new Playlist(getName() + "'s recommendations", getName(), songs);
        return true;
    }

    /**
     * Generate a fan playlist based on the currently playing track
     * @param currTrack on which to base the generation
     * @return true if the generation was successful
     */
    public boolean generateFansPlaylist(final Track currTrack) {
        final int maxSongCount = 5;

        if (!(currTrack instanceof Song currSong)) {
            randomSong = null;
            return false;
        }

        HashMap<String, Integer> listeners = new HashMap<>();
        for (Album album : Library.getInstance().getArtist(currSong.getArtist()).getAlbums()) {
            for (Track song : album.getTracks()) {
                listeners.putAll(song.getListeners());
            }
        }

        ArrayList<Map.Entry<String, Integer>> topListeners = new ArrayList<>(listeners.entrySet());
        topListeners.sort((x, y) -> y.getValue() > x.getValue()
                ? 1
                : y.getValue() < x.getValue()
                ? -1
                : x.getKey().compareTo(y.getKey())
        );

        ArrayList<Track> songs = new ArrayList<>();
        int fanCounter = 1;
        for (Map.Entry<String, Integer> entry : topListeners) {
            User user = Library.getInstance().getUser(entry.getKey());
            ArrayList<Song> topSongs = new ArrayList<>(user.getLikedSongs());
            topSongs.sort((x, y) -> y.getLikeCount() > x.getLikeCount()
                    ? 1
                    : y.getLikeCount() < x.getLikeCount()
                    ? -1
                    : x.getName().compareTo(y.getName())
            );
            int songCounter = 1;
            for (Song topSong : topSongs) {
                if (songCounter == maxSongCount) {
                    break;
                }
                boolean isSingular = true;
                for (Track song : songs) {
                    if (topSong.getName().equals(song.getName())) {
                        isSingular = false;
                        break;
                    }
                }
                if (isSingular) {
                    songs.add(topSong);
                    songCounter++;
                }
            }
            if (fanCounter == maxSongCount) {
                break;
            }
            fanCounter--;
        }

        fansPlaylist = new Playlist(currSong.getArtist() + " Fan Club recommendations",
                getName(), songs);
        return true;
    }
}
