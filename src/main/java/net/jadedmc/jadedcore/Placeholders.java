package net.jadedmc.jadedcore;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.jadedmc.jadedcore.features.player.JadedPlayer;
import net.jadedmc.jadedcore.utils.chat.ChatUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * This class will be registered through the register-method in the
 * plugins onEnable-method.
 */
class Placeholders extends PlaceholderExpansion {
    private final JadedCorePlugin plugin;

    /**
     * Since we register the expansion inside our own plugin, we
     * can simply use this method here to get an instance of our
     * plugin.
     *
     * @param plugin
     *        The instance of our plugin.
     */
    public Placeholders(final JadedCorePlugin plugin){
        this.plugin = plugin;
    }

    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }

    /**
     * Because this is a internal class, this check is not needed
     * and we can simply return {@code true}
     *
     * @return Always true since it's an internal class.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     * <br>For convienience do we return the author from the plugin.yml
     *
     * @return The name of the author as a String.
     */
    @Override
    public @NotNull String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br>This must be unique and can not contain % or _
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public @NotNull String getIdentifier(){
        return "jadedcore";
    }

    /**
     * This is the version of the expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * For convenience do we return the version from the plugin.yml
     *
     * @return The version as a String.
     */
    @Override
    public @NotNull String getVersion(){
        return plugin.getDescription().getVersion();
    }


    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        // Overall
        if(identifier.contains("top_ap_name")) {
            int place = Integer.parseInt(identifier.replaceAll("\\D+","")) - 1;
            ArrayList<String> temp = new ArrayList<>(plugin.leaderboardManager().getAchievementPointsLeaderboard().keySet());
            if(temp.size() < place + 1) {
                return "---";
            }

            return temp.get(place);
        }

        if(identifier.contains("top_ap_value")) {
            int place = Integer.parseInt(identifier.replaceAll("\\D+","")) - 1;
            ArrayList<Integer> temp = new ArrayList<>(plugin.leaderboardManager().getAchievementPointsLeaderboard().values());

            if(temp.size() < place + 1) {
                return "---";
            }

            return temp.get(place) + "";
        }

        JadedPlayer jadedPlayer = plugin.jadedPlayerManager().getPlayer(player);

        if(jadedPlayer == null) {
            return "";
        }

        if(identifier.contains("rank_displayname_legacy")) {
            return ChatUtils.toLegacy(jadedPlayer.getRank().getDisplayName());
        }

        if(identifier.contains("rank_chat_prefix_legacy")) {
            return ChatUtils.toLegacy(jadedPlayer.getRank().getChatPrefix());
        }

        if(identifier.contains("rank_chat_color_legacy")) {
            return ChatUtils.toLegacy(jadedPlayer.getRank().getChatColor());
        }

        if(identifier.contains("rank_displayname")) {
            return jadedPlayer.getRank().getDisplayName();
        }

        if(identifier.contains("rank_chat_prefix")) {
            return jadedPlayer.getRank().getChatPrefix();
        }

        if(identifier.contains("rank_chat_color")) {
            return jadedPlayer.getRank().getChatColor();
        }

        if(identifier.contains("rank_color")) {
            return jadedPlayer.getRank().getChatColor();
        }

        return null;
    }
}