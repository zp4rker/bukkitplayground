package com.zp4rker.bukkitplayground.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class WorldGen {

    public static void handle(CommandSender sender, String[] args, String worldName, JsonObject generatorSettings) {
        if (args.length == 0) {
            sender.sendMessage(new ComponentBuilder().append("Please provide an argument!").color(ChatColor.RED).build().toLegacyText());
            return;
        }

        if (args[0].equalsIgnoreCase("start")) {
            if (sender.getServer().getWorld(worldName) != null) {
                sender.sendMessage(new ComponentBuilder().append("World already exists!").color(ChatColor.RED).build().toLegacyText());
                return;
            }

            World world = new WorldCreator(worldName)
                    .type(WorldType.FLAT)
                    .generatorSettings(generatorSettings.toString())
                    .createWorld();

            sender.sendMessage(new ComponentBuilder().append("World created!").color(ChatColor.GOLD).build().toLegacyText());

            if (sender instanceof Player player) {
                sender.sendMessage(new ComponentBuilder().append("Teleporting to new world...").color(ChatColor.GOLD).build().toLegacyText());
                player.teleport(world.getSpawnLocation());
            }
        } else if (args[0].equalsIgnoreCase("stop")) {
            World world = sender.getServer().getWorld(worldName);
            if (world == null) {
                sender.sendMessage(new ComponentBuilder("World does not exist!").color(ChatColor.RED).build().toLegacyText());
                return;
            }

            for (Player player : world.getPlayers()) {
                player.teleport(player.getServer().getWorlds().getFirst().getSpawnLocation());
                player.sendMessage(new ComponentBuilder().append("Teleporting you to original world...").color(ChatColor.GOLD).build().toLegacyText());
            }

            if (!(Bukkit.unloadWorld(world, false))) {
                sender.sendMessage(new ComponentBuilder().append("Failed to unload world!").color(ChatColor.RED).build().toLegacyText());
                return;
            }
            try {
                FileUtils.deleteDirectory(world.getWorldFolder());
            } catch (IOException e) {
                sender.sendMessage(new ComponentBuilder().append("Failed to delete world!").color(ChatColor.RED).build().toLegacyText());
                return;
            }
            sender.sendMessage(new ComponentBuilder().append("World deleted!").color(ChatColor.GOLD).build().toLegacyText());
        }

    }

    public static JsonObject settings(String baseBlock) {
        JsonObject stoneLayer = new JsonObject();
        stoneLayer.addProperty("block", baseBlock);
        stoneLayer.addProperty("height", 1);
        JsonArray layers = new JsonArray();
        layers.add(stoneLayer);

        JsonObject genSettings = new JsonObject();
        genSettings.add("layers", layers);
        genSettings.addProperty("biome", "void");
        genSettings.add("structure_overrides", new JsonArray());

        return genSettings;
    }

}
