package environment.party.artist;

import environment.Library;
import environment.page.ArtistPageGenerator;
import environment.page.PageGeneratorStrategy;
import environment.party.Party;
import environment.party.observer.SubjectParty;
import environment.party.user.Notification;
import environment.party.wrapped.WrapVisitable;
import environment.party.wrapped.WrapVisitor;
import environment.party.user.User;
import lombok.Getter;
import lombok.Setter;
import source.compilation.Album;
import source.compilation.Playlist;
import source.track.Song;
import source.track.Track;

import java.util.*;

@Getter @Setter
public final class Artist extends Party implements WrapVisitable, SubjectParty {
    private ArrayList<Album> albums;
    private ArrayList<Event> events;
    private ArrayList<Merch> merchandise;
    private double songRevenue;
    private double merchRevenue;
    private HashMap<String, Double> profit;
    private HashSet<User> subscribers;

    public Artist(final String name, final int age, final String city) {
        super(name, age, city);
        albums = new ArrayList<>();
        events = new ArrayList<>();
        merchandise = new ArrayList<>();
        songRevenue = 0;
        merchRevenue = 0;
        profit = new HashMap<>();
        subscribers = new HashSet<>();
    }

    @Override
    public boolean add(final Library library) {
        if (library.getUser(getName()) != null || library.getArtist(getName()) != null
                || library.getHost(getName()) != null) {
            return false;
        }
        library.getArtists().add(this);
        return true;
    }

    @Override
    public boolean delete(final Library library, final int timestamp) {
        for (User user : library.getUsers()) {
            user.getPlayer().update(user, timestamp);
            if (user.getSelectedParty() == this) {
                return false;
            }
            for (Album album : albums) {
                if (user.getPlayer().getCompilation() == album) {
                    return false;
                }
                if (album.contains(user.getPlayer().getTrack())) {
                    return false;
                }
                for (Track track : album.getTracks()) {
                    for (Playlist playlist : user.getPlaylists()) {
                        if (playlist.contains(track)) {
                            return false;
                        }
                    }
                }
            }
        }
        for (Album album : albums) {
            library.getAlbums().remove(album);
            for (Track track : album.getTracks()) {
                library.getSongs().remove((Song) track);
                for (User user : library.getUsers()) {
                    if (user.getLikedSongs().contains(track)) {
                        user.getLikedSongs().remove((Song) track);
                    }
                }
            }
        }
        library.getArtists().remove(this);
        return true;
    }

    @Override
    public PageGeneratorStrategy getPageGenerator() {
        return new ArtistPageGenerator(this);
    }

