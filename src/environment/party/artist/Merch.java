package environment.party.artist;

import environment.party.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public final class Merch {
    private String name;
    private String description;
    private int price;

    public Merch(final String name, final String description, final int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    /**
     * Adds this piece of merchandise from a given artist to a given user's owned merch list and
     * calculates adds its price to the artist's total merch revenue.
     * @param user who wishes to buy this piece of merch
     * @param artist to whom this merch belongs to
     */
    public void buy(final User user, final Artist artist) {
        user.getOwnedMerch().add(this);
        artist.addMerchRevenue(price);
    }
}
