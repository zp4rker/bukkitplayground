package com.zp4rker.bukkitplayground.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zp4rker.bukkitplayground.BukkitPlayground;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftMob;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CmdCopychunk implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;

        JsonObject layer0 = new JsonObject();
        layer0.addProperty("block", "minecraft:air");
        layer0.addProperty("height", 1);
        JsonArray layers = new JsonArray();
        layers.add(layer0);
        JsonObject settings = new JsonObject();
        settings.addProperty("biome", "plains");
        settings.add("layers", layers);

        World world = new WorldCreator(BukkitPlayground.WORLD_NAME)
                .type(WorldType.FLAT)
                .generatorSettings(settings.toString())
                .generateStructures(false)
                .createWorld();

        Chunk chunk = player.getLocation().getChunk();

        assert world != null;
        ServerLevel levelTo = ((CraftWorld) world).getHandle();
        ServerLevel levelFrom = ((CraftWorld) player.getWorld()).getHandle();

        int radius = args.length < 1 ? 0 : Integer.parseInt(args[0]);
        for (int chunkX = chunk.getX() - radius; chunkX <= chunk.getX() + radius; chunkX++) {
            for (int chunkZ = chunk.getZ() - radius; chunkZ <= chunk.getZ() + radius; chunkZ++) {
                LevelChunk chunkTo = levelTo.getChunk(chunkX, chunkZ);
                LevelChunk chunkFrom = levelFrom.getChunk(chunkX, chunkZ);

                for (int x = 0; x < 16; x++) {
                    for (int y = world.getMinHeight(); y < world.getMaxHeight(); y++) {
                        for (int z = 0; z < 16; z++) {
                            chunkTo.setBiome(x, y, z, chunkFrom.getNoiseBiome(x, y, z));
                            BlockPos pos = new BlockPos(x, y, z);
                            BlockState state = chunkFrom.getBlockState(pos);
                            chunkTo.setBlockState(pos, state, false);
                            if (state.hasBlockEntity()) {
                                BlockPos worldPos = new BlockPos(chunkX * 16 + x, y, chunkZ * 16 +z);
                                BlockEntity be = levelFrom.getBlockEntity(worldPos);
                                if (be == null) continue;
                                chunkTo.setBlockEntity(be);
                            }
                        }
                    }
                }

                for (Entity entity : levelFrom.getChunkEntities(chunkX, chunkZ)) {
                    if (entity instanceof HumanEntity) continue;

                    Location loc = entity.getLocation();
                    loc.setWorld(world);
                    Entity newEntity = entity.copy(loc);
                    if (!(newEntity instanceof Mob mob)) continue;

                    net.minecraft.world.entity.Mob nmsMob = ((CraftMob) mob).getHandle();
                    nmsMob.addTag(BukkitPlayground.DUMB_TAG);

                    mob.setAI(false);
                    mob.setSilent(true);
                }
            }
        }

        sender.sendMessage(Component.text("Chunk(s) copied! Reloading world...").color(TextColor.color(0xFFAA00)));

        Bukkit.unloadWorld(world, true);
        world = new WorldCreator(world.getName()).createWorld();
        sender.sendMessage(Component.text("World reloaded! Teleporting you now...").color(TextColor.color(0xFFAA00)));

        Location loc = player.getLocation();
        loc.setWorld(world);
        player.teleport(loc);

        return true;
    }
}
