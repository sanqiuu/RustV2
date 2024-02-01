package com.sanqiu.rustv2.interfacee;

import com.sanqiu.rustv2.util.ItemUtil;
import com.sanqiu.rustv2.util.JavaUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.Map;

public class VehicleInterface {
    public enum VEHICLE_TYPE{
        Car("汽车","Item:\n" +
                "  ==: org.bukkit.inventory.ItemStack\n" +
                "  type: VEHICLE_VEHICLE_CRATE\n" +
                "  meta:\n" +
                "    ==: ItemMeta\n" +
                "    meta-type: UNSPECIFIC\n" +
                "    internal: H4sIAAAAAAAAAONiYOBi4HPKyU/Ods0rySypDElMZ2bgSs1Lz8xLDclMLWIAAmYGzvKM1NSckMqCVBCfg4G9LDUjMzknlUEIyrBKKs0tSC2KT04E6gAA7RLAklUAAAA="),
        Bike("自行车","Item:\n" +
                "  ==: org.bukkit.inventory.ItemStack\n" +
                "  type: VEHICLE_VEHICLE_CRATE\n" +
                "  meta:\n" +
                "    ==: ItemMeta\n" +
                "    meta-type: UNSPECIFIC\n" +
                "    internal: H4sIAAAAAAAAAONiYOBi4HPKyU/Ods0rySypDElMZ2bgSs1Lz8xLDclMLWIAAmYGzvKM1NSckMqCVBCfg4G9LDUjMzknlUEQyrDKzczLjE/KzAYqAACj5ly4VAAAAA=="),
        Plane("飞机","Item:\n" +
                "  ==: org.bukkit.inventory.ItemStack\n" +
                "  type: VEHICLE_VEHICLE_CRATE\n" +
                "  meta:\n" +
                "    ==: ItemMeta\n" +
                "    meta-type: UNSPECIFIC\n" +
                "    internal: H4sIAAAAAAAAAONiYOBi4HPKyU/Ods0rySypDElMZ2bgSs1Lz8xLDclMLWIAAmYGzvKM1NSckMqCVBCfg4G9LDUjMzknlUEEyrAqLsgvKimOL8hJzAOqAQBKSKVhVwAAAA=="),
        OilContent("简便油桶","Item:\n" +
                "  ==: org.bukkit.inventory.ItemStack\n" +
                "  type: VEHICLE_JERRY_CAN\n" +
                "  meta:\n" +
                "    ==: ItemMeta\n" +
                "    meta-type: UNSPECIFIC\n" +
                "    internal: H4sIAAAAAAAAAONiYGBlYEkrTc1xnePAwAAAx2b6Ww8AAAA=");
        private final String name;
        private final String itemstackString;
        VEHICLE_TYPE(String name,String itemstackString) {
            this.name = name;
            this.itemstackString = itemstackString;
        }
    }
    public static ItemStack create(VEHICLE_TYPE type){
        ItemStack item = ItemUtil.stringToItem(type.itemstackString);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(type.name);
        item.setItemMeta(itemMeta);
        return item;
    }

}
