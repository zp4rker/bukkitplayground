package com.zp4rker.bukkitplayground;

import com.zp4rker.bukkitplayground.commands.CmdCopychunk;
import com.zp4rker.bukkitplayground.commands.CmdDeleteworld;
import com.zp4rker.bukkitplayground.commands.CmdSummonentity;
import com.zp4rker.bukkitplayground.listeners.EntitySpawn;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class BukkitPlayground extends JavaPlugin {
    @Override
    public void onEnable() {
        getCommand("copychunk").setExecutor(new CmdCopychunk());
        getCommand("deleteworld").setExecutor(new CmdDeleteworld());
        getCommand("summonentity").setExecutor(new CmdSummonentity());

        registerEvents(
                new EntitySpawn()
        );
    }

    private void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
