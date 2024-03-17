package environment.party.wrapped;

import environment.Library;
import environment.party.artist.Artist;
import environment.party.host.Host;
import environment.party.user.User;
import source.compilation.Podcast;
import source.track.Song;
import source.track.Track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Wrap implements WrapVisitor {
    @Override
    public Map<String, ?> visit(final User user) {
        Map<String, Object> wrapped = new HashMap<>();

        HashMap<String, Integer> artists = new HashMap<>();
        HashMap<String, Integer> genres = new HashMap<>();
        HashMap<String, Integer> songs = new HashMap<>();
        HashMap<String, Integer> albums = new HashMap<>();
        for (Song song : Library.getInstance().getSongs()) {
            Integer listens = song.getListeners().get(user.getName());
            if (listens != null) {
                Integer currListens = artists.putIfAbsent(song.getArtist(), listens);
                if (currListens != null) {
                    artists.put(song.getArtist(), listens + currListens);
                }
                currListens = genres.putIfAbsent(song.getGenre(), listens);
                if (currListens != null) {
                    genres.put(song.getGenre(), listens + currListens);
                }
                currListens = songs.putIfAbsent(song.getName(), listens);
                if (currListens != null) {
                    songs.put(song.getName(), listens + currListens);
                }
                currListens = albums.putIfAbsent(song.getAlbum(), listens);
                if (currListens != null) {
                    albums.put(song.getAlbum(), listens + currListens);
                }
            }
        }

        HashMap<String, Integer> episodes = new HashMap<>();
        for (Podcast podcast : Library.getInstance().getPodcasts()) {
            for (Track track : podcast.getTracks()) {
                Integer listens = track.getListeners().get(user.getName());
                if (listens != null) {
                    Integer currListens = episodes.putIfAbsent(track.getName(), listens);
                    if (currListens != null) {
                        episodes.put(track.getName(), listens + currListens);
                    }
                }
            }
        }

        if (!artists.isEmpty() || !genres.isEmpty() || !songs.isEmpty() || !albums.isEmpty()
                || !episodes.isEmpty()) {
            wrapped.put("topArtists", getTop5(artists));
            wrapped.put("topGenres", getTop5(genres));
            wrapped.put("topSongs", getTop5(songs));
            wrapped.put("topAlbums", getTop5(albums));
            wrapped.put("topEpisodes", getTop5(episodes));
        }

        return wrapped;
    }

    @Override
    public Map<String, ?> visit(final Artist artist) {
        Map<String, Object> wrapped = new HashMap<>();

        HashMap<String, Integer> albums = new HashMap<>();
        HashMap<String, Integer> songs = new HashMap<>();
        LinkedHashMap<String, Integer> fans = new LinkedHashMap<>();
        for (Song song : Library.getInstance().getSongs()) {
            if (song.getArtist().equals(artist.getName())) {
                for (Map.Entry<String, Integer> entry : song.getListeners().entrySet()) {
                    Integer currListens = albums.putIfAbsent(song.getAlbum(), entry.getValue());
                    if (currListens != null) {
                        albums.put(song.getAlbum(), entry.getValue() + currListens);
                    }
                    currListens = songs.putIfAbsent(song.getName(), entry.getValue());
                    if (currListens != null) {
                        songs.put(song.getName(), entry.getValue() + currListens);
                    }
                    currListens = fans.putIfAbsent(entry.getKey(), entry.getValue());
                    if (currListens != null) {
                        fans.put(entry.getKey(), entry.getValue() + currListens);
                    }
                }
            }
        }
        if (!albums.isEmpty() || !songs.isEmpty() || !fans.isEmpty()) {
            wrapped.put("topAlbums", getTop5(albums));
            wrapped.put("topSongs", getTop5(songs));
            wrapped.put("listeners", fans.size());
            wrapped.put("topFans", new ArrayList<>(getTop5(fans).keySet()));
        }

        return wrapped;
    }

    @Override
    public Map<String, ?> visit(final Host host) {
        LinkedHashMap<String, Object> wrapped = new LinkedHashMap<>();

        HashMap<String, Integer> episodes = new HashMap<>();
        HashMap<String, Integer> fans = new HashMap<>();
        for (Podcast podcast : Library.getInstance().getPodcasts()) {
            if (podcast.getOwner().equals(host.getName())) {
                for (Track ep : podcast.getTracks()) {
                    for (Map.Entry<String, Integer> entry : ep.getListeners().entrySet()) {
                        Integer currListens = episodes.putIfAbsent(ep.getName(), entry.getValue());
                        if (currListens != null) {
                            episodes.put(ep.getName(), entry.getValue() + currListens);
                        }
                        currListens = fans.putIfAbsent(entry.getKey(), entry.getValue());
                        if (currListens != null) {
                            fans.put(entry.getKey(), entry.getValue() + currListens);
                        }
                    }
                }
            }
        }
        wrapped.put("topEpisodes", getTop5(episodes));
        wrapped.put("listeners", fans.size());

        return wrapped;
    }

    private Map<String, Integer> getTop5(final Map<String, Integer> listenedTo) {
        final int max = 5;

        ArrayList<Map.Entry<String, Integer>> auxList = new ArrayList<>(listenedTo.entrySet());
        auxList.sort((x, y) -> y.getValue() > x.getValue()
                ? 1
                : y.getValue() < x.getValue()
                ? -1
                : x.getKey().compareTo(y.getKey())
        );

        listenedTo.clear();
        for (Map.Entry<String, Integer> entry : auxList) {
            listenedTo.put(entry.getKey(), entry.getValue());
            if (listenedTo.size() == max) {
                break;
            }
        }

        return listenedTo;
    }
}
