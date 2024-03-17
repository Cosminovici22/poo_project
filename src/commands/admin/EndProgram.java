package commands.admin;

import commands.Command;
import commands.output.MapOutput;
import commands.output.Output;
import environment.Library;
import environment.party.artist.Artist;
import environment.party.user.User;
import lombok.Getter;

import java.util.HashMap;

public final class EndProgram extends Command {
    @Override
    public Output execute(final Library library) {
        final double premiumPrice = 1000000;
        final double roundNum = 100;

        for (User user : library.getUsers()) {
            if (user.isPremium()) {
                for (Artist artist : library.getArtists()) {
                    artist.addSongRevenue(user.getGeneratedRevenueFor(artist, premiumPrice));
                }
            }
        }

        HashMap<String, Revenue> result = new HashMap<>();
        for (Artist artist : library.getArtists()) {
            if (artist.isRelevant()) {
                Revenue revenue = new Revenue(
                        Math.round(artist.getSongRevenue() * roundNum) / roundNum,
                        Math.round(artist.getMerchRevenue() * roundNum) / roundNum,
                        artist.getRanking(),
                        artist.getMostProfitableSong()
                );
                result.put(artist.getName(), revenue);
            }
        }

        return new MapOutput("endProgram", null, null, result);
    }

    @Getter
    public static final class Revenue {
        private double songRevenue;
        private double merchRevenue;
        private int ranking;
        private String mostProfitableSong;

        public Revenue(final double songRevenue, final double merchRevenue, final int ranking,
                       final String mostProfitableSong) {
            this.songRevenue = songRevenue;
            this.merchRevenue = merchRevenue;
            this.ranking = ranking;
            this.mostProfitableSong = mostProfitableSong == null ? "N/A" : mostProfitableSong;
        }
    }
}
