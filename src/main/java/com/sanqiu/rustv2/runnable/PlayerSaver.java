package com.sanqiu.rustv2.runnable;

import com.sanqiu.rustv2.manager.PlayerManager;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerSaver extends BukkitRunnable {
    @Override
    public void run() {
        PlayerManager.INSTANCE.save();
    }
}
