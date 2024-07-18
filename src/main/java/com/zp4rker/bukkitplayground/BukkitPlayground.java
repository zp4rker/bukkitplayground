package com.zp4rker.bukkitplayground;

import com.zp4rker.bukkitplayground.commands.CmdCopychunk;
import com.zp4rker.bukkitplayground.commands.CmdDeleteworld;
import com.zp4rker.bukkitplayground.commands.CmdSummonentity;
import com.zp4rker.bukkitplayground.listeners.EntityListeners;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class BukkitPlayground extends JavaPlugin {
    public static final String WORLD_NAME = "chunk_copy";

    @Override
    public void onEnable() {
        getCommand("copychunk").setExecutor(new CmdCopychunk());
        getCommand("deleteworld").setExecutor(new CmdDeleteworld());
        getCommand("summonentity").setExecutor(new CmdSummonentity());

        registerEvents(
                new EntityListeners()
        );
    }

    private void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
