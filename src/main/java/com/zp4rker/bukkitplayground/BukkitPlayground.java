package com.zp4rker.bukkitplayground;

import com.zp4rker.bukkitplayground.commands.CmdCopychunk;
import com.zp4rker.bukkitplayground.commands.CmdDeleteworld;
import org.bukkit.plugin.java.JavaPlugin;

public final class BukkitPlayground extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Initialising...");
        getCommand("copychunk").setExecutor(new CmdCopychunk());
        getCommand("deleteworld").setExecutor(new CmdDeleteworld());
    }

}
