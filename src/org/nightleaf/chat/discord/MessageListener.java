package org.nightleaf.chat.discord;

import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

import org.nightleaf.chat.message.UniqueMessages;
import org.nightleaf.chat.message.UniqueMessages.UniqueMessage;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
	String channel = event.getTextChannel().getName().toLowerCase();
	String author = event.getAuthor().getUsername().toLowerCase();
	String message = event.getMessage().getContent();

	String[] param = message.split(" ");
	String command = "";
	String mention = "";

	if (message.startsWith("!")) {
	    command = param[0].substring(1);
	}

	UniqueMessage uMsg = UniqueMessages.getMessage(command);
	if (uMsg != null) {
	    System.out.println("!" + uMsg.getCommand() + " triggered.");
	    event.getTextChannel().sendMessage(uMsg.getMessage());
	}
    }
}
