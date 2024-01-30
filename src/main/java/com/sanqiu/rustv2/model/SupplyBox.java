package com.sanqiu.rustv2.model;

import com.sanqiu.rustv2.data.RChunk;
import com.sanqiu.rustv2.manager.ChunkManager;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public class SupplyBox {
    private static final String SupplyBoxName = "container.chest";

    private static boolean isInventoryEmpty(final Inventory inventory) {
        return Arrays.stream(inventory.getContents()).noneMatch(Objects::nonNull);

    }
    private static void addSupply(Inventory inventory){
        if(!isInventoryEmpty(inventory)) return;

        inventory.addItem(new ItemStack(Material.DIAMOND));
    }
    public  static void add(Chunk chunk){
       RChunk rChunk =  ChunkManager.INSTANCE.get(chunk);
        if(rChunk!=null && rChunk.isSupplyListInit()) return;
        if(rChunk == null) {
            rChunk = new RChunk(chunk.getX(),chunk.getZ());
            ChunkManager.INSTANCE.add(rChunk);
        }
        rChunk.initSupplyList();
        ChunkSnapshot chunkSnapshot = chunk.getChunkSnapshot();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int y_max = chunkSnapshot.getHighestBlockYAt(x,z);
                for (int y = 0; y < y_max; y++) {

                    Block block = chunk.getBlock(x,y,z);
                    if (isSupplyBox(block)){

                        Location location = block.getLocation();
                        Chest chest = (Chest)block.getState();
                        Inventory inventory = chest.getInventory();
                        addSupply(inventory);
                        rChunk.supplyList.add(location);
                    }
                }
            }
        }

    }
    public  static void update(Location location){
        Block block = location.getBlock();
        if(isSupplyBox(block)){
            Chest chest = (Chest)block.getState();
            Inventory inventory = chest.getInventory();
            addSupply(inventory);

        }
    }
    public static boolean isSupplyBox(Inventory inventory){
        boolean result = false;
        if(inventory.getType() != InventoryType.CHEST) return result;

        if(!inventory.getTitle().equals(SupplyBoxName)) return result;
        result = true;
        return  result;
    }
    public static boolean isSupplyBox(Block block){
        if(block.getState() instanceof Chest){
            Chest chest = (Chest)block.getState();
            return chest.getInventory().getTitle().equals(SupplyBoxName);
        }
        return false;
    }
}
