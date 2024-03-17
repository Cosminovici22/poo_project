package commands.output;

import environment.party.user.Notification;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class GetNotificationsOutput extends UserOutput {
    private ArrayList<DummyNotification> notifications;

    public GetNotificationsOutput(final String command, final Integer timestamp, final String user,
                                  final ArrayList<Notification> notifications) {
        super(command, timestamp, user);
        this.notifications = new ArrayList<>();
        for (Notification notification : notifications) {
            this.notifications.add(new DummyNotification(notification));
        }
        notifications.clear();
    }

    @Getter
    class DummyNotification {
        private String name;
        private String description;

        DummyNotification(final Notification notification) {
            switch (notification.getNotifType()) {
                case EVENT_NOTIF:
                    name = "New Event";
                    description = "New Event from " + notification.getParty().getName() + ".";
                    break;
                case ALBUM_NOTIF:
                    name = "New Album";
                    description = "New Album from " + notification.getParty().getName() + ".";
                    break;
                case PODCAST_NOTIF:
                    name = "New Podcast";
                    description = "New Podcast from " + notification.getParty().getName() + ".";
                    break;
                case MERCH_NOTIF:
                    name = "New Merchandise";
                    description = "New Merchandise from " + notification.getParty().getName() + ".";
                    break;
                case ANNOUNCE_NOTIF:
                    name = "New Announcement";
                    description = "New Announcement from " + notification.getParty().getName()
                            + ".";
                    break;
                default:
                    break;
            }
        }
    }
}
