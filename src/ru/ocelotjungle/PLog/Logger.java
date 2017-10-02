package ru.ocelotjungle.PLog;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;


public class Logger extends Thread {
	
	private Statement statement;
	private HashMap<String, Record> lastRecords = new HashMap<>();
	
	@Override
	public void run() {
		Date date;
		while(true) {
			statement = PLog.statement;
			Block location;
			int wid, pid;
			date = new Date();
			for(Player player : PLog.server.getOnlinePlayers()) {
				location = player.getLocation().getBlock();
				try {
					wid = statement.executeQuery("SELECT id FROM 'worlds' WHERE name = '" + player.getWorld().getName() + "'").getInt("id");
					pid = statement.executeQuery("SELECT id FROM 'players' WHERE name = '" + player.getName() + "'").getInt("id");
					Record newRecord, oldRecord;
					newRecord = new Record(wid, location.getX(), location.getY(), location.getZ());
					oldRecord = lastRecords.get(player.getName());
					if (oldRecord == null || !oldRecord.compare(newRecord)) {
						statement.execute(String.format(
								"INSERT INTO 'logs' ('time', 'wid', 'pid', 'x', 'y', 'z') VALUES (%d, %d, %d, %d, %d, %d)",
								Math.floorDiv(date.getTime(), 1000), wid, pid, location.getX(), location.getY(), location.getZ()));
						lastRecords.put(player.getName(), newRecord);
					}
				} catch (SQLException sqle) { sqle.printStackTrace(); }
			}
			try {
				Thread.sleep(10000);
			}
			catch (InterruptedException ie) { break; }
		}
	}
}
