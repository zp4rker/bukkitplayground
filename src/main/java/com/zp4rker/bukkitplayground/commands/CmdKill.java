package com.zp4rker.bukkitplayground.commands;

import com.zp4rker.bukkitplayground.refl.ReflUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CmdKill implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (ReflUtil.MAJOR_VERSION > 20 || (ReflUtil.MAJOR_VERSION == 20 && ReflUtil.MINOR_VERSION >= 4)) {
            player.damage(Float.MAX_VALUE, DamageSource.builder(DamageType.OUT_OF_WORLD).build());
        } else {
            try {
                Object craftPlayer = ReflUtil.getOBCClass("entity.CraftPlayer").cast(player);
                Object nmsPlayer = craftPlayer.getClass().getDeclaredMethod("getHandle").invoke(craftPlayer);

                Class<?> entityClass;
                Class<?> damageSourceClass;
                if (ReflUtil.MAJOR_VERSION < 17) {
                    entityClass = ReflUtil.getNMSClass("Entity");
                    damageSourceClass = ReflUtil.getNMSClass("DamageSource");
                } else {
                    entityClass = Class.forName("net.minecraft.world.entity.Entity");
                    damageSourceClass = Class.forName("net.minecraft.world.damagesource.DamageSource");
                }

                Method damageMethod = getDamageMethod(entityClass, damageSourceClass);
                Object damageSource = getDamageSource(damageSourceClass, entityClass, nmsPlayer);

                damageMethod.invoke(nmsPlayer, damageSource, Float.MAX_VALUE);
            } catch (ReflectiveOperationException e) {
                player.sendMessage("Failed to execute!");
                e.printStackTrace();
            }
        }

        return true;
    }

    private static @NotNull Object getDamageSource(Class<?> damageSourceClass, Class<?> entityClass, Object entity) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException {
        Class<?> damageSourcesClass;
        if (ReflUtil.MAJOR_VERSION > 19 || (ReflUtil.MAJOR_VERSION == 19 && ReflUtil.MINOR_VERSION == 4)) {
            damageSourcesClass = Class.forName("net.minecraft.world.damagesource.DamageSources");
        } else {
            damageSourcesClass = damageSourceClass;
        }

        Object damageSource = null;
        try {
            if (damageSourceClass == damageSourcesClass) {
                damageSource = damageSourcesClass.getDeclaredField("OUT_OF_WORLD").get(null);
            } else {
                Object damageSources = entityClass.getDeclaredMethod("damageSources").invoke(entity);
                try {
                    damageSource = damageSourcesClass.getDeclaredMethod("fellOutOfWorld").invoke(damageSources);
                } catch (NoSuchMethodException e) {
                    damageSource = damageSourcesClass.getDeclaredMethod("outOfWorld").invoke(damageSources);
                }
            }
        } catch (NoSuchFieldException | NoSuchMethodException e) {
            if (damageSourceClass == damageSourcesClass) {
                for (Field field : damageSourceClass.getDeclaredFields()) {
                    if (field.getType() != damageSourceClass) continue;
                    Field nameField = null;
                    for (Field innerField : damageSourceClass.getDeclaredFields()) {
                        if (innerField.getType() != String.class) continue;
                        nameField = innerField;
                    }
                    if (nameField == null) throw new NoSuchFieldException("Could not find name field!");
                    Object obj = field.get(null);
                    if (nameField.get(obj).equals("outOfWorld")) {
                        damageSource = obj;
                        break;
                    }
                }
            } else {
                for (Method method : damageSourcesClass.getDeclaredMethods()) {
                    if (method.getReturnType() != damageSourceClass) continue;
                    if (method.getParameterCount() > 0) continue;

                    Method damageSourcesMethod = getNoParamMethod(entityClass, "damageSources", damageSourcesClass);
                    Class<?> damageTypeClass = Class.forName("net.minecraft.world.damagesource.DamageType");
                    Method typeMethod = getNoParamMethod(damageSourceClass, "type", damageTypeClass);
                    Object damageSources = damageSourcesMethod.invoke(entity);
                    Object obj = method.invoke(damageSources);
                    Object damageType = typeMethod.invoke(obj);
                    Method msgIdMethod = getNoParamMethod(damageTypeClass, "msgId", String.class);
                    Object damageTypeString = msgIdMethod.invoke(damageType);
                    if (damageTypeString.equals("outOfWorld") || damageTypeString.equals("fellOutOfWorld")) {
                        damageSource = obj;
                    }
                }
            }
            if (damageSource == null) throw new NoSuchFieldException("Could not find damage source!");
        }
        return damageSource;
    }

    private static @NotNull Method getNoParamMethod(Class<?> clazz, String name, Class<?> returnType) throws NoSuchMethodException {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(name);
        } catch (NoSuchMethodException e) {
            for (Method innerMethod : clazz.getDeclaredMethods()) {
                if (innerMethod.getReturnType() != returnType) continue;
                method = innerMethod;
            }
            if (method == null) throw new NoSuchMethodException("Could not find method `" + name + "`!");
        }
        return method;
    }

    private static @NotNull Method getDamageMethod(Class<?> entityClass, Class<?> damageSourceClass) throws NoSuchMethodException {
        Method damageMethod = null;
        try {
            damageMethod = entityClass.getDeclaredMethod("damageEntity", damageSourceClass, Float.class);
        } catch (NoSuchMethodException e) {
            for (Method method : entityClass.getDeclaredMethods()) {
                if (method.getReturnType() != boolean.class) continue;
                if (method.getParameterCount() != 2) continue;
                if (method.getParameterTypes()[0] != damageSourceClass) continue;
                if (method.getParameterTypes()[1] != float.class) continue;

                damageMethod = method;
                break;
            }
            if (damageMethod == null) throw new NoSuchMethodException("Could not find damage method!");
        }
        return damageMethod;
    }
}
