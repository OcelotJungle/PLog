package ru.ocelotjungle.PLog;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import commands.Check;
import commands.Page;
import commands.Who;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player && label.equalsIgnoreCase("plog") && args.length > 0 && (sender.hasPermission("plog.*") || sender.hasPermission("plog.basic"))) {
			Player player = (Player)sender;
			if (args[0].equalsIgnoreCase("who") && (sender.hasPermission("plog.*") || sender.hasPermission("plog.who"))) {
				if (args.length >= 3) {
					new Who(player, args).start();
				} else {
					sender.sendMessage(ChatColor.RED + "Usage: /plog who <hours ago> <radius> [excluded players]");
				}
				return true;
			} else if (args[0].equalsIgnoreCase("check") && (sender.hasPermission("plog.*") || sender.hasPermission("plog.check"))) {
				if (args.length >= 3) {
					new Check(player, args).start();
				} else {
					sender.sendMessage(ChatColor.RED + "Usage: /plog check <hours ago> <radius> [excluded players]");
				}
				return true;
			} else if (args[0].equalsIgnoreCase("page") && (sender.hasPermission("plog.*") || sender.hasPermission("plog.page"))) {
				if (args.length >= 2) {
					new Page(player, args).start();
				} else {
					sender.sendMessage(ChatColor.RED + "Usage: /plog page <page>");
				}
				return true;
			}
		}
		sender.sendMessage(ChatColor.RED + "Usage: /plog <who|check|page> ...");
		return true;
	}
}
