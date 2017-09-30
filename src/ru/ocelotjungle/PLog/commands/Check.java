package ru.ocelotjungle.PLog.commands;

import java.util.LinkedHashSet;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ru.ocelotjungle.PLog.Record;

public class Check extends Thread {
	
	private Player player;
	private String[] args;
	
	public Check(Player player, String[] args) {
		this.player = player;
		this.args = args;
	}

	@Override
	public void run() {
		player.sendMessage(ChatColor.GRAY + "Searching started, please wait...");
		LinkedHashSet<Record> records = ISelectioner.getPlayerList(player.getLocation().getBlock(), args);
		Record[] recordArray = new Record[records.size()];
		Page.setSelection(player, records.toArray(recordArray));
		new Page(player, new String[]{"page", "1"}).start();
	}
}
