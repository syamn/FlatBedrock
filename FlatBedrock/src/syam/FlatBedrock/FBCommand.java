package syam.FlatBedrock;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class FBCommand implements CommandExecutor {
	public final static Logger log = FlatBedrock.log;
	private final static String logPrefix = FlatBedrock.logPrefix;
	private final static String msgPrefix = FlatBedrock.msgPrefix;

	private final FlatBedrock plugin;
	public FBCommand(final FlatBedrock plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args){
		// 設定ファイル再読み込み
		if (args.length >= 1 && (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("r"))){
			if (!sender.hasPermission("flatbedrock.reload")){
				Actions.message(sender, null, "&cYou don't have permission to use this!");
				return true;
			}
			try{
				plugin.getConfigs().loadConfig(false);
			}catch(Exception ex){
				log.warning(logPrefix+ "an error occured while trying to load the config file.");
				ex.printStackTrace();
				return true;
			}
			Actions.message(sender, null, "&aConfiguration reloaded!");
			return true;
		}

		return false;
	}
}
