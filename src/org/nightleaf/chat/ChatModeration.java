package org.nightleaf.chat;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jibble.pircbot.User;
import org.nightleaf.Main;
import org.nightleaf.data.Config;
import org.nightleaf.ui.UIUtility;

public class ChatModeration {

    static boolean debug = false;

    static Map<String, Integer> warningsMap = new HashMap<String, Integer>();
    static Map<String, Integer> punishMap = new HashMap<String, Integer>();

    enum PunishmentType {
	TIMEOUT, PURGE
    }

    enum PunishmentReason {
		LINK, SPAM, SWEAR, RACISM, CAPS
    }

    /**
     * Logger
     */
    private static Logger logger = Logger.getLogger(ChatModeration.class
	    .getName());

    /**
     * Handles moderation for the channel's chatroom.
     * 
     * @param sender
     *            The sender of the message.
     * @param message
     *            The message.
     */
    public static void moderateChat(String channel, String sender,
	    String message) {
	boolean containsLinks = foundPattern(
		"[-a-zA-Z0-9@:%_\\+.~#?&//=]{2,256}\\.[a-z]{2,4}\\b(\\/[-a-zA-Z0-9@:%_\\+.~#?&//=]*)?",
		message);
	boolean containsTooManyCaps = getUppercaseCount(message) >= 15;
	boolean shouldPunish = false;
	boolean caps = false;
	boolean links = false;

	// If the sender is a mod or in debug mode, ignore.
	if (isOp(channel, sender) || debug) {
	    return;
	}
	if (containsLinks) {
	    if (isSubbed(sender)) {
		return;
	    }
	    UIUtility.addChatMessage(channel, "SYSTEM", "Link detected in "
		    + sender + "'s message.");
	    shouldPunish = true;
	    links = true;
	    caps = false;
	}
	if (containsTooManyCaps) {
	    UIUtility.addChatMessage(channel, "SYSTEM",
		    "Too many capital letters detected in " + sender
			    + "'s message.");
	    shouldPunish = true;
	    links = false;
	    caps = true;
	}

	if (shouldPunish && channel.equalsIgnoreCase("#nightleaf")) {
	    // Make sure we are a mod, otherwise its pointless to moderate!
	    if (isOp(channel, "botleaf")) {
		UIUtility.addChatMessage(channel, "SYSTEM",
			"Applying punishment to " + sender + ".");
		if (links) {
		    if (ChatManager.isPermitted(sender)) {
			ChatManager.removePermit(sender);
			return;
		    }
		    punish(channel, sender, PunishmentReason.LINK);
		} else if (caps)
		    punish(channel, sender, PunishmentReason.CAPS);
	    } else {
		UIUtility
			.addChatMessage(channel, "SYSTEM", "Unable to punish "
				+ sender
				+ ", reason: Not a moderator of this channel.");
	    }
	}
    }

    /**
     * Punishes a chatter.
     * 
     * @param channel
     *            The channel the user was in.
     * @param nick
     *            The nick of the chatter.
     * @param reason
     *            The reason they are getting punished.
     */
    public static void punish(String channel, String nick,
	    PunishmentReason reason) {
	int defaultTimeout = 600; // 10 minute timeout by default;
	int warnings = getWarningCount(nick);

	PunishmentType type = PunishmentType.PURGE;
	if (warnings >= 2) {
	    type = PunishmentType.TIMEOUT;
	}
	int duration = defaultTimeout * (warnings * 3);
	if (type.equals(PunishmentType.TIMEOUT)) {
	    Main.bl.sendQueuedMessage(channel, ".timeout " + nick + " "
		    + duration);
	    clearWarnings(nick);
	} else {
	    Main.bl.sendQueuedMessage(channel, ".timeout " + nick + " 1");
	    addWarning(nick, warnings + 1);
	}
	sendPunishMessage(channel, nick, duration, type, reason);

    }

    /**
     * Sends the punishment to the chat to let the accused know why they are
     * being punished.
     * 
     * @param channel
     *            The channel the accused is in.
     * @param nick
     *            The nick of the accused.
     * @param duration
     *            The duration of the punishment.
     * @param type
     *            The type of punishment.
     * @param reason
     *            The reason for the punishment.
     */
    public static void sendPunishMessage(String channel, String nick,
	    int duration, PunishmentType type, PunishmentReason reason) {
	if (debug) {
	    return;
	}
	int warnings = getWarningCount(nick);
	Main.bl.sendQueuedMessage(channel,
		getRandomPunishMessage(nick, warnings, duration, type, reason));
    }