    @Override
    public Map<String, ?> accept(final WrapVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getType() {
        return "artist";
    }

    @Override
    public boolean addRemoveSubscriber(final User user) {
        return subscribers.add(user) || !subscribers.remove(user);
    }

    /**
     * Fetches the added like count of all the albums of this artist.
     * @return the added like count of all the albums of this artist.
     */
    public int getLikeCount() {
        int likeCount = 0;

        for (Album album : albums) {
            likeCount += album.getLikeCount();
        }

        return likeCount;
    }

    /**
     * Fetches this artist's album with the given name
     * @param name of the album to fetch
     * @return the album with the given name, or null if it does not exist
     */
    public Album getAlbum(final String name) {
        for (Album album : albums) {
            if (album.getName().equals(name)) {
                return album;
            }
        }
        return null;
    }

    /**
     * Attempts to add the given album and its songs to the given library
     * @param library in which to add the given album
     * @param album to attempt to add to the given library
     * @return false if the album already exists or true if the addition was successful
     */
    public boolean addAlbum(final Library library, final Album album) {
        ArrayList<Track> songs = album.getTracks();
        for (int idx1 = 0; idx1 < songs.size() - 1; idx1++) {
            for (int idx2 = idx1 + 1; idx2 < songs.size(); idx2++) {
                if (songs.get(idx1).getName().equals(songs.get(idx2).getName())) {
                    return false;
                }
            }
        }
        albums.add(album);
        for (Track track : songs) {
            library.getSongs().add((Song) track);
        }
        library.getAlbums().add(album);
        for (User user : getSubscribers()) {
            user.update(Notification.NotifType.ALBUM_NOTIF, this);
        }
        return true;
    }

    /**
     * Attempts to remove the given album and its songs from the given library
     * @param library from which to remove the given album
     * @param album to attempt to remove from the given library
     * @return false if the given album is not in the library, or true if the removal succeeded
     */
    public boolean removeAlbum(final Library library, final Album album) {
        for (User user : library.getUsers()) {
            if (user.getPlayer().getCompilation() == album) {
                return false;
            } else if (album.contains(user.getPlayer().getTrack())) {
                return false;
            } else {
                for (Track track : album.getTracks()) {
                    for (Playlist playlist : user.getPlaylists()) {
                        if (playlist.contains(track)) {
                            return false;
                        }
                    }
                }
            }
        }
        albums.remove(album);
        library.getAlbums().remove(album);
        return true;
    }

    /**
     * Fetches this artist's event with the given name
     * @param name of the event to fetch
     * @return the event with the given name, or null if it does not exist
     */
    public Event getEvent(final String name) {
        for (Event event : events) {
            if (event.getName().equals(name)) {
                return event;
            }
        }
        return null;
    }

    /**
     * Attempts to add the given event to the events list
     * @param event to attempt to add to the events list
     * @return false if the event already exists or true if the addition was successful
     */
    public boolean addEvent(final Event event) {
        if (getEvent(event.getName()) != null) {
            return false;
        }
        events.add(event);
        for (User user : getSubscribers()) {
            user.update(Notification.NotifType.EVENT_NOTIF, this);
        }
        return true;
    }

    /**
     * Attempts to remove the event with the given name from the events list
     * @param name of the event to attempt to remove from the events list
     * @return false if the event is not in the events list or true if the removal was successful
     */
    public boolean removeEvent(final String name) {
        Event event = getEvent(name);
        if (event == null) {
            return false;
        }
        events.remove(event);
        return true;
    }

    /**
     * Attempts to add the given merch to the merchandise list
     * @param newMerch to attempt to add to the merchandise list
     * @return false if the merch already exists or true if the addition succeeded
     */
    public boolean addMerch(final Merch newMerch) {
        for (Merch merch : merchandise) {
            if (merch.getName().equals(newMerch.getName())) {
                return false;
            }
        }
        merchandise.add(newMerch);
        for (User user : getSubscribers()) {
            user.update(Notification.NotifType.MERCH_NOTIF, this);
        }
        return true;
    }

    /**
     * Determines if this artist has generated revenue or has any listens
     * @return true if the artist is relevant in the sense described above or false otherwise
     */
    public boolean isRelevant() {
        if (merchRevenue != 0) {
            return true;
        }

        for (Song song : Library.getInstance().getSongs()) {
            if (song.getArtist().equals(getName()) && !song.getListeners().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Adds the given value to this artist's merch revenue
     * @param value to add to this artist's merch revenue
     */
    public void addMerchRevenue(final double value) {
        merchRevenue += value;
    }

    /**
     * Adds the given value to this artist's song revenue
     * @param value to add to this artist's song revenue
     */
    public void addSongRevenue(final double value) {
        songRevenue += value;
    }

    /**
     * Fetches the name of this artist's song which has generated the most revenue
     * @return the name of this artist's song which has generated the most revenue
     */
    public String getMostProfitableSong() {
        ArrayList<Map.Entry<String, Double>> auxList = new ArrayList<>(profit.entrySet());
        auxList.sort((x, y) -> y.getValue() > x.getValue()
                ? 1
                : y.getValue() < x.getValue()
                ? -1
                : x.getKey().compareTo(y.getKey())
        );
        return auxList.isEmpty() ? null : auxList.get(0).getKey();
    }

    /**
     * Determines this artist's ranking among all the other artists based on their revenue
     * @return an integer representing this artist's ranking
     */
    public int getRanking() {
        int ranking = 1;
        for (Artist artist : Library.getInstance().getArtists()) {
            double thisRevenue = songRevenue + merchRevenue;
            double revenue = artist.songRevenue + artist.merchRevenue;
            if ((revenue == thisRevenue && artist.getName().compareTo(getName()) < 0
                    || revenue > thisRevenue) && artist.isRelevant()) {
                ranking++;
            }
        }
        return ranking;
    }
}
