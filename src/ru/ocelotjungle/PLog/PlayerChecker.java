package ru.ocelotjungle.PLog;

import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerChecker implements Listener {
	
	private Statement statement;
	private static boolean first = true;
	
	@EventHandler
	public void onPlayerJoined(PlayerJoinEvent evt) {
		statement = PLog.statement;
		try {
			if (first) {
				initDatabase();
				first = false;
			}
			statement.execute("INSERT INTO 'players' ('name') VALUES ('" + evt.getPlayer().getName() + "');");
		}
		catch (SQLException sqle) {}
	}
	
	private void initDatabase() throws SQLException {
		statement.execute("CREATE TABLE if not exists 'worlds' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text UNIQUE);");
		statement.execute("CREATE TABLE if not exists 'players' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text UNIQUE);");
		statement.execute("CREATE TABLE if not exists 'logs' ('time' INTEGER, 'wid' INTEGER, 'pid' INTEGER, 'x' INTEGER, 'y' INTEGER, 'z' INTEGER);");
		for(World world : PLog.server.getWorlds()) {
			try {
				statement.execute("INSERT INTO 'worlds' ('name') VALUES ('" + world.getName() + "');");
			}
			catch (SQLException sqle) {}
		}
	}
}
