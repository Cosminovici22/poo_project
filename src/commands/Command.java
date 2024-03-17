package commands;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import commands.admin.*;
import commands.statistics.Wrapped;
import commands.party.artist.*;
import commands.party.host.*;
import commands.party.user.*;
import commands.output.Output;
import commands.party.user.merch.*;
import commands.party.user.notifications.GetNotifications;
import commands.party.user.notifications.Subscribe;
import commands.party.user.page.*;
import commands.party.user.player.*;
import commands.party.user.playlist.*;
import commands.party.user.premium.*;
import commands.party.user.searchbar.*;
import commands.statistics.*;
import environment.Library;
import lombok.Getter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "command",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Search.class, name = "search"),
        @JsonSubTypes.Type(value = Select.class, name = "select"),
        @JsonSubTypes.Type(value = Load.class, name = "load"),
        @JsonSubTypes.Type(value = PlayPause.class, name = "playPause"),
        @JsonSubTypes.Type(value = Repeat.class, name = "repeat"),
        @JsonSubTypes.Type(value = Shuffle.class, name = "shuffle"),
        @JsonSubTypes.Type(value = Forward.class, name = "forward"),
        @JsonSubTypes.Type(value = Backward.class, name = "backward"),
        @JsonSubTypes.Type(value = Like.class, name = "like"),
        @JsonSubTypes.Type(value = Next.class, name = "next"),
        @JsonSubTypes.Type(value = Prev.class, name = "prev"),
        @JsonSubTypes.Type(value = AddRemoveInPlaylist.class, name = "addRemoveInPlaylist"),
        @JsonSubTypes.Type(value = Status.class, name = "status"),
        @JsonSubTypes.Type(value = CreatePlaylist.class, name = "createPlaylist"),
        @JsonSubTypes.Type(value = SwitchVisibility.class, name = "switchVisibility"),
        @JsonSubTypes.Type(value = Follow.class, name = "follow"),
        @JsonSubTypes.Type(value = ShowPlaylists.class, name = "showPlaylists"),
        @JsonSubTypes.Type(value = ShowPreferredSongs.class, name = "showPreferredSongs"),
        @JsonSubTypes.Type(value = GetTop5Songs.class, name = "getTop5Songs"),
        @JsonSubTypes.Type(value = GetTop5Playlists.class, name = "getTop5Playlists"),

        @JsonSubTypes.Type(value = ChangePage.class, name = "changePage"),
        @JsonSubTypes.Type(value = PrintCurrentPage.class, name = "printCurrentPage"),
        @JsonSubTypes.Type(value = AddUser.class, name = "addUser"),
        @JsonSubTypes.Type(value = DeleteUser.class, name = "deleteUser"),
        @JsonSubTypes.Type(value = ShowAlbums.class, name = "showAlbums"),
        @JsonSubTypes.Type(value = ShowPodcasts.class, name = "showPodcasts"),
        @JsonSubTypes.Type(value = AddAlbum.class, name = "addAlbum"),
        @JsonSubTypes.Type(value = RemoveAlbum.class, name = "removeAlbum"),
        @JsonSubTypes.Type(value = AddEvent.class, name = "addEvent"),
        @JsonSubTypes.Type(value = RemoveEvent.class, name = "removeEvent"),
        @JsonSubTypes.Type(value = AddMerch.class, name = "addMerch"),
        @JsonSubTypes.Type(value = AddPodcast.class, name = "addPodcast"),
        @JsonSubTypes.Type(value = RemovePodcast.class, name = "removePodcast"),
        @JsonSubTypes.Type(value = AddAnnouncement.class, name = "addAnnouncement"),
        @JsonSubTypes.Type(value = RemoveAnnouncement.class, name = "removeAnnouncement"),
        @JsonSubTypes.Type(value = SwitchConnectionStatus.class, name = "switchConnectionStatus"),
        @JsonSubTypes.Type(value = GetTop5Albums.class, name = "getTop5Albums"),
        @JsonSubTypes.Type(value = GetTop5Artists.class, name = "getTop5Artists"),
        @JsonSubTypes.Type(value = GetAllUsers.class, name = "getAllUsers"),
        @JsonSubTypes.Type(value = GetOnlineUsers.class, name = "getOnlineUsers"),

        @JsonSubTypes.Type(value = Wrapped.class, name = "wrapped"),
        @JsonSubTypes.Type(value = BuyMerch.class, name = "buyMerch"),
        @JsonSubTypes.Type(value = SeeMerch.class, name = "seeMerch"),
        @JsonSubTypes.Type(value = BuyPremium.class, name = "buyPremium"),
        @JsonSubTypes.Type(value = CancelPremium.class, name = "cancelPremium"),
        @JsonSubTypes.Type(value = AdBreak.class, name = "adBreak"),
        @JsonSubTypes.Type(value = Subscribe.class, name = "subscribe"),
        @JsonSubTypes.Type(value = GetNotifications.class, name = "getNotifications"),
        @JsonSubTypes.Type(value = UpdateRecommendations.class, name = "updateRecommendations"),
        @JsonSubTypes.Type(value = LoadRecommendations.class, name = "loadRecommendations"),
        @JsonSubTypes.Type(value = NextPage.class, name = "nextPage"),
        @JsonSubTypes.Type(value = PreviousPage.class, name = "previousPage"),
})
@Getter
public abstract class Command {
    private String command;
    private String username;
    private int timestamp;

    /**
     * Executes respective command.
     *
     * @param library of all the songs, playlists, podcasts and users
     * @return commands.output of respective command
     */
    public abstract Output execute(Library library);
}
