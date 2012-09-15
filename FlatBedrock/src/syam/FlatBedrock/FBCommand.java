package syam.FlatBedrock;

import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import syam.FlatBedrock.Util.Actions;

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
		if (args.length >= 1 && (args[0].equalsIgnoreCase("reload") || args[0].startsWith("r"))){
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

		// pay コマンド
		if (args.length >= 1 && (args[0].equals("pay") || args[0].startsWith("p"))){
			if (!(sender instanceof Player)) {
				Actions.message(sender, null, "&cThis command cannot be run from the console!");
				return true;
			}
			Player player = (Player)sender;
			if (!sender.hasPermission("flatbedrock.pay")){
				Actions.message(sender, null, "&cYou don't have permission to use this!");
				return true;
			}

			if (!plugin.getConfigs().useVault){
				Actions.message(sender, null, "&cSorry, this command not available now!");
				return true;
			}

			// Pay cost
			boolean paid = false;
			double cost = plugin.getConfigs().payCost;

			if (cost > 0 ){
				paid = Actions.takeMoney(player.getName(), cost);
				if (!paid){
					Actions.message(null, player, "&cお金が足りません！ " + Actions.getCurrencyString(cost) + "必要です！");
					return true;
				}
			}

			int radius = plugin.getConfigs().payRadius;

			FBActions.flatBedrock(player, radius);

			String msg = "&a周囲、半径" + radius + "ブロックの岩盤を整地しました！";
			if (paid) msg = msg + " &c(-" + Actions.getCurrencyString(cost) + ")";
			Actions.message(null, player, msg);

			return true;
		}

		// 半径を指定して変換
		if (args.length == 1){
			if (!(sender instanceof Player)) {
				Actions.message(sender, null, "&cThis command cannot be run from the console!");
				return true;
			}
			Player player = (Player)sender;
			// Check Permission
			if (!sender.hasPermission("flatbedrock.use")){
				Actions.message(null, player, "&cYou don't have permission to use this!");
				return true;
			}

			int radius = 0;
			int maxRadius = plugin.getConfigs().maxRadius;

			// Check Args
			try{
				radius = Integer.parseInt(args[0].trim());
				// 最大値チェック
				if (radius > maxRadius){
					Actions.message(null, player, "You specified too big radius of "+radius+", radius was automatically limited to "+maxRadius);
					radius = maxRadius;
				}
			}catch (NumberFormatException ex){
				// 引数が整数ではない
				Actions.message(null, player, msgPrefix+"&c'"+args[0]+"' is not a number!");
				return true;
			}

			// 処理開始通知
			Actions.message(null, player, "&7Flatting bedrock in radius: " + radius + "..");

			FBActions.flatBedrock(player, radius);

			// 完了通知
			Actions.message(null, player, "&aFlattened bedrock in radius: " + radius + "!");
			return true;
		}

		// コマンドヘルプを表示
		Actions.message(sender, null, "&c===================================");
		Actions.message(sender, null, "&bFlatBedrock Plugin version &3%version &bby syamn");
		Actions.message(sender, null, " &b<>&f = Required, &b[]&f = optional");
		Actions.message(sender, null, " /flatbedrock pay (/fbr p)&7 :");
		Actions.message(sender, null, "   &7- Pay cost and flattens bedrock");
		Actions.message(sender, null, " /flatbedrock <radius> (/fbr <radius>)&7 :");
		Actions.message(sender, null, "   &7- Flattens bedrock in specified radius");
		Actions.message(sender, null, " /flatbedrock reload (/fbr r)&7 :");
		Actions.message(sender, null, "   &7- Reload configs from config.yml");
		Actions.message(sender, null, "&c===================================");

		return true;
	}
}
