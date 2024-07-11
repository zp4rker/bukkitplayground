package com.zp4rker.bukkitplayground.listeners;

import net.minecraft.world.entity.Entity;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntitySpawn implements Listener {
    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent e) {
        if (!e.getLocation().getWorld().getName().equals("chunk_copy")) return;

        if (!(e.getEntity() instanceof Mob mob)) return;
        Entity nmsEntity = ((CraftEntity) e.getEntity()).getHandle();
        if (!nmsEntity.getTags().contains("dumb")) return;

        mob.setAI(false);
        mob.setSilent(true);
    }
}
