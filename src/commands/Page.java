package commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ru.ocelotjungle.PLog.PLog;
import ru.ocelotjungle.PLog.Record;

public class Page extends Thread {
	
	private Player player;
	private String[] args;
	private static HashMap<String, Record[]> selection = new HashMap<>();
	private static Date date = new Date();
	
	private Statement statement = PLog.statement;
	
	public Page(Player player, String[] args) {
		this.player = player;
		this.args = args;
	}

	@Override
	public void run() {
		Record[] lines = selection.get(player.getName());
		int page = Integer.parseInt(args[1]);
		if (page >= 1 && lines.length >= (page - 1) * 10) {
			player.sendMessage(ChatColor.GOLD + "Page " + page + "/" + (lines.length / 10 + 1));
			int limit = Math.min(lines.length, page * 10 - 1);
			for (int i = (page - 1) * 10; i < limit; i++) {
				String playerName = "";
				try {
					ResultSet resultSet = statement.executeQuery("SELECT name FROM 'players' WHERE id = " + lines[i].pid);
					playerName = resultSet.getString("name");
					resultSet.close();
				}
				catch (SQLException me) { me.printStackTrace(); }
				date.setTime(lines[i].time * 1000);
				player.sendMessage(String.format(
						" -- " + ChatColor.AQUA + "%s, %s: %d %d %d",
						new SimpleDateFormat("dd.MM.YY HH:mm:ss").format(date), playerName, lines[i].x, lines[i].y, lines[i].z));
			}
			player.sendMessage(ChatColor.GRAY + " - Use /plog page <page>");
		}
	}
	
	public static void setSelection(Player player, Record[] selection) {
		Page.selection.put(player.getName(), selection);
	}

}
