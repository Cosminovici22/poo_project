package environment.party;

import environment.Library;
import environment.page.PageGeneratorStrategy;
import lombok.Getter;

@Getter
public abstract class Party {
    private String name;
    private int age;
    private String city;

    public Party(final String name, final int age, final String city) {
        this.name = name;
        this.age = age;
        this.city = city;
    }

    /**
     * Attempts to add this party to the given library.
     * @param library to which to add this party
     * @return true if the addition was successful, or false otherwise
     */
    public abstract boolean add(Library library);

    /**
     * Attempt to delete this party from the given library.
     * @param library from which to delete this party
     * @param timestamp at which the deletion occurs
     * @return true if the deletion was successful, or false otherwise
     */
    public abstract boolean delete(Library library, int timestamp);

    /**
     * Return the page generator object that corresponds to this party
     * @return the page generator object that corresponds to this party
     */
    public abstract PageGeneratorStrategy getPageGenerator();

    /**
     * Used for displaying messages.
     * @return a string representing the type of this party's inheritor
     */
    public abstract String getType();
}
