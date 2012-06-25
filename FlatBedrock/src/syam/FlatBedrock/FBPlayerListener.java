package syam.FlatBedrock;

import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


public class FBPlayerListener implements Listener{
	public final static Logger log = FlatBedrock.log;
	private final static String logPrefix = FlatBedrock.logPrefix;
	private final static String msgPrefix = FlatBedrock.msgPrefix;

	private final FlatBedrock plugin;

	public FBPlayerListener(FlatBedrock plugin){
		this.plugin = plugin;
	}

	/* 登録するイベントはここから下に */
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event){
		Player player = event.getPlayer();

		// 権限チェック
		if (player.hasPermission("flatbedrock.auto")){

			// configs
			int checkRadius = plugin.getConfigs().checkRadius;
			int maxRadius = plugin.getConfigs().maxRadius;

			// player locations
			World world = player.getWorld();
			int x = player.getLocation().getBlockX();
			int z = player.getLocation().getBlockZ();

			// 通常ワールド
			if (world.getEnvironment() == Environment.NORMAL){
				// 1列ずつ半径内を走査
				for (int i = x - checkRadius; i < x + checkRadius; i++){
					for (int j = z - checkRadius; j < z + checkRadius; j++){
						// Y=0 岩盤でなければ岩盤に変換
						if (world.getBlockAt(i, 0, j).getTypeId() != 7) world.getBlockAt(i, 0, j).setTypeId(7);
						// Y=1～4 岩盤があれば石に変換
						if (world.getBlockAt(i, 1, j).getTypeId() == 7) world.getBlockAt(i, 1, j).setTypeId(1);
						if (world.getBlockAt(i, 2, j).getTypeId() == 7) world.getBlockAt(i, 2, j).setTypeId(1);
						if (world.getBlockAt(i, 3, j).getTypeId() == 7) world.getBlockAt(i, 3, j).setTypeId(1);
						if (world.getBlockAt(i, 4, j).getTypeId() == 7) world.getBlockAt(i, 4, j).setTypeId(1);
					}
				}
			} // ネザーワールド
			else if (world.getEnvironment() == Environment.NETHER) {
				// 1列ずつ半径内を走査
				for (int i = x - checkRadius; i < x + checkRadius; i++){
					for (int j = z - checkRadius; j < z + checkRadius; j++){
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
						// Y=1～4 岩盤があればネザーラックに変換
						if (world.getBlockAt(i, 126, j).getTypeId() == 7) world.getBlockAt(i, 126, j).setTypeId(87);
						if (world.getBlockAt(i, 125, j).getTypeId() == 7) world.getBlockAt(i, 125, j).setTypeId(87);
						if (world.getBlockAt(i, 124, j).getTypeId() == 7) world.getBlockAt(i, 124, j).setTypeId(87);
						if (world.getBlockAt(i, 123, j).getTypeId() == 7) world.getBlockAt(i, 123, j).setTypeId(87);
					}
				}
			}
		}
	}
}
