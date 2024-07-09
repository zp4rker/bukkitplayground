package com.zp4rker.bukkitplayground;

import com.zp4rker.bukkitplayground.commands.CmdStoneworld;
import com.zp4rker.bukkitplayground.commands.CmdVoidworld;
import com.zp4rker.bukkitplayground.commands.CmdWorld;
import org.bukkit.plugin.java.JavaPlugin;

public final class BukkitPlayground extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Initialising...");
        getCommand("stoneworld").setExecutor(new CmdStoneworld());
        getCommand("voidworld").setExecutor(new CmdVoidworld());
        getCommand("world").setExecutor(new CmdWorld());
    }

}
