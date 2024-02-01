package com.sanqiu.rustv2.runnable;

import com.sanqiu.rustv2.RustV2;
import com.sanqiu.rustv2.interfacee.VehicleInterface;
import com.sanqiu.rustv2.model.Radio;
import com.sanqiu.rustv2.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
;

public class RadioUpdater extends BukkitRunnable {
    @Override
    public void run() {
        for(Player player : RustV2.getPlugin().getServer().getOnlinePlayers()){
            Radio.Radiate(player);
            //Bukkit.getLogger().info("item类型-"+ ItemUtil.itemToString(player.getItemInHand()));

        }
    }
}
