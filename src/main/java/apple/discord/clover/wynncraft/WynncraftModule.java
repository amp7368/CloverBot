package apple.discord.clover.wynncraft;

import apple.discord.clover.api.base.json.InstantGsonSerializing;
import apple.lib.modules.AppleModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;

public class WynncraftModule extends AppleModule {

    private static WynncraftModule instance;

    public WynncraftModule() {
        instance = this;
    }

    public static WynncraftModule get() {
        return instance;
    }

    @NotNull
    public static Gson gson() {
        GsonBuilder gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        return InstantGsonSerializing.registerGson(gson).create();
    }

    @Override
    public void onEnable() {
    }

    @Override
    public String getName() {
        return "Wynncraft";
    }
}
