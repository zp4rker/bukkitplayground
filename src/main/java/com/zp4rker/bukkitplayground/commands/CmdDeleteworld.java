package com.zp4rker.bukkitplayground.commands;

import com.zp4rker.bukkitplayground.BukkitPlayground;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class CmdDeleteworld implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        World world = new WorldCreator(BukkitPlayground.WORLD_NAME).createWorld();
        if (world == null) return true;

        Bukkit.unloadWorld(world, false);
        try {
            FileUtils.deleteDirectory(world.getWorldFolder());
        } catch (IOException e) {
            sender.sendMessage(Component.text("Failed to delete the world directory!").color(TextColor.color(0xFF5555)));
            return true;
        }

        sender.sendMessage(Component.text("Deleted the world!").color(TextColor.color(0xFFAA00)));

        return true;
    }
}
