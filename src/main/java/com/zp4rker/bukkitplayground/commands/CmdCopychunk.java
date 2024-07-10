package com.zp4rker.bukkitplayground.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CmdCopychunk implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        JsonObject layer0 = new JsonObject();
        layer0.addProperty("block", "air");
        JsonArray layers = new JsonArray();
        layers.add(layer0);
        JsonObject settings = new JsonObject();
        settings.addProperty("biome", "void");
        settings.add("layers", layers);

        World world = new WorldCreator("chunk_copy")
                .type(WorldType.FLAT)
                .generatorSettings(settings.toString())
                .generateStructures(false)
                .createWorld();

        Chunk chunk = player.getLocation().getChunk();

        assert world != null;
        ServerLevel levelTo = ((CraftWorld) world).getHandle();
        ServerLevel levelFrom = ((CraftWorld) player.getWorld()).getHandle();

        LevelChunk chunkTo = levelTo.getChunk(chunk.getX(), chunk.getZ());
        LevelChunk chunkFrom = levelFrom.getChunk(chunk.getX(), chunk.getZ());

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < world.getMaxHeight(); y++) {
                for (int z = 0; z < 16; z++) {
                    BlockPos bp = new BlockPos(x, y, z);
                    chunkTo.setBiome(x, y, z, chunkFrom.getNoiseBiome(x, y, z)); // Is this needed?
                    BlockState blockState = chunkFrom.getBlockState(bp);
                    chunkTo.setBlockState(bp, blockState, false);
                }
            }
        }

        sender.sendMessage(Component.text("Chunk(s) copied! Reloading now...").color(TextColor.color(0xFFAA00)));

        Chunk chunkNew = world.getChunkAt(chunk.getX(), chunk.getZ());
        chunkNew.unload(true);
        chunkNew.load(false);

        sender.sendMessage(Component.text("Chunk(s) reloaded! Teleporting now...").color(TextColor.color(0xFFAA00)));
        Location loc = player.getLocation();
        loc.setWorld(world);
        player.teleport(loc);

        return true;
    }
}
