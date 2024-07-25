package com.zp4rker.bukkitplayground.refl;

import org.bukkit.Bukkit;

public class ReflUtil {
    private static final String OBC_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();
    private static final String NMS_PACKAGE;

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
    }

    public static Class<?> getOBCClass(String className) throws ClassNotFoundException {
        return Class.forName(OBC_PACKAGE + "." + className);
    }

    public static Class<?> getNMSClass(String className) throws ClassNotFoundException {
        return Class.forName(NMS_PACKAGE + "." + className);
    }
}
