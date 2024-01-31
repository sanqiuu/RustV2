package com.sanqiu.rustv2.recipe;

import com.sanqiu.rustv2.RustV2;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

public class RecipeWorkBench {
    public static void Create(){
        ItemStack workbench = new ItemStack(Material.WORKBENCH);
        Plugin plugin = RustV2.getPlugin();
        NamespacedKey key = new NamespacedKey(plugin, "workbench");
        NamespacedKey key2 = new NamespacedKey(plugin, "workbench2");
        ShapedRecipe recipe = new ShapedRecipe(key,workbench);
        ShapedRecipe recipe2 = new ShapedRecipe(key2,workbench);
        recipe.shape("AA", "AA");
        recipe2.shape("AA", "AA");
        recipe.setIngredient('A', Material.LOG);
        recipe2.setIngredient('A', Material.LOG_2);
        plugin.getServer().addRecipe(recipe);
        plugin.getServer().addRecipe(recipe2);
    }
}