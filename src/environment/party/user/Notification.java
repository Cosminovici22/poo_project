package environment.party.user;

import environment.party.Party;
import lombok.Getter;

@Getter
public class Notification {
    private NotifType notifType;
    private Party party;

    public Notification(final NotifType notifType, final Party party) {
        this.notifType = notifType;
        this.party = party;
    }

    public enum NotifType {
        EVENT_NOTIF,
        ALBUM_NOTIF,
        PODCAST_NOTIF,
        MERCH_NOTIF,
        ANNOUNCE_NOTIF,
    }
}
