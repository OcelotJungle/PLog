package ru.ocelotjungle.PLog;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public class PLog extends JavaPlugin {
	
	public static Server server;
	private static java.util.logging.Logger logger;
	private Logger loggerThread = new Logger();

	private static Connection connection;
	public static Statement statement;
	
	
	
	public void onEnable() {
		new File(getDataFolder().toString()).mkdir();
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + getDataFolder() + File.separator + "PLog.s3db");
			statement = connection.createStatement();
		}
		catch (ClassNotFoundException | SQLException me) { me.printStackTrace(); }
		server = getServer();
		logger = server.getLogger();
		loggerThread.start();
		server.getPluginManager().registerEvents(new PlayerChecker(), this);
		getCommand("plog").setExecutor(new CommandExecutor());
	}
	
	public void onDisable() {
		loggerThread.interrupt();;
		try {
			statement.close();
			connection.close();
		}
		catch (SQLException sqle) { sqle.printStackTrace(); }
	}
	
	public static void log(Object Log) {
		logger.info(Log.toString());
	}
}
