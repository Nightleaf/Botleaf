package org.nightleaf.ui;

import java.io.IOException;
import java.util.Random;

import javax.swing.DefaultListModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.nightleaf.Main;
import org.nightleaf.chat.ChatManager;
import org.nightleaf.chat.ChatModeration;
import org.nightleaf.model.ChatMember;
import org.nightleaf.util.Utility;

public class UIUtility {
    static boolean isHTML = true;

    public static void updateTitle(String channel, int chatters, int messages) {
	Main.ngui.setTitle("Botleaf - " + channel + " - Messages: " + messages
		+ " - Chatters: " + chatters);
    }

    public static void addChatMessage(String channel, String sender,
	    String message) {
	boolean isOp = ChatModeration.isOp(channel, sender);

	if (!isHTML) {
	    if (!isOp)
		Append("[" + Utility.getTimeStamp() + "] <" + sender + ">: "
			+ message);
	    else
		Append("[" + Utility.getTimeStamp() + "] [MOD] <" + sender
			+ ">: " + message);
	    Append("\n");
	} else {
	    if (sender.equalsIgnoreCase("nightleaf")) {
		AppendHTML("<font color='white'>["
			+ Utility.getTimeStamp()
			+ "] <img src='http://localhost/twitch/icons/broadcaster.png' align='absmiddle' height='18' width='18'></img> ["
			+ getFormattedUser(sender) + "]: " + message
			+ "</font>");
	    } else if (isOp) {
		AppendHTML("<font color='white'>["
			+ Utility.getTimeStamp()
			+ "] <img src='http://localhost/twitch/icons/mod.png' align='absmiddle' height='18' width='18'></img> ["
			+ getFormattedUser(sender) + "]: " + message
			+ "</font>");
	    } else {
		AppendHTML("<font color='white'>[" + Utility.getTimeStamp()
			+ "] [" + getFormattedUser(sender) + "]: " + message
			+ "</font>");
	    }
	}
    }

    public static void Append(String message) {
	try {
	    Document doc = Main.ngui.editorPane.getDocument();
	    doc.insertString(doc.getLength(), message, null);
	    Main.ngui.editorPane.setCaretPosition(Main.ngui.editorPane
		    .getDocument().getLength());
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public static void AppendHTML(String message) {
	try {
	    HTMLDocument doc = (HTMLDocument) Main.ngui.editorPane
		    .getDocument();
	    HTMLEditorKit editorKit = (HTMLEditorKit) Main.ngui.editorPane
		    .getEditorKit();
	    String text = message;
	    editorKit.insertHTML(doc, doc.getLength(), text, 0, 0, null);
	    Main.ngui.editorPane.setCaretPosition(Main.ngui.editorPane
		    .getDocument().getLength());
	} catch (BadLocationException | IOException e) {
	    e.printStackTrace();
	}
    }

    public static String getFormattedUser(String sender) {
	ChatMember cm = ChatManager.getChatMemberByName(sender);
	if (cm == null) {
	    return "<b><font color='" + getRandomTwitchColor() + "'>" + sender
		    + "</font></b>";
	}
	return "<b><font color='" + cm.getChatColor() + "'>" + sender
		+ "</font></b>";
    }

    public static String getRandomTwitchColor() {
	Random random = new Random();
	String[] colors = { "#E05B5B", "#3DB974", "#0CB0D9", "#00FF7F",
		"#00E700", "#5F9EA0", "#FF581A" };
	return colors[random.nextInt(colors.length)];
    }

    public static void addChatter(String nick) {
	nick = nick.toLowerCase();
	if (nick.equalsIgnoreCase("system")) {
	    return;
	}
	ChatManager.addChatMember(nick);
	setChatters(Main.ngui);
    }

    @SuppressWarnings("unchecked")
    public static void setChatters(NGUI gui) {
	DefaultListModel<String> model = new DefaultListModel<String>();
	for (ChatMember cm : ChatManager.chattersList) {
	    if (!model.contains(cm))
		model.addElement(cm.getNick());
	}
	gui.chattersList.setModel(model);
    }
}
