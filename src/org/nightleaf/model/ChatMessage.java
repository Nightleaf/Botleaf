package org.nightleaf.model;

public class ChatMessage {

    private String sender;
    private String channel;
    private boolean isOp;
    private boolean isSub;
    private String message;

    public ChatMessage(String sender, String channel, boolean isOp,
	    boolean isSub, String message) {
	this.sender = sender;
	this.channel = channel;
	this.isOp = isOp;
	this.isSub = isSub;
	this.message = message;
    }

    public String getSender() {
	return sender;
    }

    public String getChannel() {
	return channel;
    }

    public boolean isOp() {
	return isOp;
    }

    public boolean isSub() {
	return isSub;
    }

    public String getMessage() {
	return message;
    }

}
