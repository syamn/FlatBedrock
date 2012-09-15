package syam.FlatBedrock;

import java.util.logging.Logger;

import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class FlatBedrock extends JavaPlugin {
	// Logger
	public final static Logger log = Logger.getLogger("Minecraft");
	public final static String logPrefix = "[FlatBedrock] ";
	public final static String msgPrefix = "&6[FlatBedrock] &f";

	// Private classes
	private ConfigurationManager config;

	// Listener
	private final FBPlayerListener playerListener = new FBPlayerListener(this);

	// Instance
	private static FlatBedrock instance;

	// ** Hookup Plugins **
	public static Vault vault = null;
	public static Economy economy = null;

	/**
	 * プラグイン起動処理
	 */
	public void onEnable(){
		instance = this;
		config = new ConfigurationManager(this);
		PluginManager pm = getServer().getPluginManager();

		// load config
		try{
			config.loadConfig(true);
		}catch(Exception ex){
			log.warning(logPrefix+ "an error occured while trying to load the config file.");
			ex.printStackTrace();
		}

		// プラグインフック
		if (config.useVault){
			config.useVault = setupVault();
		}

		// Regist Listener
		pm.registerEvents(playerListener, this);

		// Regist Commands
		getServer().getPluginCommand("flatbedrock").setExecutor(new FBCommand(this));

		// メッセージ表示
		PluginDescriptionFile pdfFile = this.getDescription();
		log.info("["+pdfFile.getName()+"] version "+pdfFile.getVersion()+" is Enabled!");
	}

	/**
	 * プラグイン停止処理
	 */
	public void onDisable(){
		// メッセージ表示
		PluginDescriptionFile pdfFile = this.getDescription();
		log.info("["+pdfFile.getName()+"] version "+pdfFile.getVersion()+" is Disabled!");
	}

	/**
	 * Vaultプラグインにフック
	 */
	public boolean setupVault(){
		Plugin plugin = this.getServer().getPluginManager().getPlugin("Vault");
		if(plugin != null & plugin instanceof Vault) {
			RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			// 経済概念のプラグインがロードされているかチェック
			if(economyProvider==null){
	        	log.warning(logPrefix+"Economy plugin NOT found. Disabled Vault plugin integration.");
		        return false;
			}

			try{
				vault = (Vault) plugin;
				economy = economyProvider.getProvider();

				if (vault == null || economy == null){
				    throw new NullPointerException();
				}
			} // 例外チェック
			catch(Exception e){
				log.warning(logPrefix+"Could NOT be hook to Vault plugin. Disabled Vault plugin integration.");
		        return false;
			}

			// Success
			log.info(logPrefix+"Hooked to Vault plugin!");
			return true;
		}
		else {
			// Vaultが見つからなかった
	        log.warning(logPrefix+"Vault plugin was NOT found! Disabled Vault integration.");
	        return false;
	    }
	}

	/* getter */

	/**
	 * 設定マネージャを返す
	 * @return ConfigurationManager
	 */
	public ConfigurationManager getConfigs(){
		return config;
	}

	/**
	 * インスタンスを返す
	 * @return シングルトンインスタンス or null
	 */
	public static FlatBedrock getInstance(){
		return instance;
	}

}
