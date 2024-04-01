package fr.axewizard.toomanyspells.items;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class wand {
    private ItemStack itemWand;
    public wand(){
        itemWand = new ItemStack(randomizeMaterial());
        ItemMeta itemWandMeta = itemWand.getItemMeta();
        itemWandMeta.displayName(randomizeName());
        itemWandMeta.lore(List.of(
                Component.text("Right Click pour lancer un sort")
                        .color(NamedTextColor.WHITE)
                        .decoration(TextDecoration.ITALIC, false)
        ));
        itemWandMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemWandMeta.addEnchant(Enchantment.DURABILITY,1,true);
        itemWandMeta.getPersistentDataContainer().set(keysPersistantData.WAND, PersistentDataType.BOOLEAN,true);
        itemWand.setItemMeta(itemWandMeta);
    }

    public ItemStack getItemWand() {
        return itemWand;

}

    public static Material randomizeMaterial() {
        List<Material> materials = Arrays.asList(Material.AMETHYST_SHARD,Material.STICK,Material.BLAZE_ROD,Material.SPECTRAL_ARROW,Material.ECHO_SHARD,Material.BONE,Material.FEATHER);
        Random rand = new Random();
        int index = rand.nextInt(materials.size());
        return materials.get(index);
    }
    public Component randomizeName() {
        List<String> objetsMagiques = Arrays.asList("Baton", "Baguette", "Sceptre", "Canne", "Perche", "Gourdin",
                "Trique", "Crosse", "Javelot", "Girouette", "Pilon", "Parapluie", "Pelle", "Branchette", "Barre",
                "Bagueton", "Queue (de billard)", "Fleau", "Faisceau", "Bougie", "Manche", "Fourchette", "Regle",
                "Latte", "Pagaie");

        List<String> materiaux = Arrays.asList("en Argent", "en Or", "en Cuivre", "en Bronze", "en Fer", "en Ebene",
                "en Chene", "en Hetre", "en Acajou", "en Quartz", "en Saphir", "en Emeraude", "en Diamant", "en Rubis",
                "en Cristal", "en Mithril", "en Adamantium", "en Orichalque", "en Os", "en Ivoire", "en Bois de Rose",
                "en Arbre de Vie", "en Obsidienne", "en Nacre", "en Opale", "en Ambre", "en Amethyste", "en Jade",
                "en Onyx", "en Pierre de Lune", "en Tourmaline", "en Topaze", "en Malachite", "en Peridot", "en Agate",
                "en Fluorite", "en Turquoise", "en Lapis-lazuli", "en Corail", "en Coquillage", "en Os de Dragon",
                "en Plume de Phenix", "en Ecaille de Licorne", "en Cuir de Dragon", "en Peau de Basilic",
                "en Plume d'Ange", "en Ecaille de Dragon");

        List<String> adjectifs = Arrays.asList("Puissant", "Mystique", "Envoutant", "Magistral", "Luminescent",
                "Etincelant", "Brillant", "Radieux", "Protecteur", "Securisant", "Energetique", "Bouclier", "Ancien",
                "Antique", "Revere", "Legendaire", "Celtique", "Nordique", "Oriental", "Egyptien", "Feerique",
                "Merveilleux", "Ensorcele", "Divin", "Surnaturel", "Incroyable", "Fantastique", "Heroique", "Imposant",
                "Intriguant", "Miraculeux", "Prodigieux", "Sublime", "Surprenant", "Transcendant", "Visionnaire",
                "Alchimique", "Enchante", "Magique", "Mysterieux", "Mythique", "Surnaturel", "Insondable",
                "Incommensurable", "Inalterable", "Intemporel");



        String randomObjetMagique = objetsMagiques.get(new Random().nextInt(objetsMagiques.size()));
        String randomMateriel = materiaux.get(new Random().nextInt(materiaux.size()));
        String randomAdjective = adjectifs.get(new Random().nextInt(adjectifs.size()));

        // Define a list of valid colors
        List<NamedTextColor> validColors = Arrays.asList(
                NamedTextColor.RED,
                NamedTextColor.GREEN,
                NamedTextColor.BLUE,
                NamedTextColor.YELLOW,
                NamedTextColor.AQUA,
                NamedTextColor.LIGHT_PURPLE,
                NamedTextColor.GOLD,
                NamedTextColor.WHITE,
                NamedTextColor.DARK_PURPLE,
                NamedTextColor.DARK_AQUA,
                NamedTextColor.DARK_RED,
                NamedTextColor.DARK_GREEN
        );
        NamedTextColor randomColor = validColors.get(new Random().nextInt(validColors.size()));

        return Component.text(randomObjetMagique+ " " + randomMateriel + " " + randomAdjective)
                .color(randomColor)
                .decoration(TextDecoration.BOLD, true)
                .decoration(TextDecoration.ITALIC, false);
    }
}