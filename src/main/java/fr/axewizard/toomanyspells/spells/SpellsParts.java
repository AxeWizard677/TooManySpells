package fr.axewizard.toomanyspells.spells;

import org.bukkit.Material;

public enum SpellsParts {
    EXPLOSION (Material.GUNPOWDER),
    THROW_ENTITY (Material.TNT),
    THROW_FALLING_BLOCK (Material.DIRT),
    LASER_BEAM (Material.FIREWORK_ROCKET),
    FIRE_BEAM (Material.FLINT_AND_STEEL),
    FIRE_AOE (Material.FIRE_CHARGE),
    EXPLOSIVE_SNOWBALL (Material.SNOWBALL);

    private Material material;

    public Material getMaterial() {
        return material;
    }

    SpellsParts(Material material) {
        this.material = material;
    }
}
