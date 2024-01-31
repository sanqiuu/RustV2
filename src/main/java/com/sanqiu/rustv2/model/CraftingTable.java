package com.sanqiu.rustv2.model;

import com.sanqiu.rustv2.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

class CraftingTableHolder implements InventoryHolder {
    private CraftingTable gui;
    private Integer subGui;
    public CraftingTableHolder(CraftingTable gui,Integer subGui){

        this.gui = gui;
        this.subGui = subGui;
    }
    public  Integer getSubgui()
    {
        return subGui;
    }
    public CraftingTable getGUI()
    {
        return  gui;
    }
    @Override
    public Inventory getInventory() {

        return gui.getInventory();
    }

}
enum MATERIAL_TYPE {
    WOOD(0),
    STONE(0),
    REDSTONE(0),
    COAL(0),
    IRON(0),
    LEATHER(0),
    FEATHER(0),

    STRING(0),
    WOOL(0),
    FLINT(0),
    CLOTH(1),
    TORCH(0);
    private final int value;

    // 构造器默认也只能是private, 从而保证构造函数只能在内部使用
    MATERIAL_TYPE(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
 class    MATERIAL {
    private  static  MATERIAL_TYPE fromName(String string){
        MATERIAL_TYPE type = null;
        switch (string){
            case "木头":
                type = MATERIAL_TYPE.WOOD;
                break;
            case "圆石":
                type = MATERIAL_TYPE.STONE;
                break;
            case "红石":
                type = MATERIAL_TYPE.REDSTONE;
                break;
            case "皮革":
                type = MATERIAL_TYPE.LEATHER;
                break;
            case "煤":
                type = MATERIAL_TYPE.COAL;
                break;
            case "铁":
                type = MATERIAL_TYPE.IRON;
                break;
            case "羽毛":
                type = MATERIAL_TYPE.FEATHER;
                break;
            case "线":
                type = MATERIAL_TYPE.STRING;
                break;
            case "羊毛":
                type = MATERIAL_TYPE.WOOL;
                break;
            case "燧石":
                type = MATERIAL_TYPE.FLINT;
                break;
            case "布料":
                type = MATERIAL_TYPE.CLOTH;
                break;
            case "火把":
                type = MATERIAL_TYPE.TORCH;
                break;
        }
        assert type!=null;
        return type;
    }

     private  static String getName(MATERIAL_TYPE type){
        String string = null;
       switch (type){
           case WOOD:
               string = "木头";
               break;
           case STONE:
               string = "圆石";
               break;
           case REDSTONE:
               string = "红石";
               break;
           case LEATHER:
               string = "皮革";
               break;
           case COAL:
               string = "煤";
               break;
           case IRON:
               string = "铁";
               break;
           case FEATHER:
               string = "羽毛";
               break;
           case STRING:
               string = "线";
               break;
           case WOOL:
               string = "羊毛";
               break;
           case FLINT:
               string = "燧石";
               break;
           case CLOTH:
               string = "布料";
               break;
           case TORCH:
               string = "火把";
               break;
       }
       assert string!=null;
       return  string;
    }
    private   static Material getMaterial(MATERIAL_TYPE type){

        Material material = null;
        switch (type){
            case WOOD:
                material = Material.LOG;
                break;
            case STONE:
                material = Material.COBBLESTONE;
                break;
            case REDSTONE:
                material = Material.REDSTONE;
                break;
            case LEATHER:
                material =Material.LEATHER;
                break;
            case COAL:
                material = Material.COAL;
                break;
            case IRON:
                material = Material.IRON_INGOT;
                break;
            case FEATHER:
                material = Material.FEATHER;
                break;
            case STRING:
                material = Material.STRING;
                break;
            case WOOL:
                material = Material.WOOL;
                break;
            case FLINT:
                material = Material.FLINT;
                break;
            case CLOTH:
                material = Material.PAPER;
                break;
            case TORCH:
                material = Material.TORCH;
                break;
        }
        assert material!=null;
        return  material;
    }
     public static void addLore(ItemStack itemStack, MATERIAL_TYPE type, int number){
         ItemMeta itemMeta = itemStack.getItemMeta();
         String lore = MATERIAL.getName(type) +"X"+number;
         List<String> lore_list;
         if(itemMeta.hasLore()){
             lore_list = itemMeta.getLore();
             if(!lore_list.contains(lore)){
                 lore_list.add(lore);
             }
         }else{
             lore_list = new ArrayList<>();
             lore_list.add(lore);
         }
         itemMeta.setLore(lore_list);
         itemStack.setItemMeta(itemMeta);
     }
     public static List<ItemStack> fromLore(ItemStack itemStack){
         List<String> lore_list = itemStack.getItemMeta().getLore();
         List<ItemStack> list = new ArrayList<>();
         if(lore_list == null) return list;
         for(String lore:lore_list){
             String object_name = lore.split("X")[0];
             int num = Integer.parseInt(lore.split("X")[1]);
             MATERIAL_TYPE type = MATERIAL.fromName(object_name);
             ItemStack item = new ItemStack(MATERIAL.getMaterial(type),num);
             if(type.getValue() == 1){
                 ItemMeta meta = item.getItemMeta();
                 meta.setDisplayName(MATERIAL.getName(type));
                 item.setItemMeta(meta);
             }
             list.add(item);
         }
         return  list;
     }
}
public class CraftingTable {
    private Inventory inventory;
    Player player;
    public CraftingTable(Player player){
        this.player = player;
    }
    public  Inventory getInventory()
    {
        return inventory;
    }

    private ItemStack toItem(Material type, String name, int num)
    {
        ItemStack itemStack = new ItemStack(type,num);
        if(name!=null){
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(name);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }
    public  boolean buyGoods(Player player,Inventory inv,int index)
    {

        ItemStack item = inv.getItem(index);
        boolean canBuy = true;
        if(item!=null ){
            List<ItemStack> list = MATERIAL.fromLore(item);

            for(ItemStack is:list){
                int num = is.getAmount();
                if(!ItemUtil.isPlayerItemEnough(player,is,num)){
                    canBuy = false;
                    break;
                }
            }

            if(canBuy){
                for(ItemStack is:list){
                    int num = is.getAmount();
                    ItemUtil.subPlayerItemAmount(player,is,num);
                }
                ItemStack itemStack = item.clone();
                ItemMeta meta = itemStack.getItemMeta();
                meta.setLore(null);
                itemStack.setItemMeta(meta);
                player.getInventory().addItem(itemStack);
                player.sendMessage("购买成功");
            }else {
                player.sendMessage("材料不足,无法购买");
            }

        }
        return canBuy;
    }

    public void OpenCraftingTable()
    {
        inventory = Bukkit.createInventory(new CraftingTableHolder(this,null),9,"制作台");
        ItemStack item1 = toItem(Material.IRON_AXE,"工具",1);
        ItemStack item2 = toItem(Material.STRING,"材料",1);
        ItemStack item3 = toItem(Material.ARROW,"子弹",1);
        ItemStack item4 = toItem(Material.IRON_SWORD,"武器",1);
        ItemStack item5 = toItem(Material.IRON_CHESTPLATE,"护甲",1);
        ItemStack item6 = toItem(Material.WOOD,"建筑方块",1);
        ItemStack item7 = toItem(Material.TNT,"爆炸物",1);
        ItemStack item8 = toItem(Material.BOAT,"载具",1);
        ItemStack item9 = toItem(Material.FISHING_ROD,"测试",1);
        inventory.setItem(0,item1);
        inventory.setItem(1,item2);
        inventory.setItem(2,item3);
        inventory.setItem(3,item4);
        inventory.setItem(4,item5);
        inventory.setItem(5,item6);
        inventory.setItem(6,item7);
        inventory.setItem(7,item8);
        inventory.setItem(8,item9);
        player.openInventory(inventory);
    }
    private void InitTNTInventory(Inventory inv,Player player){

        ItemStack item1 = toItem(Material.TNT,"TNT",1);
        if(!Blueprint.hasBlueprint(player, item1)){
            item1  = toItem(Material.STAINED_GLASS_PANE,null,1);
        }
        MATERIAL.addLore(item1,MATERIAL_TYPE.REDSTONE,64);
        MATERIAL.addLore(item1,MATERIAL_TYPE.STRING,4);
        MATERIAL.addLore(item1,MATERIAL_TYPE.COAL,10);
        inv.setItem(0,item1);
    }
    private void InitTestInventory(Inventory inv,Player player){
        ItemStack item1 = toItem(Material.PAPER,"蓝图-TNT",1);
        ItemStack item2 = toItem(Material.IRON_ORE,null,64);
        ItemStack item3 = toItem(Material.GOLD_ORE,null,64);
        ItemStack item4 = toItem(Material.DIAMOND_ORE,null,64);
        ItemStack item5 = toItem(Material.COAL_ORE,null,64);
        ItemStack item6 = toItem(Material.REDSTONE_ORE,null,64);
        ItemStack item7 = toItem(Material.LOG,null,64);
        ItemStack item8 = toItem(Material.STONE,null,64);
        ItemStack item9 = toItem(Material.COBBLESTONE,null,64);
        ItemStack item10 = toItem(Material.BREAD,null,64);
        ItemStack item11 = toItem(Material.GOLDEN_APPLE,null,64);
        inv.setItem(0,item1);
        inv.setItem(1,item2);
        inv.setItem(2,item3);
        inv.setItem(3,item4);
        inv.setItem(4,item5);
        inv.setItem(5,item6);
        inv.setItem(6,item7);
        inv.setItem(7,item8);
        inv.setItem(8,item9);
        inv.setItem(9,item10);
        inv.setItem(10,item11);
    }
    private void InitRustBlockInventory(Inventory inv,Player player){
        ItemStack item ;
         item = toItem(Material.WOOD,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,7);
        inv.setItem(0,item);
        item = toItem(Material.SMOOTH_BRICK,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.STONE,12);
        inv.setItem(1,item);
        item = toItem(Material.FURNACE,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.STONE,25);
        inv.setItem(2,item);
        item = toItem(Material.WOOD_DOOR,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,240);
        inv.setItem(3,item);
        item = toItem(Material.BED,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.CLOTH,25);
        MATERIAL.addLore(item,MATERIAL_TYPE.LEATHER,10);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,60);
        item = toItem(Material.CHEST,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,70);
        inv.setItem(4,item);
        item = toItem(Material.ANVIL,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,80);
        inv.setItem(5,item);
        item = toItem(Material.LADDER,null,1);
        MATERIAL. addLore(item,MATERIAL_TYPE.WOOD,5);
        inv.setItem(6,item);
        item = toItem(Material.TORCH,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.COAL,5);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,3);
        inv.setItem(7,item);
        item = toItem(Material.SIGN,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,30);
        inv.setItem(8,item);
        item = toItem(Material.TRAP_DOOR,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,50);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,25);
        inv.setItem(9,item);
        item = toItem(Material.WOOD_STAIRS,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,7);
        inv.setItem(10,item);
        item = toItem(Material.WOOL,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.CLOTH,5);
        inv.setItem(11,item);
        item = toItem(Material.IRON_DOOR,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,100);
        inv.setItem(12,item);
        item = toItem(Material.RAILS,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,10);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,5);
        inv.setItem(13,item);
        item = toItem(Material.IRON_FENCE,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,20);
        inv.setItem(14,item);
        item = toItem(Material.SMOOTH_STAIRS,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.STONE,10);
        inv.setItem(15,item);

