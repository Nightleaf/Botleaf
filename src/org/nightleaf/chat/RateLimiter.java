package org.nightleaf.chat;

import java.util.LinkedList;

import org.nightleaf.Main;
import org.nightleaf.data.Config;
import org.nightleaf.model.ChatMessage;
import org.nightleaf.ui.UIUtility;

/**
 * This class is designed to throttle the amount of messages sent to the server
 * within a 30 second time period.
 * 
 * @author Nightleaf
 *
 */
public class RateLimiter implements Runnable {

    /**
     * The amount of ticks this session.
     */
    public int ticks = 0;

    /**
     * The amount of messages we have remaining to send in this session.
     */
    public int messageStock = 20;

    /**
     * The default amount of messages we can send in a session.
     */
    public final int DEFAULT_MESSAGE_STOCK = 20;

    /**
     * The message queue.
     */
    public LinkedList<ChatMessage> queue = new LinkedList<ChatMessage>();

    /**
     * Adds a message to the queue.
     * 
     * @param channel
     *            The channel the message is to be sent to.
     * @param message
     *            The message being sent.
     */
    public void addMessage(String channel, String message) {
	ChatMessage chatMessage = new ChatMessage(Config.getConfig("BOT_NAME"),
		channel, true, false, message);
	queue.add(chatMessage);
	System.out.println("[DEBUG] Added queued message (Channel: " + channel
		+ ", message: " + message + ")");
    }

    public void run() {
	while (true) {
	    try {
		ticks++;
		if (ticks == 30) {
		    messageStock = 20;
		    ticks = 0;
		}

		if (messageStock > 0) {
		    if (queue.size() > 0) {
			ChatMessage message = queue.get(0);
			if (message != null) {
			    Main.bl.sendMessage(message.getChannel(),
				    message.getMessage());
			    UIUtility.addChatMessage(message.getChannel(),
				    Config.getConfig("BOT_NAME").toLowerCase(),
				    message.getMessage());
			    System.out.println("[DEBUG] Message stock: "
				    + messageStock + ", ticks: " + ticks);
			    queue.remove(0);
			    messageStock--;
			}
		    }
		}

		Thread.sleep(1000l);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }
}
