package apple.discord.clover.wynncraft;

import apple.discord.clover.api.base.json.InstantGsonSerializing;
import apple.lib.modules.AppleModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;

public class WynncraftModule extends AppleModule {

    private static final GsonBuilder GSON_BUILDER = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm");
    private static WynncraftModule instance;

    static {
        InstantGsonSerializing.registerGson(GSON_BUILDER);
    }

    public WynncraftModule() {
        instance = this;
    }

    public static WynncraftModule get() {
        return instance;
    }

    @NotNull
    public static Gson gson() {
        return GSON_BUILDER.create();
    }

    @Override
    public void onEnable() {
    }

    @Override
    public String getName() {
        return "Wynncraft";
    }
}
