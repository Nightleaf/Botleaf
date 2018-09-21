package org.nightleaf.data;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

public class Config {

    public final static File CONFIG_FILE = new File("./config/config.json");

    /**
     * This collection holds all of our configuration attributes.
     */
    public static Map<String, String> attributes = new HashMap<String, String>();

    public static String[] messages;

    /**
     * Initializes the attributes (using the defaults).
     */
    public static void init() {
	boolean newConfig = false;
	/**
	 * Creates the json file for the eventual save.
	 */
	if (!CONFIG_FILE.exists()) {
	    try {
		CONFIG_FILE.createNewFile();
		newConfig = true;
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	/**
	 * Default values for the config file.
	 */
	attributes.put("BOT_NAME", "Botleaf");
	attributes.put("TWITCH_OAUTH", "oauth:uxtj64hkchd5jftctp3d4irxth0c4m");
	attributes.put("TWITCH_CHANNEL", "nightleaf");
	attributes
		.put("MESSAGES",
			"Nightleaf's Twitter: http://twitter.com/Nightleaf475, Nightleaf's YT: http://Youtube.com/Nightleaf475, Follow for the latest from Nightleaf!|Join The Emerald Legion on Steam: http://steamcommunity.com/groups/TheEmeraldLegion");
	if (newConfig) {
	    ConfigSerializer.save();
	}
	Config.messages = getConfig("MESSAGES").split("|");
    }

    /**
     * Returns a specific attribute from the config.
     * 
     * @param key
     *            The key of the variable.
     * @return
     */
    public static String getConfig(String key) {
	return attributes.get(key);
    }

    /**
     * Gets the reflection type (for gson). This needs to match the structure of
     * attributes.
     * 
     * @return
     */
    public static Type getType() {
	return new TypeToken<Map<String, String>>() {
	}.getType();
    }

}
