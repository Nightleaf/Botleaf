package org.nightleaf.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.nightleaf.chat.ChatModeration;

public class ChatLog {

    public static void appendMessage(String nick, String channel, String message) {
	int isOp = ChatModeration.isOp(channel, nick) == true ? 1 : 0;
	boolean isSub = false;
	Date now = new Date();
	Timestamp ts = new Timestamp(now.getTime());

	int messageId = countMessages() + 1;

	SQLManager
		.update("INSERT INTO `twitch`.`chatlog` (`id`, `date`, `nick`, `sub`, `mod`, `message`) VALUES ('"
			+ messageId
			+ "', '"
			+ ts.toString()
			+ "', '"
			+ nick
			+ "', 0, " + isOp + ", '" + message + "');");
    }

    public static int countMessages() {
	int count = 0;
	ResultSet rs = SQLManager.query("SELECT * FROM `twitch`.`chatlog`");
	try {
	    rs.last();
	    count = rs.getRow();
	    rs.beforeFirst();
	    return count;
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return count;
    }

}
