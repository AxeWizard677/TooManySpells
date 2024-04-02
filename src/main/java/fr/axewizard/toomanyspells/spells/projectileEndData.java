package fr.axewizard.toomanyspells.spells;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class projectileEndData {
    public Location  location = null;
    public Vector direction = null;
    public boolean hitSomething = false;

    public projectileEndData(Location location, Vector direction, boolean hitSomething) {
        this.location = location;
        this.direction = direction;
        this.hitSomething = false;
    }

    public projectileEndData(Location location, Vector direction) {
        this.location = location;
        this.direction = direction;
    }

}
