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
import java.util.Random;

public class SupplyBox {
    private static class SUPPLY_ELEMENT{
        public ItemStack itemStack;
        public int weight;
        public SUPPLY_ELEMENT (ItemStack itemStack,int weight){
            this.itemStack = itemStack;
            this.weight = weight;
        }
    }
    private static SUPPLY_ELEMENT[] SupplyList = {
            new SUPPLY_ELEMENT(new ItemStack(Material.IRON_INGOT,10),10),
            new SUPPLY_ELEMENT(new ItemStack(Material.REDSTONE,10),10),
            new SUPPLY_ELEMENT(new ItemStack(Material.FEATHER,10),10),
            new SUPPLY_ELEMENT(new ItemStack(Material.LEATHER,10),10),
            new SUPPLY_ELEMENT(new ItemStack(Material.STRING,10),10),
            new SUPPLY_ELEMENT(Drop.createCloth(10),10),
            new SUPPLY_ELEMENT(Drop.createBlueprint("铁靴子",1),5),
            new SUPPLY_ELEMENT(Drop.createBlueprint("铁胸甲",1),5),
            new SUPPLY_ELEMENT(Drop.createBlueprint("铁头盔",1),5),
            new SUPPLY_ELEMENT(Drop.createBlueprint("铁护腿",1),5),
            new SUPPLY_ELEMENT(Drop.createBlueprint("TNT",1),5),
            new SUPPLY_ELEMENT(Drop.createBlueprint("AK-47",1),1),
            new SUPPLY_ELEMENT(Drop.createBlueprint("Karabiner 98k",1),1),
            new SUPPLY_ELEMENT(Drop.createBlueprint("MP5",1),3),
            new SUPPLY_ELEMENT(Drop.createBlueprint("火箭炮",1),1),
            new SUPPLY_ELEMENT(Drop.createBlueprint("飞机",1),1),
            new SUPPLY_ELEMENT(Drop.createBlueprint("自行车",1),5),
            new SUPPLY_ELEMENT(Drop.createBlueprint("汽车",1),3)

    };
    private static final String SupplyBoxName = "container.chest";

    private static boolean isInventoryEmpty(final Inventory inventory) {
        return Arrays.stream(inventory.getContents()).noneMatch(Objects::nonNull);

    }
    private static void addSupply(Inventory inventory){
        if(!isInventoryEmpty(inventory)) return;
        int totalWeight = 0;
        for (SUPPLY_ELEMENT element : SupplyList){
            totalWeight += element.weight;
        }
        for(int num=0;num<3;num++){
            Random random = new Random();
            int randomIndex = -1;
            int randomWeight = random.nextInt(totalWeight);
            for (int i = 0; i < SupplyList.length; i++){
                if (randomWeight < SupplyList[i].weight){
                    randomIndex = i;
                    break;
                }
                randomWeight -= SupplyList[i].weight;
            }
            inventory.addItem(SupplyList[randomIndex].itemStack);
        }
    }
    public  static void add(Chunk chunk){
        if(!chunk.isLoaded()) return;
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
