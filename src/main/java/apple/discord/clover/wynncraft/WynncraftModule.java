package apple.discord.clover.wynncraft;

import apple.lib.modules.AppleModule;

public class WynncraftModule extends AppleModule {

    private static WynncraftModule instance;

    public WynncraftModule() {
        instance = this;
    }

    public static WynncraftModule get() {
        return instance;
    }

    @Override
    public void onEnable() {
        WynnDatabase.load();
        new WynnPlayerDatabase();
    }

    @Override
    public String getName() {
        return "Wynncraft";
    }
}
