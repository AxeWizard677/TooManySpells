package fr.axewizard.toomanyspells.spells;

import fr.axewizard.toomanyspells.items.keysPersistantData;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import org.bukkit.Particle;
import org.bukkit.Location;

import java.util.Objects;
import java.util.function.Predicate;

public class basicProjectile {

    projectileEndData data;

    basicProjectile(projectileEndData data) {
        this.data = data;
    }

    public void visibleBeam(Vector direction, Location location, double length, Particle particle) {
        double step = 1;
        // Boucle de création des particules du laser
        for (double waypoint = 1; waypoint < length; waypoint += step) {
            location = location.add(direction.multiply(step)); // Mise à jour de la position du laser
            // Création d'une particule à la position actuelle du laser
            location.getWorld().spawnParticle(particle, location, 1, 0, 0, 0, 0);
        }
    }

    public static Vector calculateVectorReflection(Vector blockFace,Vector projectileDirection) {

        // Calcul du vecteur réfléchi selon la réflexion spéculaire
        Vector reflectedVector = projectileDirection.subtract(blockFace.multiply(2 * projectileDirection.dot(blockFace)));

        return reflectedVector.normalize();
    }
 //LASER BEAM WITH LENGTH
    public projectileEndData laserBeam(Location location, Vector direction, double length) {
        RayTraceResult rayTraceResult = location.getWorld().rayTraceBlocks(location, direction, length, FluidCollisionMode.NEVER, true);
        if (rayTraceResult == null) {
            visibleBeam(direction, location, length,Particle.FIREWORKS_SPARK);
            return new projectileEndData(location.add(direction.multiply(length)), direction,false);
        }
        double traveledDistance = location.distance(rayTraceResult.getHitPosition().toLocation(location.getWorld()));
        visibleBeam(direction, location, traveledDistance,Particle.FIREWORKS_SPARK);

        Vector hitDirection = Objects.requireNonNull(rayTraceResult.getHitBlockFace()).getDirection();
        Vector newDirection = calculateVectorReflection(hitDirection, direction);
        return new projectileEndData(rayTraceResult.getHitPosition().toLocation(location.getWorld()), newDirection,true);
    }
//LASER BEAM WITHOUT LENGTH
    public projectileEndData laserBeam(Location location, Vector direction) {
        return laserBeam(location, direction, 20);
    }

    public projectileEndData laserBeam(projectileEndData data) {
        return laserBeam(data.location, data.direction, 20);
    }

    public projectileEndData explosiveSnowball(Location location, Vector direction) {
        Snowball snowball = (Snowball) location.getWorld().spawnEntity(location, EntityType.SNOWBALL);
        snowball.setVelocity(direction);
        snowball.getPersistentDataContainer().set(keysPersistantData.EXLPLOSIVE_SNOWBALL,PersistentDataType.BOOLEAN,true);
        return new projectileEndData(location, direction, false);
    }

    public projectileEndData explosiveSnowball(projectileEndData data) {
        return explosiveSnowball(data.location, data.direction);
    }

    public projectileEndData fireBeam(Location location, Vector direction) {
        double length = 10;
        RayTraceResult rayTraceResult = location.getWorld().rayTraceEntities(location, direction, length);
        if(rayTraceResult != null &&
                rayTraceResult.getHitEntity() instanceof Player &&
                rayTraceResult.getHitEntity().getLocation().distance(location) > 1.6200000 &&
                rayTraceResult.getHitEntity().getLocation().distance(location) < 1.6200001) {
            rayTraceResult = location.getWorld().rayTraceEntities(location.add(direction.multiply(1)), direction, length);
        }
        if (rayTraceResult == null) {
            visibleBeam(direction, location, length, Particle.FLAME);
            return new projectileEndData(location.add(direction.multiply(length)), direction, false);
        }
        double traveledDistance = location.distance(rayTraceResult.getHitPosition().toLocation(location.getWorld()));
        visibleBeam(direction, location, traveledDistance, Particle.FLAME);
        rayTraceResult.getHitEntity().setFireTicks(200);
        return new projectileEndData(location, direction, false);
    }
    public projectileEndData fireBeam(projectileEndData data) {
        return fireBeam(data.location, data.direction);
    }

    public projectileEndData fireAOE(Location location, Vector direction) {
        double radius = 2;
        location.getWorld().spawnParticle(Particle.FLAME, location, 100, radius, radius, radius, 0);
        location.getWorld().getNearbyEntities(location, radius, radius, radius).forEach(entity -> {
            if (entity instanceof Animals || entity instanceof Monster || entity instanceof Player) {
                entity.setFireTicks(100);
            }
        });

        return new projectileEndData(location, direction, false);
    }
    public projectileEndData fireAOE(projectileEndData data) {
        return fireAOE(data.location, data.direction);
    }

    public projectileEndData explosion(Location location,Vector direction ,Float power) {
        location.getWorld().createExplosion(location, power);
        return new projectileEndData(location, direction, true);
    }
    public projectileEndData explosion(Location location, Vector direction) {
        return explosion(location, direction, 2.0f);
    }

    public projectileEndData explosion(projectileEndData data) {
        return explosion(data.location, data.direction);
    }

    public projectileEndData fallingBlock(Location location, Vector direction, Material material) {
        FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(location, material, (byte) 0);
        fallingBlock.setVelocity(direction);
        fallingBlock.setDropItem(false);
        fallingBlock.setHurtEntities(true);
        fallingBlock.setPersistent(false);
        return new projectileEndData(location, direction, false);
    }

    public projectileEndData fallingBlock(projectileEndData data) {
        return fallingBlock(data.location, data.direction, Material.DIRT);
    }

    public projectileEndData fallingBlock(projectileEndData data, Material material) {
        return fallingBlock(data.location, data.direction, material);
    }

    public projectileEndData entity(Location location, Vector direction, EntityType entityType) {
        Entity entity = location.getWorld().spawnEntity(location, entityType);
        entity.setVelocity(direction);
        return new projectileEndData(location, direction, false);
    }

    public projectileEndData entity(projectileEndData data) {
        return entity(data.location, data.direction, EntityType.COW);
    }

    public projectileEndData entity(projectileEndData data, EntityType entityType) {
        return entity(data.location, data.direction, entityType);
    }
}

