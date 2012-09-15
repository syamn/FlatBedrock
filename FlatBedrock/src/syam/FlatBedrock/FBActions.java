/**
 * FlatBedrock - Package: syam.FlatBedrock
 * Created: 2012/09/16 2:46:51
 */
package syam.FlatBedrock;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import syam.FlatBedrock.Util.Actions;

/**
 * FBActions (FBActions.java)
 * @author syam(syamn)
 */
public class FBActions {
	public static boolean flatBedrock(Player player, int radius){
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

					/*
					 * 覚え書き: 12/06/26現在、
					 * world.isChunkLoaded(x, z) は正常に動作しない (ほとんどの場所でfalseが返される)
					 * world.isChunkLoaded(world.getChunkAt(x, z)) は非常に時間がかかる
					 * (どうやら world.getChunkAt(x, z) の処理が非常に重いみたい)
					 * このことから、チャンクを読み込む時は
					 * world.getChunkAt(world.getBlockAt(x, 0, z)) でブロックデータを取り、それを引数に渡す
					 */
					if (world.isChunkLoaded(world.getChunkAt(world.getBlockAt(i, 0, j)))){
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
					if (world.isChunkLoaded(world.getChunkAt(world.getBlockAt(i, 0, j)))){
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
			return false;
		}

		return true;
	}
}
