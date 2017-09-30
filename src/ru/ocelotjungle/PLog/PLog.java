package ru.ocelotjungle.PLog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class PLog extends JavaPlugin {
	
	public static Server server;
	private Logger loggerThread = new Logger();

	private static Connection connection;
	public static Statement statement;
	
	static {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:PLog.s3db");
			statement = connection.createStatement();
		}
		catch (ClassNotFoundException | SQLException me) { me.printStackTrace(); }
	}
	
	public void onEnable() {
		server = getServer();
		try {
			initDatabase();
			loggerThread.start();
			server.getPluginManager().registerEvents(new PlayerChecker(), this);
		} catch (SQLException me) { me.printStackTrace(); } // me = MultipleException, not Me
		getCommand("plog").setExecutor(new CommandExecutor());
	}
	
	private void initDatabase() throws SQLException {
		statement.execute("CREATE TABLE if not exists 'worlds' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text UNIQUE);");
		statement.execute("CREATE TABLE if not exists 'players' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'name' text UNIQUE);");
		statement.execute("CREATE TABLE if not exists 'logs' ('time' INTEGER, 'wid' INTEGER, 'pid' INTEGER, 'x' INTEGER, 'y' INTEGER, 'z' INTEGER);");
		for(World world : getServer().getWorlds()) {
			try {
				statement.execute("INSERT INTO 'worlds' ('name') VALUES ('" + world.getName() + "');");
			}
			catch (SQLException sqle) {}
		}
	}
	
	public void onDisable() {
		loggerThread.interrupt();;
		try {
			statement.close();
			connection.close();
		}
		catch (SQLException sqle) { sqle.printStackTrace(); }
	}
}
