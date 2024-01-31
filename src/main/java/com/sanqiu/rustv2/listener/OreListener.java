package com.sanqiu.rustv2.listener;

import com.sanqiu.rustv2.model.Ore;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class OreListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Block block = event.getBlock();

        if(Ore.isOre(block)){
            Ore.remove(block);
        }
    }


    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();
        new Ore().add(chunk);

    }
}
