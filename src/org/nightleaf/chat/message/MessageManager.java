package org.nightleaf.chat.message;

import java.util.Random;

import org.nightleaf.chat.ChatManager;
import org.nightleaf.json.twitch.TwitchAPI;
import org.nightleaf.twitter.TwitterManager;

public class MessageManager implements Runnable {

    public Random random = new Random();
    public int tick = 0;
    public int uTick = 0;
    public int pTick = 0;

    public static String lastMessage = "";

    @Override
    public void run() {
	while (true) {
	    try {
		tick++;
		uTick++;
		pTick++;

		// Messages to advertise are sent out once per hour.
		if (tick == getTickRate()) { // 30 Minutes

		    if (ChatManager.permittedUsers.size() > 0)
			ChatManager.permittedUsers.clear();

		    tick = 0;
		}

		if (uTick == 60) {
		    TwitterManager.update();
		    TwitchAPI.update();
		    uTick = 0;
		}

		Thread.sleep(1000l);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }

    /**
     * Gets the tick rate depending on stream status.
     * 
     * @return
     */
    public static int getTickRate() {
	if (TwitchAPI.isLive) {
	    return 600;
	}
	return 3600;
    }
}
