package com.zp4rker.bukkitplayground.listeners;

import com.zp4rker.bukkitplayground.BukkitPlayground;
import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.world.EntitiesLoadEvent;

public class EntityListeners implements Listener {
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        if (!e.getLocation().getWorld().getName().equals(BukkitPlayground.WORLD_NAME)) return;

        if (!(e.getEntity() instanceof Mob mob)) return;
        Entity nmsEntity = ((CraftEntity) e.getEntity()).getHandle();
        if (!nmsEntity.getTags().contains("dumb")) return;

        mob.setAI(false);
        mob.setSilent(true);
    }

    @EventHandler
    public void onEntitiesLoad(EntitiesLoadEvent e) {
        if (!e.getWorld().getName().equals("chunk_copy")) return;

        for (org.bukkit.entity.Entity entity : e.getEntities()) {
            if (!(entity instanceof Mob mob)) continue;
            Entity nmsEntity = ((CraftEntity) entity).getHandle();
            if (!nmsEntity.getTags().contains("dumb")) continue;

            mob.setAI(false);
            mob.setSilent(true);
        }
    }
}
