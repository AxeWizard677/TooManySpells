package fr.axewizard.toomanyspells;

import fr.axewizard.toomanyspells.command.commandWand;
import fr.axewizard.toomanyspells.listener.playerListener;
import fr.axewizard.toomanyspells.spells.basicProjectile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;

public final class TooManySpells extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getConsoleSender().sendMessage(Component.text("[TooManySpells] ").append(Component.text("Successfully started !")).color(NamedTextColor.LIGHT_PURPLE));
        getCommand("wand").setExecutor(new commandWand());
        getServer().getPluginManager().registerEvents(new playerListener(),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getConsoleSender().sendMessage(Component.text("[TooManySpells] ").append(Component.text("Successfully stopped !")).color(NamedTextColor.LIGHT_PURPLE));
    }

    public static TooManySpells getInstance(){
        return TooManySpells.getPlugin(TooManySpells.class);
    }

    public static Component getPluginPrefix(){
        return net.kyori.adventure.text.Component.text("[TooManySpells] ").color(NamedTextColor.LIGHT_PURPLE);
    }
}
