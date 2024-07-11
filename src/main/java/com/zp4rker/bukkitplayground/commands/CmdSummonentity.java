package com.zp4rker.bukkitplayground.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CmdSummonentity implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (args.length < 1) return true;

        String entityId = args[0];
        Entity entity = player.getServer().getEntity(UUID.fromString(entityId));
        if (entity == null) return true;

        entity.teleport(player);

        return true;
    }
}
