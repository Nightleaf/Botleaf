package org.nightleaf.chat.message;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.nightleaf.sql.SQLManager;

/**
 * Class that handles all the unique messages for advertisements and other
 * message related features.
 * 
 * @author Nightleaf
 *
 */
public class UniqueMessages {

    /*
     * Contains all of our advertisement messages.
     */
    public static List<UniqueMessage> uniqueMessages = new ArrayList<UniqueMessage>();

    /**
     * Loads all the messages into the 'adMessages' array.
     */
    public static void loadMessages() {
	uniqueMessages.clear();
	try {
	    ResultSet r = SQLManager
		    .query("SELECT * FROM `twitch`.`messages_unique`");
	    while (r.next()) {
		String command = r.getString("command");
		String message = r.getString("message");
		UniqueMessage uMsg = new UniqueMessage(command, message);
		uniqueMessages.add(uMsg);
	    }
	} catch (SQLException e) {

	}
    }

    /**
     * Retrieves a message based on the command.
     * 
     * @param command
     *            The command for the message.
     * @return
     */
    public static UniqueMessage getMessage(String command) {
	for (int i = 0; i < uniqueMessages.size(); i++) {
	    UniqueMessage um = uniqueMessages.get(i);
	    if (um.getCommand().equalsIgnoreCase(command)) {
		return um;
	    }
	}
	return null;
    }

    /**
     * Class that holds the advert message data.
     * 
     * @author Nightleaf
     *
     */
    public static class UniqueMessage {

	private String command;
	private String message;

	public UniqueMessage(String cmd, String msg) {
	    this.command = cmd;
	    this.message = msg;
	}

	public String getCommand() {
	    return command;
	}

	public String getMessage() {
	    return message;
	}
    }

}
