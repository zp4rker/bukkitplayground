package com.zp4rker.bukkitplayground;

import com.zp4rker.bukkitplayground.commands.CommandGive;
import org.bukkit.plugin.java.JavaPlugin;

public final class BukkitPlayground extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("=".repeat(64));

        getLogger().info("BukkitPlayground version " + getDescription().getVersion());
        getLogger().info("Bukkit version " + getServer().getBukkitVersion());

        getLogger().info("=".repeat(64));

        getLogger().info("-------- Registering commands --------");
        getCommand("give").setExecutor(new CommandGive());
        getLogger().info("-------- Commands registered ---------");
    }

}
