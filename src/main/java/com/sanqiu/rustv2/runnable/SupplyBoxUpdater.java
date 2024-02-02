package com.sanqiu.rustv2.runnable;

import com.sanqiu.rustv2.data.RChunk;
import com.sanqiu.rustv2.manager.ChunkManager;
import com.sanqiu.rustv2.model.SupplyBox;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class SupplyBoxUpdater extends BukkitRunnable {
    @Override
    public void run() {
        for (RChunk rChunk: ChunkManager.INSTANCE.chunk_list){
            for(Location location : rChunk.supplyList){
                SupplyBox.update(location);
            }
        }
    }
}
