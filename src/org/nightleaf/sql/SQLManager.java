package org.nightleaf.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Nightleaf
 *
 */
public class SQLManager {

    /**
     * Whether or not the SQL system is running.
     */
    public static boolean isRunning = false;

    /**
     * Whether or not the SQL system has started.
     */
    public static boolean hasStarted = false;

    /**
     * Sets the default database to connect to, but we can switch databases with
     * this.
     */
    public static String database = "emeraldbot";

    /**
     * The last time a query was sent.
     */
    public static long lastQuery;

    /**
     * The SQL connection
     */
    private static Connection connection = null;

    /**
     * The SQL statement.
     */
    private static Statement statement = null;

    public static void init() {
	connect();
    }

    /**
     * Connects to the SQL server.
     */
    public static void connect() {
	String URL = "jdbc:mysql://localhost/" + getDatabase();
	try {
	    Class.forName("com.mysql.jdbc.Driver").newInstance();
	    connection = DriverManager.getConnection(URL, "root", "");
	    statement = connection.createStatement();
	} catch (InstantiationException e) {
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Checks to see if the connection is still active.
     *
     * @return
     */
    public static boolean isConnected() {
	try {
	    return !connection.isClosed();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return false;
    }

    /**
     * Executes an update to a table on the sql server.
     *
     * @param update
     * @return
     */
    public static int update(String update) {
	if (!isConnected()) {
	    connect();
	}
	try {
	    return statement.executeUpdate(update);

	} catch (Exception e) {
	    e.printStackTrace();
	}
	return -1;
    }

    /**
     * Executes a query and returns results from the SQL server.
     *
     * @param query
     * @return
     */
    public static ResultSet query(String query) {
	if (!isConnected()) {
	    connect();
	}
	try {

	    return statement.executeQuery(query);

	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    public static boolean isRunning() {
	return isRunning;
    }

    public static String getDatabase() {
	return database;
    }

    public static void setDatabase(String database) {
	SQLManager.database = database;
    }

    public static long getLastQuery() {
	return lastQuery;
    }

    public static Connection getConnection() {
	return connection;
    }

    public static Statement getStatement() {
	return statement;
    }
}
