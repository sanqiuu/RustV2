package com.sanqiu.rustv2;

import com.sanqiu.rustv2.listener.*;
import com.sanqiu.rustv2.manager.ChunkManager;
import com.sanqiu.rustv2.manager.PlayerManager;
import com.sanqiu.rustv2.runnable.ChunkSaver;
import com.sanqiu.rustv2.runnable.PlayerSaver;
import com.sanqiu.rustv2.runnable.RadioUpdater;
import com.sanqiu.rustv2.runnable.SupplyBoxUpdater;
import org.bukkit.plugin.java.JavaPlugin;

public final class RustV2 extends JavaPlugin {
    private static RustV2 plugin;
    @Override
    public void onEnable() {
        // Plugin startup logic
        setPlugin(this);
        getServer().getPluginManager().registerEvents(new TNTListener(), this);
        getServer().getPluginManager().registerEvents(new LifeBlockListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new SupplyBoxListener(), this);
        getServer().getPluginManager().registerEvents(new CraftingTableListener(), this);
        getServer().getPluginManager().registerEvents(new BlueprintListener(), this);
        getServer().getPluginManager().registerEvents(new OreListener(), this);
        getServer().getPluginManager().registerEvents(new RadioListener(), this);
        new PlayerSaver().runTaskTimer(this, 10*20,30*20);
        new ChunkSaver().runTaskTimer(this, 60*20,10*60*20);
        new RadioUpdater().runTaskTimer(this, 0,20);
        new SupplyBoxUpdater().runTaskTimer(this, 10*20,30*60*20);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PlayerManager.INSTANCE.save();
        ChunkManager.INSTANCE.save();
    }
    public static RustV2 getPlugin() {
        return plugin;
    }

    public static void setPlugin(RustV2 plugin) {
        RustV2.plugin = plugin;
    }
}
