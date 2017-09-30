package ru.ocelotjungle.PLog;

import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerChecker implements Listener {
	
	private Statement statement = PLog.statement;
	
	@EventHandler
	public void onPlayerJoined(PlayerJoinEvent evt) {
		try {
			statement.execute("INSERT INTO 'players' ('name') VALUES ('" + evt.getPlayer().getName() + "');");
		}
		catch (SQLException sqle) {}
	}
}
