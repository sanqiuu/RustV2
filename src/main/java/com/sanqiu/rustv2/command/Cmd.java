package com.sanqiu.rustv2.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Cmd implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("rustmc"))
        {
            if (!(sender instanceof Player ))
            {
                sender.sendMessage("你必须是一名玩家!");
                return true;
            }
            Player player = (Player) sender;
            if(!player.hasPermission("rustmc.use"))
            {
                return true;
            }
            if(args.length == 1 && args[0]!=null )
            {
                if(args[0].equals("demo")){
                    player.sendMessage("rustmc demo ^-^");
                }
                if(args[0].equals("give")){
                    player.getInventory().addItem(new ItemStack(Material.WORKBENCH));
                }
            }else{
                player.sendMessage("please use [rustmc give]");
            }
            return true; // 返回true防止返回指令的usage信息
        }
        return false;

    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> list = new ArrayList<>();
        list.add("demo");
        return list;

    }
}