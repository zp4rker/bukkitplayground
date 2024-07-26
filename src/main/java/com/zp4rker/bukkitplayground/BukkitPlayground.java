package com.zp4rker.bukkitplayground;

import com.zp4rker.bukkitplayground.commands.CmdKill;
import org.bukkit.plugin.java.JavaPlugin;

public final class BukkitPlayground extends JavaPlugin {
    @Override
    public void onEnable() {
        getCommand("kill").setExecutor(new CmdKill());
    }
}
