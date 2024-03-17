package environment.party.host;

import environment.Library;
import environment.page.HostPageGenerator;
import environment.page.PageGeneratorStrategy;
import environment.party.Party;
import environment.party.observer.SubjectParty;
import environment.party.user.Notification;
import environment.party.wrapped.WrapVisitable;
import environment.party.wrapped.WrapVisitor;
import environment.party.user.User;
import lombok.Getter;
import lombok.Setter;
import source.compilation.Podcast;
import source.track.Track;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

@Getter @Setter
public final class Host extends Party implements WrapVisitable, SubjectParty {
    private ArrayList<Podcast> podcasts;
    private ArrayList<Announcement> announcements;
    private HashSet<User> subscribers;

    public Host(final String name, final int age, final String city) {
        super(name, age, city);
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
        subscribers = new HashSet<>();
    }

    @Override
    public boolean add(final Library library) {
        if (library.getUser(getName()) != null || library.getArtist(getName()) != null
                || library.getHost(getName()) != null) {
            return false;
        }
        library.getHosts().add(this);
        return true;
    }

    @Override
    public boolean delete(final Library library, final int timestamp) {
        for (User user : library.getUsers()) {
            user.getPlayer().update(user, timestamp);
            if (user.getSelectedParty() == this) {
                return false;
            }
            for (Podcast podcast : podcasts) {
                if (user.getPlayer().getCompilation() == podcast) {
                    return false;
                }
                if (podcast.contains(user.getPlayer().getTrack())) {
                    return false;
                }
            }
        }

        library.getHosts().remove(this);
        return true;
    }

    @Override
    public PageGeneratorStrategy getPageGenerator() {
        return new HostPageGenerator(this);
    }

    @Override
    public Map<String, ?> accept(final WrapVisitor visitor) {
        return visitor.visit(this);
    }

    @Override
    public String getType() {
        return "host";
    }

    @Override
    public boolean addRemoveSubscriber(final User user) {
        return subscribers.add(user) || !subscribers.remove(user);
    }

    /**
     * Fetches this host's podcast with the given name
     * @param name of the podcast to fetch
     * @return the podcast with the given name, or null if it does not exist
     */
    public Podcast getPodcast(final String name) {
        for (Podcast podcast : podcasts) {
            if (podcast.getName().equals(name)) {
                return podcast;
            }
        }
        return null;
    }

    /**
     * Attempts to add the given podcast to the given library
     * @param library in which to add the given podcast
     * @param podcast to attempt to add to the given library
     * @return false if the podcast already exists or true if the addition was successful
     */
    public boolean addPodcast(final Library library, final Podcast podcast) {
        ArrayList<Track> episodes = podcast.getTracks();
        for (int idx1 = 0; idx1 < episodes.size() - 1; idx1++) {
            for (int idx2 = idx1 + 1; idx2 < episodes.size(); idx2++) {
                if (episodes.get(idx1).getName().equals(episodes.get(idx2).getName())) {
                    return false;
                }
            }
        }
        podcasts.add(podcast);
        library.getPodcasts().add(podcast);
        for (User user : getSubscribers()) {
            user.update(Notification.NotifType.PODCAST_NOTIF, this);
        }
        return true;
    }

    /**
     * Attempts to remove the given podcast from the given library
     * @param library from which to remove the given podcast
     * @param podcast to attempt to remove from the given library
     * @return false if the given podcast is not in the library, or true if the removal succeeded
     */
    public boolean removePodcast(final Library library, final Podcast podcast) {
        for (User user : library.getUsers()) {
            if (user.getPlayer().getCompilation() == podcast) {
                return false;
            } else if (podcast.contains(user.getPlayer().getTrack())) {
                return false;
            }
        }
        podcasts.remove(podcast);
        library.getPodcasts().remove(podcast);
        return true;
    }

    /**
     * Fetches this hosts' announcement with the given name
     * @param name of the announcement to fetch
     * @return the announcement with the given name, or null if it does not exist
     */
    public Announcement getAnnouncement(final String name) {
        for (Announcement announcement : announcements) {
            if (announcement.getName().equals(name)) {
                return announcement;
            }
        }
        return null;
    }

    /**
     * Attempts to add the announcement event to the announcements list
     * @param announcement to attempt to add to the announcements list
     * @return false if the announcement already exists or true if the addition was successful
     */
    public boolean addAnnouncement(final Announcement announcement) {
        if (getAnnouncement(announcement.getName()) != null) {
            return false;
        }
        announcements.add(announcement);
        for (User user : getSubscribers()) {
            user.update(Notification.NotifType.ANNOUNCE_NOTIF, this);
        }
        return true;
    }

    /**
     * Attempts to remove the announcement with the given name from the announcements list
     * @param name of the announcement to attempt to remove from the announcements list
     * @return false if the announcement does not exist or true if the removal was successful
     */
    public boolean removeAnnouncement(final String name) {
        Announcement announcement = getAnnouncement(name);
        if (announcement == null) {
            return false;
        }
        announcements.remove(announcement);
        return true;
    }
}
