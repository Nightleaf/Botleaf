package org.nightleaf;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.jibble.pircbot.PircBot;
import org.nightleaf.chat.ChatManager;
import org.nightleaf.chat.ChatModeration;
import org.nightleaf.chat.message.UniqueMessages;
import org.nightleaf.data.Config;
import org.nightleaf.model.ChatMessage;
import org.nightleaf.sql.ChatLog;
import org.nightleaf.ui.UIUtility;
import org.nightleaf.util.Utility;

public class Botleaf extends PircBot {

    public int messageCount = 0;

    /**
     * Logger
     */
    private static Logger logger = Logger.getLogger(Botleaf.class.getName());

    public Set<String> moderators = new HashSet<String>();

    public Botleaf() {
	this.setName(Config.getConfig("BOT_NAME"));
	ChatManager.addChatMember(Config.getConfig("BOT_NAME"));
    }

    public void onDisconnect() {
	while (!isConnected()) {
	    try {
		UIUtility.addChatMessage("#nightleaf", "SYSTEM",
			"Reconnecting to chat..");
		reconnect();
		Thread.sleep(60000);
	    } catch (Exception e) {
		UIUtility.addChatMessage("#nightleaf", "SYSTEM",
			"Reconnecting to chat in 60 seconds...");
	    }
	}
    }

    public void onMessage(String channel, String sender, String login,
	    String hostname, String message) {
	UIUtility.addChatter(sender);
	messageCount++;
	if (messageCount >= 2500) {
	    Main.ngui.editorPane.setText("");
	    UIUtility.addChatMessage(channel, "SYSTEM",
		    "Chat cleared!  (Reached 2500 messages)");
	}

	ChatMessage chatMessage = new ChatMessage(sender, channel,
		ChatModeration.isOp(channel, sender),
		ChatModeration.isSubbed(sender), message);

	ChatManager.addChatMember(sender);

	int activeChatters = ChatManager.chattersList.size() + 1;
	UIUtility.updateTitle(channel, activeChatters, messageCount);

	if (channel.toLowerCase().contains("nightleaf")) {
	    ChatLog.appendMessage(sender, channel, message);
	}
	UIUtility.addChatMessage(channel, sender, message);
	ChatModeration.moderateChat(channel, sender, message);
	String[] params = message.split(" ");
	String scriptCmd = params[0].substring(1);

	// If we arent listening to the nightleaf channel, we shouldnt be
	// moderating.
	if (!channel.equalsIgnoreCase("#nightleaf")) {
	    return;
	}

	boolean isOp = ChatModeration.isOp(channel, sender);

	if (params[0].startsWith("!"))
	    Main.scripter.invoke("command_" + scriptCmd, channel, sender,
		    params[0], isOp, message, params);
	switch (message) {

	case "!reload":
	    if (isOp) {
		Main.scripter.load();
		UniqueMessages.loadMessages();
		sendQueuedMessage(channel, "@" + sender + ", ");
	    }
	    break;

	case "!time": // Displays the broadcaster's current time in the chat.
	    sendQueuedMessage(channel, "@" + sender
		    + " - Nightleaf's time is now " + Utility.getTimeStamp()
		    + " MST.");
	    break;
	}

    }

    public void sendQueuedMessage(String channel, String message) {
	Main.limiter.addMessage(channel, message);
    }

    public void onPrivateMessage(String sender, String login, String hostname,
	    String message) {
	logger.severe("[WHISPER] <" + sender + ">: " + message);
    }

    public void onUserMode(String channel, String sourceNick,
	    String sourceLogin, String sourceHostname, String recipient) {
	// logger.info("[onMode] user = " + recipient + ", source = " +
	// sourceNick);
	recipient = recipient.split(" ")[2];
	moderators.add(recipient);
    }

    public void onOp(String channel, String sourceNick, String sourceLogin,
	    String sourceHostname, String recipient) {
	// logger.info("[onOp] user = " + recipient + ", source = " +
	// sourceNick);
    }

    public void onDeop(String channel, String sourceNick, String sourceLogin,
	    String sourceHostname, String recipient) {
	// logger.info("[onDeop] user = " + recipient + ", source = " +
	// sourceNick);
    }
}