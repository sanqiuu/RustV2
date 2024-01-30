package com.sanqiu.rustv2.listener;

import com.sanqiu.rustv2.model.SupplyBox;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.world.ChunkLoadEvent;

public class SupplyBoxListener implements Listener {


    @EventHandler
    public void onBoxBreak(BlockBreakEvent event) {

        Block block = event.getBlock();
        if(SupplyBox.isSupplyBox(block)){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();

        SupplyBox.add(chunk);
    }
}
