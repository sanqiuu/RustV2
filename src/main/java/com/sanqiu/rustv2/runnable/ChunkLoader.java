package com.sanqiu.rustv2.runnable;

import com.sanqiu.rustv2.model.SupplyBox;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.scheduler.BukkitRunnable;

public class ChunkLoader extends BukkitRunnable {

    @Override
    public void run() {
        for(Chunk chunk:Bukkit.getWorlds().get(0).getLoadedChunks()){
            SupplyBox.add(chunk);
        }
    }
}
