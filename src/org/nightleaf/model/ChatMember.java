package org.nightleaf.model;

import java.util.Date;

import org.nightleaf.ui.UIUtility;

public class ChatMember {

    private String nick;
    private boolean isMod;
    private boolean isSub;
    private boolean isTurbo;
    private String chatColor;
    private long lastMessage;
    private Date lastMessageDate;
    private String[] lastMessages;
    private int chatMessageCount;
    private int warnings;

    public ChatMember(String nick) {
	this.nick = nick;
	this.chatMessageCount = 0;
	this.warnings = 0;
	this.chatColor = UIUtility.getRandomTwitchColor();
    }

    public void onMessage(String channel, String message) {
	chatMessageCount++;
    }

    public String getNick() {
	return nick;
    }

    public boolean isMod() {
	return isMod;
    }

    public boolean isSub() {
	return isSub;
    }

    public boolean isTurbo() {
	return isTurbo;
    }

    public String getChatColor() {
	return chatColor;
    }

    public long getLastMessage() {
	return lastMessage;
    }

    public Date getLastMessageDate() {
	return lastMessageDate;
    }

    public String[] getLastMessages() {
	return lastMessages;
    }

    public int getChatMessageCount() {
	return chatMessageCount;
    }

    public int getWarnings() {
	return warnings;
    }

}
