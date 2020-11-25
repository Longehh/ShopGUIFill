package wtf.longeh.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import wtf.longeh.ShopFillPlugin;
import wtf.longeh.utils.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

public class ShopFillHandler implements Listener {
    private ShopFillPlugin shopFillPlugin;

    public ShopFillHandler(ShopFillPlugin shopFillPlugin) {
        this.shopFillPlugin = shopFillPlugin;
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent e) {
        if (e.getInventory().getTitle().contains(C(shopFillPlugin.getConfig().getString("messages.guiTitle")))) {
            if (e.getInventory().getItem(shopFillPlugin.getConfig().getInt("itemSlot")).getType() == Material.MOB_SPAWNER) {
                return;
            }
            e.getInventory().setItem(shopFillPlugin.getConfig().getInt("fillAllSlot"),
                    new ItemBuilder(Material.getMaterial(shopFillPlugin.getConfig().getString("item.material")))
                            .setName(C(shopFillPlugin.getConfig().getString("item.name")))
                            .setLore(colourizeList(shopFillPlugin.getConfig().getStringList("item.lore"))).toItemStack());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory() == null || e.getCurrentItem() == null) {
            return;
        }
        if (e.getClickedInventory().getTitle().contains(C(shopFillPlugin.getConfig().getString("messages.guiTitle")))) {
            if (e.getCurrentItem().getType() == Material.getMaterial(shopFillPlugin.getConfig().getString("item.material"))) {
                Player player = (Player) e.getWhoClicked();
                ItemStack shopItem = e.getInventory().getItem(shopFillPlugin.getConfig().getInt("itemSlot"));
                double itemPrice = shopFillPlugin.getShopPriceCache().getPrice(shopItem);
                if(itemPrice == 0.0) {
                    player.sendMessage(C(shopFillPlugin.getConfig().getString("messages.cannotBuy")));
                    return;
                }
                int freeSlots = calculateSlots(e.getWhoClicked().getInventory());
                if (freeSlots != 0) {
                    int itemAmount = freeSlots * shopItem.getMaxStackSize();
                    double finalPrice = itemPrice * itemAmount;
                    if (shopFillPlugin.getEcon().getBalance(player) >= finalPrice) {
                        shopFillPlugin.getEcon().withdrawPlayer(player, finalPrice);
                        giveItem(freeSlots, player, new ItemStack(shopItem.getType(), shopItem.getMaxStackSize(), shopItem.getDurability()));
                        player.sendMessage(C(
                                shopFillPlugin.getConfig().getString("messages.boughtMessage")
                                        .replace("%itemAmount%", String.valueOf(itemAmount))
                                        .replace("%itemName%", getItemName(shopItem))
                                        .replace("%price%", String.valueOf(finalPrice))));
                    } else {
                        player.sendMessage(C(shopFillPlugin.getConfig().getString("messages.cannotAffordMessage")
                                .replace("%itemAmount%", String.valueOf(itemAmount))
                                .replace("%itemName%", getItemName(shopItem))
                                .replace("%price%", String.valueOf(finalPrice))));
                    }
                    return;
                }
                player.sendMessage(C(shopFillPlugin.getConfig().getString("messages.inventoryFullMessage")));
            }
        }
    }

    public static String getItemName(org.bukkit.inventory.ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        return nmsStack.getItem().a(nmsStack);
    }

    private int calculateSlots(Inventory inventory) {
        int slots = 0;
        for (ItemStack i : inventory.getContents()) {
            if (i == null) {
                slots++;
            }
        }
        return slots;
    }

    private void giveItem(int freeSlots, Player player, ItemStack stack) {
        for (int i = 1; i <= freeSlots; i++) {
            player.getInventory().addItem(stack);
        }
    }

    private List<String> colourizeList(List<String> stringList) {
        List<String> colourizedList = new ArrayList<>();
        for(String s : stringList) {
            colourizedList.add(C(s));
        }
        return colourizedList;
    }

    public String C(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
