package com.sanqiu.rustv2.manager;

import com.sanqiu.rustv2.data.RChunk;
import com.sanqiu.rustv2.data.RPlayer;
import com.sanqiu.rustv2.util.Config;
import com.sanqiu.rustv2.util.JavaUtil;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChunkManager {
    public static ChunkManager INSTANCE = new ChunkManager();
    private Config config;
    private YamlConfiguration ymal;
    private ChunkManager(){
        config  = new Config("chunk.yml");
        ymal = config.load();
        ymal.createSection("chunk_list");
    }
    public final List<RChunk> chunk_list = new ArrayList<>();
    public boolean contains(Chunk chunk){
       int x = chunk.getX();
       int z = chunk.getZ();
       for(RChunk rChunk:chunk_list){
           if(rChunk.x == x && rChunk.z == z){
               return true;
           }
       }
       return false;
    }
    public RChunk get(Chunk chunk){
        int x = chunk.getX();
        int z = chunk.getZ();
        RChunk result = null;
        for(RChunk rChunk:chunk_list){
            if(rChunk.x == x && rChunk.z == z){
                result  = rChunk;
            }
        }
        return result;
    }
    public void add(RChunk rChunk){
        chunk_list.add(rChunk);
    }
    public   void load(){
        ymal.getConfigurationSection("chunk_list").getKeys(false).forEach(key->{
            int x = ymal.getInt("chunk_list."+key+".x");
            int z = ymal.getInt("chunk_list."+key+".z");
            List<Location> oreList  = JavaUtil.castList(ymal.getList("chunk_list."+key+".oreList"),Location.class);
            List<Location> supplyList  = JavaUtil.castList(ymal.getList("chunk_list."+key+".supplyList"),Location.class);
            RChunk rChunk = new RChunk(x,z,oreList,supplyList);
            chunk_list.add(rChunk);
        });


    }
    public void save(){
        int index = 0;
        for(RChunk rChunk:chunk_list){
            int x = rChunk.x;
            int z = rChunk.z;
            List<Location> oreList  =  rChunk.oreList;
            List<Location> supplyList  =  rChunk.supplyList;
            ymal.set("chunk_list."+index+".x",x);
            ymal.set("chunk_list."+index+".z",z);
            ymal.set("chunk_list."+index+".oreList",oreList);
            ymal.set("chunk_list."+index+".supplyList",supplyList);
            index++;
        }
        config.save();
    }
}
