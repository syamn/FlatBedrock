package syam.FlatBedrock;

import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
				Actions.message(null, player, "&c'"+args[0]+"'is not a number!");
				return true;
			}

			// player locations
			World world = player.getWorld();
			int x = player.getLocation().getBlockX();
			int z = player.getLocation().getBlockZ();

			// ノーマルワールド
			if (world.getEnvironment() == Environment.NORMAL){
				// 1列ずつ半径内を走査
				for (int i = x - radius; i < x + radius; i++){
					for (int j = z - radius; j < z + radius; j++){
						// チャンクチェック チャンクが読み込まれていなければスキップする
						if (world.isChunkLoaded(i, j)){
							// Y=0 岩盤でなければ岩盤に変換
							if (world.getBlockAt(i, 0, j).getTypeId() != 7) world.getBlockAt(i, 0, j).setTypeId(7);
							// Y=1～4 岩盤があれば石に変換
							if (world.getBlockAt(i, 1, j).getTypeId() == 7) world.getBlockAt(i, 1, j).setTypeId(1);
							if (world.getBlockAt(i, 2, j).getTypeId() == 7) world.getBlockAt(i, 2, j).setTypeId(1);
							if (world.getBlockAt(i, 3, j).getTypeId() == 7) world.getBlockAt(i, 3, j).setTypeId(1);
							if (world.getBlockAt(i, 4, j).getTypeId() == 7) world.getBlockAt(i, 4, j).setTypeId(1);
						} // else { Actions.message(null, player, "check skipped x=" + i + ", z=" + j);}
					}
				}
			} // ネザーワールド
			else if(world.getEnvironment() == Environment.NETHER){
				// 1列ずつ半径内を走査
				for (int i = x - radius; i < x + radius; i++){
					for (int j = z - radius; j < z + radius; j++){
						// チャンクチェック チャンクが読み込まれていなければスキップする
						if (world.isChunkLoaded(i, j)){
							// Y=0 岩盤でなければ岩盤に変換
							if (world.getBlockAt(i, 0, j).getTypeId() != 7) world.getBlockAt(i, 0, j).setTypeId(7);
							// Y=1～4 岩盤があればネザーラックに変換
							if (world.getBlockAt(i, 1, j).getTypeId() == 7) world.getBlockAt(i, 1, j).setTypeId(87);
							if (world.getBlockAt(i, 2, j).getTypeId() == 7) world.getBlockAt(i, 2, j).setTypeId(87);
							if (world.getBlockAt(i, 3, j).getTypeId() == 7) world.getBlockAt(i, 3, j).setTypeId(87);
							if (world.getBlockAt(i, 4, j).getTypeId() == 7) world.getBlockAt(i, 4, j).setTypeId(87);

							// 上部分
							// Y=127 岩盤でなければ岩盤に変換
							if (world.getBlockAt(i, 127, j).getTypeId() != 7) world.getBlockAt(i, 127, j).setTypeId(7);
							// Y=126～123 岩盤があればネザーラックに変換
							if (world.getBlockAt(i, 126, j).getTypeId() == 7) world.getBlockAt(i, 126, j).setTypeId(87);
							if (world.getBlockAt(i, 125, j).getTypeId() == 7) world.getBlockAt(i, 125, j).setTypeId(87);
							if (world.getBlockAt(i, 124, j).getTypeId() == 7) world.getBlockAt(i, 124, j).setTypeId(87);
							if (world.getBlockAt(i, 123, j).getTypeId() == 7) world.getBlockAt(i, 123, j).setTypeId(87);
						}
					}
				}
			} //その他のワールド サポートしていない
			else{
				Actions.message(null, player, "&cThis Environment is not supported yet!");
				return true;
			}

			// 完了通知
			Actions.message(null, player, "&aFlattened bedrock in radius: " + radius);
			return true;
		}

		return false;
	}
}
