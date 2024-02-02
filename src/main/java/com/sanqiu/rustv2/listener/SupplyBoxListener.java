package com.sanqiu.rustv2.listener;

import com.sanqiu.rustv2.data.RChunk;
import com.sanqiu.rustv2.manager.ChunkManager;
import com.sanqiu.rustv2.model.SupplyBox;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.world.ChunkEvent;
import org.bukkit.event.world.ChunkLoadEvent;

public class SupplyBoxListener implements Listener {


    @EventHandler
    public void onBoxBreak(BlockBreakEvent event) {

        Block block = event.getBlock();
        RChunk rChunk = ChunkManager.INSTANCE.get(event.getBlock().getChunk());
        if(rChunk!=null && rChunk.supplyList.contains(block.getLocation())){
            event.setCancelled(true);
        }
    }
}
