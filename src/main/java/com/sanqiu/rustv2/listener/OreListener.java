package com.sanqiu.rustv2.listener;

import com.sanqiu.rustv2.model.Ore;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;

public class OreListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Block block = event.getBlock();

        if(Ore.isOre(block)){
            Ore.remove(block);
        }
    }
    @EventHandler
    public void onBlockDropItem(BlockBreakEvent event) {

        Block block = event.getBlock();

        if(Ore.isOre(block)){
            World world = block.getWorld();
            for(ItemStack itemStack:block.getDrops()){
                int amount = itemStack.getAmount();
                itemStack.setAmount(amount *Ore.DropRate);
                world.dropItemNaturally(event.getBlock().getLocation(), itemStack);
            }
            Ore.remove(block);
            event.setDropItems(false);
        }
    }
    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        Chunk chunk = event.getChunk();
        new Ore().add(chunk);

    }
}
