package com.sanqiu.rustv2.manager;

import com.sanqiu.rustv2.data.RPlayer;
import com.sanqiu.rustv2.util.Config;
import com.sanqiu.rustv2.util.JavaUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerManager {
    public static PlayerManager INSTANCE = new PlayerManager();
    private Config config;
    private YamlConfiguration ymal;
    private PlayerManager(){
        config  = new Config("player.yml");
        ymal = config.load();
        ymal.createSection("player_list");
    }
    public final List<RPlayer> player_list = new ArrayList<>();
    public boolean contains(Player player){
        String uuid = player.getUniqueId().toString();
        boolean result = false;
        for(RPlayer p:PlayerManager.INSTANCE.player_list){
            if(p.uuid.equals(uuid)){
                result = true;
            }
        }

        return  result;
    }
    public RPlayer get(Player player){
        String uuid = player.getUniqueId().toString();
        RPlayer rPlayer = null;
        for(RPlayer p:PlayerManager.INSTANCE.player_list){
            if(p.uuid.equals(uuid)){
                rPlayer = p;
            }
        }

        return  rPlayer;
    }
    public   void load(){

        ymal.getConfigurationSection("player_list").getKeys(false).forEach(key->{
            String uuid = ymal.getString("player_list."+key+".uuid");
            List<String> blueprintList = ymal.getStringList("player_list."+key+".blueprintList");
            List<Location> blocksList  = JavaUtil.castList(ymal.getList("player_list."+key+".blocksList"),Location.class);
            RPlayer player = new RPlayer(uuid,blueprintList,blocksList);
            player_list.add(player);
        });

    }
    public void save(){
        int index = 0;
        for(RPlayer player:player_list){
            String uuid = player.uuid;
            String name = player.name;
            List<String> blueprintList = player.blueprintList;
            List<Location> blocksList  =  player.blocksList;
            ymal.set("player_list."+index+".uuid",uuid);
            ymal.set("player_list."+index+".name",name);
            ymal.set("player_list."+index+".blueprintList",blueprintList);
            ymal.set("player_list."+index+".blocksList",blocksList);
            index++;
        }
        config.save();
    }
}
