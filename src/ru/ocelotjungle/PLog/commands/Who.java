package ru.ocelotjungle.PLog.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedHashSet;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ru.ocelotjungle.PLog.PLog;
import ru.ocelotjungle.PLog.Record;

public class Who extends Thread {
	
	private Player player;
	private String[] args;
	private Statement statement;
	
	public Who(Player player, String[] args) {
		this.player = player;
		this.args = args;
	}

	@Override
	public void run() {
		statement = PLog.statement;
		player.sendMessage(ChatColor.GRAY + "Searching started, please wait...");
		LinkedHashSet<Record> records = ISelectioner.getRecordList(player.getLocation().getBlock(), args);
		HashSet<String> loggedPlayers = new HashSet<String>();
		try {
			ResultSet resultSet;
			for (Record record : records) {
				resultSet = statement.executeQuery("SELECT name FROM 'players' WHERE id = " + record.pid);
				loggedPlayers.add(resultSet.getString("name"));
			}
		}
		catch (SQLException me) { me.printStackTrace(); }
		StringBuilder result = new StringBuilder();
		for(String player : loggedPlayers) {
			result.append(" " + player);
		}
		player.sendMessage(ChatColor.GOLD + "There was:" + result.toString());
	}

}