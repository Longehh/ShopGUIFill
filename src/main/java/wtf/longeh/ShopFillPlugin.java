package wtf.longeh;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import wtf.longeh.cache.ShopPriceCache;
import wtf.longeh.listeners.ShopFillHandler;

public class ShopFillPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        setupEconomy();
        this.shopPriceCache = new ShopPriceCache(this);

        Bukkit.getPluginManager().registerEvents(new ShopFillHandler(this), this);
    }

    public Economy getEcon() {
        return econ;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public ShopPriceCache getShopPriceCache() {
        return shopPriceCache;
    }

    private Economy econ;
    private ShopPriceCache shopPriceCache;
}
