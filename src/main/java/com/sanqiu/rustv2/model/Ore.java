package com.sanqiu.rustv2.model;

import com.sanqiu.rustv2.RustV2;
import com.sanqiu.rustv2.data.RChunk;
import com.sanqiu.rustv2.manager.ChunkManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;

public class Ore  {

    Chunk chunk;
    RChunk rChunk;
    int block_x;
    int block_y;
    int block_z;
    private Block getRelative(int x, int y, int z){
        int new_x = block_x+x;
        int new_y = block_y+y;
        int new_z = block_z+z;
        if(new_x>=0 && new_x<=15 && new_z >=0 && new_z<=15){
            return chunk.getBlock(new_x,new_y,new_z);
        }
        return null;
    }
    private boolean canCreate(){

        for(int x=-3;x<=3;x++){
            for(int z=-3;z<=3;z++){
                for (int y=-1;y<=2;y++){
                    Block b = getRelative(x,y,z);
                    if(b == null)   return false;
                    if(y == -1){
                        if(b.getType()!= Material.GRASS){
                            return false;
                        }
                    }else{
                        if(b.getType()!=Material.AIR && b.getType()!=Material.LONG_GRASS){
                            return false;
                        }
                    }

                }
            }
        }

        return true;
    }

    private boolean createOre(){
        if(!canCreate()) return false;

        for(int x=-1;x<=1;x++){
            for(int z=-1;z<=1;z++){
                for (int y=0;y<=1;y++){
                    Block b = getRelative(x,y,z);
                    if(b == null) continue;
                    if(x==0&&z==0){
                        b.setType(Material.REDSTONE_ORE,false);
                        rChunk.addOre(b.getLocation());
                        continue;
                    }
                    if(x==0){
                        b.setType(Material.COAL_ORE,false);
                        rChunk.addOre(b.getLocation());
                        continue;
                    }
                    if(z==0){
                        b.setType(Material.IRON_ORE,false);
                        rChunk.addOre(b.getLocation());
                        continue;
                    }
                    if(y==0){
                        b.setType(Material.COBBLESTONE,false);
                        rChunk.addOre(b.getLocation());
                    }
                }
            }
        }
        return true;
    }


    private void create(Chunk chunk) {
        this.chunk = chunk;

        ChunkSnapshot chunkSnapshot = chunk.getChunkSnapshot();
        int max_num = 1;
        int num = 0;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                if(num == max_num) break;
                int y = chunkSnapshot.getHighestBlockYAt(x,z);
                block_x = x;
                block_y = y;
                block_z = z;
                boolean success = createOre();
                if(success) num++;
            }
        }
    }
    public    void  add(Chunk chunk){
        if(!chunk.isLoaded()) return;
        RChunk rChunk =  ChunkManager.INSTANCE.get(chunk);
        if(rChunk!=null && rChunk.isOreListInit()) return;
        if(rChunk == null) {
            rChunk = new RChunk(chunk.getX(),chunk.getZ());
            ChunkManager.INSTANCE.add(rChunk);
        }
        rChunk.initOreList();
        this.rChunk = rChunk;
        create(chunk);

        ChunkSnapshot chunkSnapshot = chunk.getChunkSnapshot();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int y_max = chunkSnapshot.getHighestBlockYAt(x,z);
                for (int y = 0; y < y_max; y++) {

                    Block block = chunk.getBlock(x,y,z);
                    Material material = block.getType();
                    if(material == Material.LOG ||material == Material.LOG_2 ){
                        rChunk.addOre(block.getLocation());
                    }
                }
            }
        }
    }
    public static boolean isOre(Block block) {
        RChunk rChunk = ChunkManager.INSTANCE.get(block.getChunk());
        if(rChunk != null && rChunk.isOre(block.getLocation())){
            return true;
        }
     return false;
    }
    public static void remove(Block block) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(RustV2.getPlugin(), new Runnable() {
            Material material = block.getType();
            byte data = block.getData();
            Location location = block.getLocation();
            @Override
            public void run() {
                Block block = location.getBlock();
                block.setType(material);
                block.setData(data);
            }
        }, 20*60); //20 Tick (1 Second) delay before run() is called
    }
}
