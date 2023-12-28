package net.jadedmc.jadedcore.features.games;

import com.cryptomorin.xseries.XMaterial;
import me.clip.placeholderapi.PlaceholderAPI;
import net.jadedmc.jadedcore.JadedAPI;
import net.jadedmc.jadedutils.chat.ChatUtils;
import net.jadedmc.jadedutils.gui.CustomGUI;
import net.jadedmc.jadedutils.items.ItemBuilder;
import net.jadedmc.jadedutils.items.SkullBuilder;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.ChatPaginator;

public class DuelsGUI extends CustomGUI {
    public DuelsGUI() {
        super(54, "Duels");

        addFiller(1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53);

        ItemStack comingSoon = new ItemBuilder(XMaterial.GRAY_DYE.parseItem())
                .setDisplayName("&c&lComing Soon")
                .build();

        setItem(22, comingSoon);

        addGame(20, Game.TOURNAMENTS);
        addGame(24, Game.MODERN_DUELS);

        ItemStack back = new SkullBuilder("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=")
                .asItemBuilder()
                .setDisplayName("&cBack")
                .build();
        setItem(0, back, (p, a) -> new GamesGUI().open(p));
    }

    private void addGame(int slot, Game game) {
        setItem(slot, getGameIcon(game), (p, a) -> JadedAPI.sendBungeecordMessage(p, "BungeeCord", "Connect", game.getServer()));
    }

    private ItemStack getGameIcon(Game game) {
        XMaterial material = game.getIconMaterial();

        ItemBuilder builder = new ItemBuilder(material)
                .setDisplayName("&a&l" + game.getName())
                .addLore("&8" + game.getType())
                .addLore("")
                .addLore(ChatPaginator.wordWrap(game.getDescription(), 25), "&7")
                .addLore("")
                .addLore("&a▸ Click to Connect")
                .addLore(ChatUtils.translateLegacy(PlaceholderAPI.setPlaceholders(Bukkit.getOnlinePlayers().iterator().next(), "&7Join %bungee_" + game.getServer() + "% others playing!")))
                .addFlag(ItemFlag.HIDE_ATTRIBUTES);
        return builder.build();
    }
}