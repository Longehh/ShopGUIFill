package wtf.longeh.cache;

import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
import net.brcdev.shopgui.ShopGuiPlugin;
import net.brcdev.shopgui.shop.Shop;
import net.brcdev.shopgui.shop.ShopItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import wtf.longeh.ShopFillPlugin;

import java.util.HashMap;
import java.util.Map;

public class ShopPriceCache {
    private final int taskID;
    private ShopFillPlugin shopFillPlugin;

    public ShopPriceCache(ShopFillPlugin shopFillPlugin) {
        this.shopFillPlugin = shopFillPlugin;
        this.shopPrices = new HashMap<>();

        this.taskID = Bukkit.getScheduler().runTaskTimer(shopFillPlugin, this::loadShopPrices, 20L, 20L).getTaskId();
    }

    private void loadShopPrices() {
        if(!ShopGuiPlugin.getInstance().getShopManager().shops.isEmpty()) {
            for(Map.Entry<String, Shop> shopEntry : ShopGuiPlugin.getInstance().getShopManager().shops.entrySet()) {
                for(ShopItem shopItem : shopEntry.getValue().getShopItems()) {
                    shopPrices.put(shopItem.getItem().getType() + ":" + shopItem.getItem().getDurability(), shopItem.getBuyPrice());
                }
            }
            Bukkit.getScheduler().cancelTask(taskID);
        }
    }

    public double getPrice(ItemStack itemStack) {
        return this.shopPrices.getOrDefault(itemStack.getType() + ":" + itemStack.getDurability(), 0.0);
    }

    private HashMap<String, Double> shopPrices;
}
