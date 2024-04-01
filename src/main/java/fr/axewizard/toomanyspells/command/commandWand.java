package fr.axewizard.toomanyspells.command;

import fr.axewizard.toomanyspells.TooManySpells;
import fr.axewizard.toomanyspells.items.wand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class commandWand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player player){
            player.getInventory().addItem(new wand().getItemWand());
            player.sendMessage(TooManySpells.getPluginPrefix().append(Component.text("You have received a wand !").color(NamedTextColor.WHITE)));
            player.playSound(player.getLocation(), Sound.BLOCK_LARGE_AMETHYST_BUD_BREAK, 10, 1);
        }
        return false;
    }
}
