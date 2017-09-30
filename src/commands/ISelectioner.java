package commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;

import org.bukkit.block.Block;

import ru.ocelotjungle.PLog.PLog;
import ru.ocelotjungle.PLog.Record;

public interface ISelectioner {
	
	Statement statement = PLog.statement;
	
	public static LinkedHashSet<Record> getPlayerList(Block playerLoc, String[] args) {
		LinkedHashSet<Record> result = new LinkedHashSet<>();
		try {
			HashSet<Integer> excludedPlayers = args.length >= 4 ? parsePlayers(args[3]) : new HashSet<>();
			int wid = statement.executeQuery("SELECT id FROM 'worlds' WHERE name = '" + playerLoc.getWorld().getName() + "'").getInt("id");
			ResultSet resultSet = statement.executeQuery("SELECT time, pid, x, y, z FROM 'logs' WHERE time >= " + calcTime(args[1]) + " AND wid = " + wid);
			while(resultSet.next()) {
				Record record = new Record(resultSet.getLong("time"), resultSet.getInt("pid"), resultSet.getInt("x"), resultSet.getInt("y"), resultSet.getInt("z"));
				if (!excludedPlayers.contains(record.pid) && Math.sqrt(Math.pow(record.x - playerLoc.getX(), 2) + Math.pow(record.y - playerLoc.getY(), 2) + Math.pow(record.z - playerLoc.getZ(), 2)) <= Integer.parseInt(args[2])) {
					result.add(record);
				}
				
			}
			resultSet.close();
			return result;
		}
		catch (SQLException | NumberFormatException me) { me.printStackTrace(); }
		return null;
	}
	
	static long calcTime(String time) {
		return Math.floorDiv((new Date()).getTime(), 1000) - (long)(Float.parseFloat(time) * 3600);
	}
	
	static HashSet<Integer> parsePlayers(String players) throws SQLException {
		try {
			HashSet<Integer> result = new HashSet<>();
			for (String player : players.split(",")) {
				try {
					int pid = statement.executeQuery("SELECT id FROM 'players' WHERE name = '" + player + "'").getInt("id");
					result.add(pid);
				}
				catch (SQLException sqle) {}
			}
			return result;
		}
		catch (NumberFormatException me) { me.printStackTrace(); }
		return null;
	}
}
