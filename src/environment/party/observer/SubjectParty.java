package environment.party.observer;

import environment.party.user.User;

public interface SubjectParty {
    /**
     * Add/remove the given user as a subscriber from this subjectParty
     * @param user to add/remove as a subscriber
     * @return true if a new user was added or false if the given user was removed
     */
    boolean addRemoveSubscriber(User user);
}