        item = toItem(Material.SMOOTH_BRICK,null,1);
        item.setDurability((short) 2);

        MATERIAL.addLore(item,MATERIAL_TYPE.STONE,5);
        inv.setItem(16,item);
        item = toItem(Material.SMOOTH_BRICK,null,1);
        item.setDurability((short) 3);
        MATERIAL.addLore(item,MATERIAL_TYPE.STONE,15);
        inv.setItem(17,item);
        item = toItem(Material.REDSTONE_LAMP_OFF,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.TORCH,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.REDSTONE,5);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,2);
        inv.setItem(18,item);
        item = toItem(Material.STAINED_GLASS,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.STONE,20);
        inv.setItem(19,item);
    }
    private void InitToolInventory(Inventory inv,Player player){

        ItemStack item = toItem(Material.STONE_AXE,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,15);
        MATERIAL.addLore(item,MATERIAL_TYPE.STONE,5);
        inv.setItem(0,item);
        item = toItem(Material.STONE_PICKAXE,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,15);
        MATERIAL.addLore(item,MATERIAL_TYPE.STONE,5);
        inv.setItem(1,item);
        item = toItem(Material.FISHING_ROD,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,10);
        MATERIAL.addLore(item,MATERIAL_TYPE.STRING,44);
        inv.setItem(2,item);
        item = toItem(Material.STONE_HOE,null, 1);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,15);
        MATERIAL.addLore(item,MATERIAL_TYPE.STONE,5);
        inv.setItem(3,item);
        item = toItem(Material.IRON_AXE,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,25);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,30);
        inv.setItem(4,item);
        item = toItem(Material.IRON_PICKAXE,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,25);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,50);
        inv.setItem(5,item);
        item = toItem(Material.IRON_HOE,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.WOOD,25);
        MATERIAL.addLore(item,MATERIAL_TYPE.IRON,30);
        inv.setItem(6,item);

    }
    private void InitAmmoInventory(Inventory inv,Player player){
        ItemStack item1 = toItem(Material.ARROW,null,1);
        MATERIAL.addLore(item1,MATERIAL_TYPE.WOOD,2);
        MATERIAL.addLore(item1,MATERIAL_TYPE.STONE,2);
        MATERIAL.addLore(item1,MATERIAL_TYPE.FEATHER,2);
        inv.setItem(0,item1);
    }
    private void InitWeaponInventory(Inventory inv,Player player){
        ItemStack item1 = toItem(Material.BOW,null,1);
        MATERIAL.addLore(item1,MATERIAL_TYPE.WOOD,20);
        MATERIAL.addLore(item1,MATERIAL_TYPE.STRING,10);
        ItemStack item2 = toItem(Material.STONE_SWORD,null,1);
        MATERIAL.addLore(item2,MATERIAL_TYPE.WOOD,15);
        MATERIAL.addLore(item2,MATERIAL_TYPE.STONE,25);
        ItemStack item3 = toItem(Material.IRON_SWORD,null,1);
        MATERIAL.addLore(item3,MATERIAL_TYPE.WOOD,25);
        MATERIAL.addLore(item3,MATERIAL_TYPE.IRON,60);
        ItemStack item4 = toItem(Material.SHIELD,null,1);
        MATERIAL.addLore(item4,MATERIAL_TYPE.WOOD,50);
        inv.setItem(0,item1);
        inv.setItem(1,item2);
        inv.setItem(2,item3);
        inv.setItem(3,item4);
    }
    private void InitArmorInventory(Inventory inv,Player player){

        ItemStack item1 = toItem(Material.LEATHER_BOOTS,null,1);
        MATERIAL.addLore(item1,MATERIAL_TYPE.LEATHER,8);
        ItemStack item2 = toItem(Material.LEATHER_CHESTPLATE,null,1);
        MATERIAL.addLore(item2,MATERIAL_TYPE.LEATHER,15);
        ItemStack item3 = toItem(Material.LEATHER_HELMET,null,1);
        MATERIAL.addLore(item3,MATERIAL_TYPE.LEATHER,5);
        ItemStack item4 = toItem(Material.LEATHER_LEGGINGS,null,1);
        MATERIAL.addLore(item4,MATERIAL_TYPE.LEATHER,10);
        inv.setItem(0,item1);
        inv.setItem(1,item2);
        inv.setItem(2,item3);
        inv.setItem(3,item4);
    }
    private void InitMaterialInventory(Inventory inv,Player player){
        ItemStack item = toItem(Material.STRING,null,1);
        MATERIAL.addLore(item,MATERIAL_TYPE.CLOTH,1);
        inv.setItem(0,item);

    }
    private void InitVehicleInventory(Inventory inv,Player player){
        ItemStack item1 = toItem(Material.BOAT,null,1);
        MATERIAL.addLore(item1,MATERIAL_TYPE.WOOD,500);
        MATERIAL.addLore(item1,MATERIAL_TYPE.IRON,100);
        inv.setItem(0,item1);
    }
    public  void OperateCraftingTable(InventoryClickEvent event){
        Inventory inv = event.getInventory();
        InventoryHolder inventoryHolder = inv.getHolder();
        if(!(inventoryHolder instanceof CraftingTableHolder)) {
            return;
        }
        int index = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        CraftingTableHolder craftingTableHolder = (CraftingTableHolder) inventoryHolder;
        event.setCancelled(true);
        if(craftingTableHolder.getSubgui() == null) {
            if (index <= 8 && index>=0) {
                Inventory sub_inv = null;
                switch (index) {
                    case 0:
                        //工具
                        sub_inv = Bukkit.createInventory(new CraftingTableHolder(null, 0), 36, "工具");
                        InitToolInventory(sub_inv,player);
                        break;
                    case 1:
                        //消耗品
                        sub_inv = Bukkit.createInventory(new CraftingTableHolder(null, 1), 36, "材料");
                        InitMaterialInventory(sub_inv,player);
                        break;
                    case 2:
                        //子弹
                        sub_inv = Bukkit.createInventory(new CraftingTableHolder(null, 2), 36, "子弹");
                        InitAmmoInventory(sub_inv,player);
                        break;
                    case 3:
                        //武器
                        sub_inv = Bukkit.createInventory(new CraftingTableHolder(null, 3), 36, "武器");
                        InitWeaponInventory(sub_inv,player);
                        break;
                    case 4:
                        //护甲
                        sub_inv = Bukkit.createInventory(new CraftingTableHolder(null, 4), 36, "护甲");
                        InitArmorInventory(sub_inv,player);
                        break;

                    case 5:
                        //建造方块
                        sub_inv = Bukkit.createInventory(new CraftingTableHolder(null, 5), 36, "建造方块");
                        InitRustBlockInventory(sub_inv,player);
                        break;
                    case 6:
                        //爆炸物
                        sub_inv = Bukkit.createInventory(new CraftingTableHolder(null, 6), 36, "爆炸物");
                        InitTNTInventory(sub_inv,player);
                        break;
                    case 7:
                        sub_inv = Bukkit.createInventory(new CraftingTableHolder(null, 6), 36, "载具");
                        InitVehicleInventory(sub_inv,player);
                        break;
                    case 8:
                        //测试
                        if(player.isOp()){
                            sub_inv = Bukkit.createInventory(new CraftingTableHolder(null, 7), 36, "测试");
                            InitTestInventory(sub_inv,player);
                        }
                        break;

                    default:
                        break;

                }
                if (sub_inv != null) {
                    player.openInventory(sub_inv);
                }
            }
        }else {
            if(index <= 35 && index>=0) {
                ItemStack item = inv.getItem(index);
                int sub_inv_index = craftingTableHolder.getSubgui();
                if(sub_inv_index == 8){
                    buyGoods(player,inv,index);
                }else{
                    if(item!=null){
                        if( item.getType() == Material.STAINED_GLASS_PANE
                                && !Blueprint.hasBlueprint(player, item)){
                            return;
                        }
                        buyGoods(player,inv,index);
                    }
                }


            }
        }

    }
}