    /**
     * Gets a random punishment message for flavor.
     * 
     * @param nick
     *            The nick of the accused.
     * @param warnings
     *            The amount of warnings the nick has.
     * @param duration
     *            The duration of the punishment.
     * @param type
     *            The type of punishment.
     * @param reason
     *            The reason for the punishment.
     * @return
     */
    public static String getRandomPunishMessage(String nick, int warnings,
	    int duration, PunishmentType type, PunishmentReason reason) {
	Random random = new Random();
	int r = random.nextInt(3);
	String message = "";
	duration = duration / 60;
	if (reason.equals(PunishmentReason.CAPS)) {
	    switch (r) {
	    case 0:
		if (type.equals(PunishmentType.PURGE))
		    message = "Watch the caps! @" + nick + "  [Warnings = "
			    + warnings + "]";
		else if (type.equals(PunishmentType.TIMEOUT))
		    message = "Watch the caps! @" + nick + ", chill for "
			    + duration + " minutes.";
		break;
	    case 1:
		if (type.equals(PunishmentType.PURGE))
		    message = "Mind the capital letters @" + nick
			    + "  [Warnings = " + warnings + "]";
		else if (type.equals(PunishmentType.TIMEOUT))
		    message = "Mind the capital letters @" + nick
			    + ", simmer down for " + duration + " minutes.";
		break;
	    case 2:
		if (type.equals(PunishmentType.PURGE))
		    message = "Quit it! @" + nick + ".  [Warnings = "
			    + warnings + "]";
		else if (type.equals(PunishmentType.TIMEOUT))
		    message = "Quit it! @" + nick + ".  Relax for " + duration
			    + " minutes.";
		break;
	    }
	} else if (reason.equals(PunishmentReason.LINK)) {
	    switch (r) {
	    case 0:
		if (type.equals(PunishmentType.PURGE))
		    message = "You can't post links here unless you have permission @"
			    + nick + "!  [Warnings = " + warnings + "]";
		else if (type.equals(PunishmentType.TIMEOUT))
		    message = "You can't post links here unless you have permission @"
			    + nick + ", chill for " + duration + " minutes.";
		break;
	    case 1:
		if (type.equals(PunishmentType.PURGE))
		    message = "NO! @" + nick + "!  [Warnings = " + warnings
			    + "]";
		else if (type.equals(PunishmentType.TIMEOUT))
		    message = "NO! @" + nick + "!  Simmer down for " + duration
			    + " minutes.";
		break;
	    case 2:
		if (type.equals(PunishmentType.PURGE))
		    message = "Quit it! @" + nick + ".  [Warnings = "
			    + warnings + "]";
		else if (type.equals(PunishmentType.TIMEOUT))
		    message = "Quit it! @" + nick + ".  Relax for " + duration
			    + " minutes.";
		break;
	    }
	}
	return message;
    }

    public static int getWarningCount(String nick) {
	int warnings = 0;
	if (warningsMap.get(nick) != null)
	    return warningsMap.get(nick);
	else
	    return warnings;
    }

    public static void addWarning(String nick, int warnings) {
	warningsMap.put(nick, warnings);
    }

    public static void clearWarnings(String nick) {
	warningsMap.put(nick, 0);
    }

    /**
     * Counts the amount of uppercase letters in the message.
     * 
     * @param message
     *            The message we are counting.
     * @return
     */
    public static int getUppercaseCount(String message) {
	int count = 0;
	for (int i = 0; i < message.length(); i++) {
	    if (Character.isUpperCase(message.charAt(i)))
		count++;
	}
	return count;
    }

    /**
     * Looks for a pattern based on a regular expression.
     * 
     * @param pattern
     *            The pattern we are looking for.
     * @param input
     *            The input.
     * @return
     */
    public static boolean foundPattern(String pattern, String input) {
	int count = 0;
	Pattern p = Pattern.compile(pattern);
	Matcher m = p.matcher(input);
	while (m.find()) {
	    count++;
	}
	if (count > 0) {
	    return true;
	}
	return false;
    }

    /**
     * Returns if the user is an op.
     * 
     * @param name
     *            The user's name.
     * @return
     */
    public static boolean isOp(String channel, String name) {
	if (Main.bl == null || Main.bl.moderators == null)
	    return false;
	return Main.bl.moderators.contains(name);
    }

    /**
     * Gets the users prefix
     * 
     * @param channel
     * @param nickname
     * @return
     */
    public static String getPrefix(String channel, String nickname) {
	User userList[] = Main.bl.getUsers(channel);

	for (User user : userList) {
	    if (nickname.equals(user.getNick())) {
		return user.getPrefix();
	    }
	}

	return "";
    }

    /**
     * Returns if the user is voiced.
     * 
     * @param name
     *            The user's name.
     * @return
     */
    public static boolean isSubbed(String name) {
	for (User u : Main.bl
		.getUsers("#" + Config.getConfig("TWITCH_CHANNEL"))) {
	    if (u.getNick().equalsIgnoreCase(name)) {
		if (u.hasVoice()) {
		    return true;
		}
	    }
	}
	return false;
    }
}
