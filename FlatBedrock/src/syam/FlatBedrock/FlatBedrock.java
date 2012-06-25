package syam.FlatBedrock;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FlatBedrock extends JavaPlugin {
	// Logger
	public final static Logger log = Logger.getLogger("Minecraft");
	public final static String logPrefix = "[FlatBedrock] ";
	public final static String msgPrefix = "&c[FlatBedrock] &f";

	// Private classes
	private ConfigurationManager config;

	// Listener
	private final FBPlayerListener playerListener = new FBPlayerListener(this);

	// Instance
	private static FlatBedrock instance;

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
