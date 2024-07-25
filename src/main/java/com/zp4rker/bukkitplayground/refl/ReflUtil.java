package com.zp4rker.bukkitplayground.refl;

import org.bukkit.Bukkit;

public class ReflUtil {
    private static final String OBC_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();
    private static final String NMS_PACKAGE;

    public static final String VERSION;
    public static final int MAJOR_VERSION;
    public static final int MINOR_VERSION;

    static {
        String nmsPackage;
        Object craftServer;
        Object nmsServer;
        try {
            craftServer = getOBCClass("CraftServer").cast(Bukkit.getServer());
            nmsServer = craftServer.getClass().getDeclaredMethod("getServer").invoke(craftServer);
            Class<?> nmsServerClass = nmsServer.getClass();
            if (nmsServerClass.getName().endsWith("DedicatedServer")) {
                nmsServerClass = nmsServerClass.getSuperclass();
            }
            nmsPackage = nmsServerClass.getPackage().getName();
        } catch (ReflectiveOperationException e) {
            nmsPackage = "net.minecraft.server";
        }
        NMS_PACKAGE = nmsPackage;

        VERSION = Bukkit.getBukkitVersion().split("-")[0];
        MAJOR_VERSION = Integer.parseInt(VERSION.split("\\.")[1]);
        MINOR_VERSION = VERSION.split("\\.").length > 2 ? Integer.parseInt(VERSION.split("\\.")[2]) : 0;
    }

    public static Class<?> getOBCClass(String className) throws ClassNotFoundException {
        return Class.forName(OBC_PACKAGE + "." + className);
    }

    public static Class<?> getNMSClass(String className) throws ClassNotFoundException {
        return Class.forName(NMS_PACKAGE + "." + className);
    }
}
