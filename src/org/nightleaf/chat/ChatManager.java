package org.nightleaf.chat;

import java.util.ArrayList;
import java.util.List;

import org.nightleaf.model.ChatMember;

public class ChatManager {

    public static List<ChatMember> chattersList = new ArrayList<ChatMember>();
    public static List<String> permittedUsers = new ArrayList<String>();

    public static void addChatMember(String nick) {
	ChatMember cm = new ChatMember(nick);
	ChatMember d = getChatMemberByName(nick);
	if (d == null)
	    chattersList.add(cm);
    }

    /**
     * Adds a user permit to post links in the chat.
     * 
     * @param nick
     *            The user allowed to post links.
     */
    public static void addPermit(String nick) {
	if (permittedUsers.contains(nick)) {
	    return;
	}
	permittedUsers.add(nick);
    }

    /**
     * Removes a permit.
     * 
     * @param nick
     */
    public static void removePermit(String nick) {
	permittedUsers.remove(nick);
    }

    /**
     * Returns true if the user is permitted.
     * 
     * @param nick
     *            The nick who is permitted or not.
     * @return
     */
    public static boolean isPermitted(String nick) {
	return permittedUsers.contains(nick);
    }

    public static ChatMember getChatMemberByName(String nick) {
	for (ChatMember cm : chattersList) {
	    if (cm.getNick().equalsIgnoreCase(nick)) {
		return cm;
	    }
	}
	return null;
    }
}
