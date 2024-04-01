package fr.axewizard.toomanyspells.listener;

import fr.axewizard.toomanyspells.TooManySpells;
import fr.axewizard.toomanyspells.items.keysPersistantData;
import fr.axewizard.toomanyspells.spells.basicProjectile;
import fr.axewizard.toomanyspells.spells.projectileEndData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

public class playerListener implements Listener {

    private final HashMap<String, Long> cooldowns = new HashMap<>();
    private final basicProjectile basicProjectile = new basicProjectile();
    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Check if the item in hand has the wand key in its persistent data container
        if (event.getAction().name().contains("RIGHT_CLICK") && item != null && item.hasItemMeta()
                && !player.isSneaking() && item.getItemMeta().getPersistentDataContainer().has(keysPersistantData.WAND)) {
            String playerUUID = player.getUniqueId().toString();
            long currentTime = System.currentTimeMillis();

            // Check if the player is in cooldown for this wand type
            if (!cooldowns.containsKey(playerUUID) || cooldowns.get(playerUUID) < currentTime) {
                projectileEndData data =
                        new projectileEndData(player.getEyeLocation(), player.getEyeLocation().getDirection(), false);
                //Laser beam then explosive snowball
                //new basicProjectile().explosiveSnowball(new basicProjectile().laserBeam(data));
                //Laser beam then fireAoe
                //new basicProjectile().fireAOE(new basicProjectile().laserBeam(data));
                //Laser beam then fireBeam
                //new basicProjectile().laserBeam((new basicProjectile().laserBeam(data)));
                //Laser beam then explosion
                //new basicProjectile().explosion(new basicProjectile().laserBeam(data));
                //Fire beam
                //new basicProjectile().fireBeam(data);
                // Set cooldown for the player and wand type
                //Laser then fallingBlock
                //new basicProjectile().fallingBlock(new basicProjectile().laserBeam(data));
                //Laser then entity
                //new basicProjectile().entity(new basicProjectile().laserBeam(data), EntityType.PRIMED_TNT);
                Inventory inventory = getSpellInventory(player, item);
                for (int i = 0; i < inventory.getSize(); i++) {
                    if (inventory.getItem(i) == null) {
                        break;
                    }
                    if (inventory.getItem(i).getType() == Material.SNOWBALL) {
                        data = basicProjectile.explosiveSnowball(data);
                    } else if (inventory.getItem(i).getType() == Material.FIREWORK_ROCKET) {
                        data = basicProjectile.laserBeam(data);
                    } else if (inventory.getItem(i).getType() == Material.FLINT_AND_STEEL) {
                        data = basicProjectile.fireBeam(data);
                    } else if (inventory.getItem(i).getType() == Material.TNT) {
                        data = basicProjectile.entity(data, EntityType.PRIMED_TNT);
                    } else if (inventory.getItem(i).getType() == Material.GUNPOWDER) {
                        data = basicProjectile.explosion(data);
                    } else if (inventory.getItem(i).getType() == Material.FIRE_CHARGE) {
                        data = basicProjectile.fireAOE(data);
                    } else if (inventory.getItem(i).getType().isBlock()) {
                        data = basicProjectile.fallingBlock(data, inventory.getItem(i).getType());
                    }
                }
                    int cooldownDuration = 1; // 1 second cooldown
                    long cooldownTime = currentTime + (cooldownDuration * 1000);
                    cooldowns.put(playerUUID, cooldownTime);
                    //Set cooldown visually
                    player.setCooldown(item.getType(), cooldownDuration * 20);

            }
        }

        if (event.getAction().name().contains("RIGHT_CLICK") && player.isSneaking() &&
                item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(keysPersistantData.WAND)) {
            // Open a 9-slot inventory for the player
            // Open the inventory for the player
            player.openInventory(getSpellInventory(player, item));
        }
    }
    @EventHandler
    public void onSnowballHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile instanceof Snowball && projectile.getPersistentDataContainer().has(keysPersistantData.EXLPLOSIVE_SNOWBALL)) {
            // Create an explosion at the snowball's location when it hits something (entity or block)
            projectile.getWorld().createExplosion(projectile.getLocation(), 2.0f); // You can adjust the explosion power as needed
        }
    }


    private Inventory getSpellInventory(Player player, ItemStack wand) {
        Inventory inventory;
        // Check if the wand has a stored inventory
        if (wand.hasItemMeta() && wand.getItemMeta().getPersistentDataContainer().has(keysPersistantData.SPELLS_INVENTORY)) {
            // Retrieve the stored inventory
            inventory = Bukkit.createInventory(null, 9, "Spells");
            byte[] serializedInventory = wand.getItemMeta().getPersistentDataContainer().get(keysPersistantData.SPELLS_INVENTORY, PersistentDataType.BYTE_ARRAY);
            if (serializedInventory != null) {
                inventory.setContents(deserializeInventory(serializedInventory).getContents());
            }
        } else {
            // Create a new inventory
            inventory = Bukkit.createInventory(null, 9, "Spells");
        }


        return inventory;
    }

    @EventHandler
    public void onPlayerCloseInventory(InventoryCloseEvent event) {
        if (event.getView().title().toString().contains("Spells")
                && event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(keysPersistantData.WAND)){
            storeInventoryInItem(event.getInventory(), event.getPlayer().getInventory().getItemInMainHand());

        }
    }


    private void storeInventoryInItem(Inventory inventoryToStore, ItemStack item) {
        byte[] serializedInventory = serializeInventory(inventoryToStore);
        if (serializedInventory != null) {
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.getPersistentDataContainer().set(keysPersistantData.SPELLS_INVENTORY, PersistentDataType.BYTE_ARRAY, serializedInventory);
            item.setItemMeta(itemMeta);
        }
    }
    @EventHandler
    public void InventoryClickEvent(InventoryClickEvent event){
        //prevent a wand to be put inside a spell inventory
      if (event.getClickedInventory() != null && event.getView().getTitle().contains("Spells")
              && event.getCurrentItem() != null && event.getCurrentItem().hasItemMeta()
              && event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(keysPersistantData.WAND)){
          event.getViewers().forEach(viewer -> viewer.sendMessage(TooManySpells.getPluginPrefix()
                                                  .append(Component.text("You can't put a wand inside a spell inventory !")
                                                            .color(NamedTextColor.RED))));
          event.setCancelled(true);
      }
    }

    private byte[] serializeInventory(Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Serialize the inventory to a byte array
            dataOutput.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to serialize inventory", e);
            return null;
        }
    }

    private Inventory deserializeInventory(byte[] serializedInventory) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedInventory);
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            int size = dataInput.readInt();
            Inventory inventory = Bukkit.getServer().createInventory(null, size);

            // Deserialize inventory contents
            for (int i = 0; i < size; i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return inventory;
        } catch (IOException | ClassNotFoundException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to deserialize inventory", e);
            return null;
        }
}
}

