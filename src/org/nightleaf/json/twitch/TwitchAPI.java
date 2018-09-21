package org.nightleaf.json.twitch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.nightleaf.Main;

public class TwitchAPI {

    public static boolean isLive = false;
    public static boolean lastLive = false;

    /**
     * Updates the variables according to the twitch json api.
     */
    public static void update() {
	// this is called every minute.

	boolean newStatus = isLive();

	// If the channel has gone live, update.
	if (lastLive != newStatus) {
	    System.out.println("Last live status: " + lastLive
		    + ", isLive() = " + isLive());
	    Main.bl.sendQueuedMessage("#nightleaf",
		    "There's been an awakening.  Have you felt it?");
	    isLive = newStatus;
	}

	lastLive = isLive;

    }

    /**
     * Checks if the stream is live, if so triggers the bot to respond.
     * 
     * @return
     */
    public static boolean isLive() {
	try {
	    URL statusCheck = new URL(
		    "http://emeraldlegion.com/twitch/botleaf/test.php");
	    BufferedReader read = new BufferedReader(new InputStreamReader(
		    statusCheck.openStream()));
	    String status = read.readLine();
	    if (status.contains("online")) {
		return true;
	    }
	    read.close();
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}

	return false;
    }
}
