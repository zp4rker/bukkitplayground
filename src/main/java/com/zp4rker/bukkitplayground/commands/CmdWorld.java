package com.zp4rker.bukkitplayground.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CmdWorld implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(new ComponentBuilder().append("Only players can use that command!").color(ChatColor.RED).build().toLegacyText());
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(new ComponentBuilder().append("You must provide a world nane!").color(ChatColor.RED).build().toLegacyText());
            return true;
        }

        World world = Bukkit.getWorld(args[0]);
        if (world == null) {
            sender.sendMessage(new ComponentBuilder().append("Couldn't find world with name: ").color(ChatColor.RED).append(args[0]).underlined(true).build().toLegacyText());
            return true;
        }

        player.sendMessage(new ComponentBuilder().append("Teleporting you to ").color(ChatColor.GOLD).append(args[0]).color(ChatColor.GREEN).build().toLegacyText());
        player.teleport(world.getSpawnLocation());

        return true;
    }
}
