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
		LinkedHashSet<Record> records = ISelectioner.getRecordList(player.getLocation().getBlock(), args);
		Record[] recordArray = new Record[records.size()];
		recordArray = records.toArray(recordArray);
		Record temp;
		for (int i = 0; i < Math.floorDiv(recordArray.length, 2); i++) {
			temp = recordArray[i];
			recordArray[i] = recordArray[recordArray.length - i - 1];
			recordArray[recordArray.length - i - 1] = temp;
		}
		Page.setSelection(player, recordArray);
		new Page(player, new String[]{null, "1"}).start();
	}
}
