package environment.party.observer;

import environment.party.user.Notification;

public interface ObserverUser {
    /**
     * Records a new notification in this user's notification history.
     * @param notifType of the new notification
     * @param subjectParty who caused the notification
     */
    void update(Notification.NotifType notifType, SubjectParty subjectParty);
}
