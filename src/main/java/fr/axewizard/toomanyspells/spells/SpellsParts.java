package fr.axewizard.toomanyspells.spells;

import org.bukkit.Material;

import java.util.*;
import java.util.function.Function;

public enum SpellsParts {
    EXPLOSION (Material.GUNPOWDER,data -> data.explosion(data)),
    THROW_ENTITY (getThowableEntity()),
    THROW_FALLING_BLOCK (getThrowableBlocks()),
    LASER_BEAM (Material.FIREWORK_ROCKET),
    FIRE_BEAM (Material.FLINT_AND_STEEL),
    FIRE_AOE (Material.FIRE_CHARGE),
    SNOWBALL (Material.SNOWBALL);

    private List<Material> material;
    private Function<basicProjectile, projectileEndData> f;

    public List<Material> getMaterial() {
        return Collections.unmodifiableList(material);
    }

    SpellsParts(Function<basicProjectile, projectileEndData> f, List<Material> material) {
        this.material = material;
        this.f = f;
    }

    SpellsParts(Function<basicProjectile, projectileEndData> f, Material material) {
        this(f,List.of(material));
    }

    SpellsParts(Function<basicProjectile, projectileEndData> f,Material... material) {
        this(f,Arrays.asList(material));
    }

    private static List<Material> getThowableEntity() {
        List<Material> ThrowableEntity = Arrays.stream(Material.values()).filter(material -> material.toString().endsWith("_spawn_egg")).toList();
        ThrowableEntity.add(Material.TNT);
        return ThrowableEntity;
    }

    private static List<Material> getThrowableBlocks() {
        List<Material> ThrowableBlocks = Arrays.stream(Material.values()).filter(Material::isBlock).toList();
        ThrowableBlocks.remove(Material.TNT);
        return ThrowableBlocks;
    }
}
