package commands.party.user.page;

import commands.Command;
import commands.output.MessageOutput;
import commands.output.Output;
import environment.Library;
import environment.party.user.Player;
import environment.party.user.User;
import lombok.Setter;

@Setter
public final class UpdateRecommendations extends Command {
    private String recommendationType;

    @Override
    public Output execute(final Library library) {
        User currUser = library.getUser(getUsername());

        if (currUser == null) {
            if (library.getArtist(getUsername()) != null
                    || library.getHost(getUsername()) != null) {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        getUsername() + " is not a normal user.");
            } else {
                return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                        "The username " + getUsername() + " doesn't exist.");
            }
        } else {
            Player player = currUser.getPlayer();
            player.update(currUser, getTimestamp());

            switch (recommendationType) {
                case "random_song":
                    if (currUser.generateRandomSong(player.getTrack(), player.getElapsedTime())) {
                        currUser.setLatestRecommendation(currUser.getRandomSong());
                        return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                                "The recommendations for user " + getUsername()
                                        + " have been updated successfully.");
                    } else {
                        return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                                "No new recommendations were found");
                    }
                case "random_playlist":
                    if (currUser.generateRandomPlaylist(player.getTrack())) {
                        currUser.setLatestRecommendation(currUser.getRandomPlaylist());
                        return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                                "The recommendations for user " + getUsername()
                                        + " have been updated successfully.");
                    } else {
                        return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                                "No new recommendations were found");
                    }
                case "fans_playlist":
                    if (currUser.generateFansPlaylist(player.getTrack())) {
                        currUser.setLatestRecommendation(currUser.getFansPlaylist());
                        return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                                "The recommendations for user " + getUsername()
                                        + " have been updated successfully.");
                    } else {
                        return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                                "No new recommendations were found");
                    }
                default:
                    return new MessageOutput(getCommand(), getTimestamp(), getUsername(),
                            "No new recommendations were found");
            }
        }
    }
}
