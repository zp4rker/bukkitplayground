package com.zp4rker.bukkitplayground.commands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CommandGive implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 2) return false;

        String playerName = args[0];
        Player player = Bukkit.getServer().getPlayer(playerName);

        if (player == null) {
            sender.sendMessage((new ComponentBuilder()).color(ChatColor.RED).append("Could not find player with name: " + playerName).build().toLegacyText());
            return true;
        }

        String itemName = args[1];
        Material material = Material.matchMaterial(itemName);

        if (material == null) {
            sender.sendMessage((new ComponentBuilder()).color(ChatColor.RED).append("Could not find material with name: " + itemName).build().toLegacyText());
            return true;
        }

        int amount = args.length >= 3 ? Integer.parseInt(args[2]) : 1;
        ItemStack item = new ItemStack(material, amount);

        if (args.length >= 4) {
            String nbtTags = args[3].replace("[", "").replace("]", "");
            item = Bukkit.getUnsafe().modifyItemStack(item, material.getKey().getKey() + "[" + nbtTags + "]");
        }

        player.getWorld().dropItem(player.getLocation(), item);

        sender.sendMessage((new ComponentBuilder()).append("Sent item to ").color(ChatColor.GREEN).append(player.getDisplayName()).color(ChatColor.GOLD).build().toLegacyText());
        player.sendMessage(new ComponentBuilder().append("Received ").color(ChatColor.GOLD).append(material.getKey().getKey()).color(ChatColor.GREEN).build().toLegacyText());

        return true;
    }
}
