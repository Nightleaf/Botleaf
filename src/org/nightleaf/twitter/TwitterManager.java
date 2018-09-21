package org.nightleaf.twitter;

import java.util.List;

import org.nightleaf.Main;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterManager {

    public static String lastTweet = "";

    public static void init() {
	lastTweet = getLatestTweet();
    }

    public static void update() {
	String latestTweet = getLatestTweet();
	if (!lastTweet.equals(latestTweet)) {
	    System.out.println("New tweet detected! (" + latestTweet + ")");
	    Main.bl.sendQueuedMessage("#nightleaf",
		    "[NEW TWEET] @Nightleaf475: " + latestTweet);
	    lastTweet = latestTweet;
	}
    }

    public static String getLatestTweet() {

	ConfigurationBuilder cb = new ConfigurationBuilder();

	cb.setDebugEnabled(true)
		.setOAuthConsumerKey("PPMLOdwtehRmf33cNWA195EzJ")
		.setOAuthConsumerSecret(
			"2ikBHl7aeYSe3o6KTXA79nHwq1hEEZYSMetL0qVq0ESY3vlmta")
		.setOAuthAccessToken(
			"68147746-zjjJlAS9egQ50zvONzpNKk4eQPhWFdvG3ouZfGCzJ")
		.setOAuthAccessTokenSecret(
			"cU1qCMUD89dDwNtmIfFEB7V56T83Nx4GgPL0W8mCGjdgs");
	TwitterFactory tf = new TwitterFactory(cb.build());

	Twitter twitter = tf.getInstance();

	List<Status> statusList = null;

	try {
	    statusList = twitter.getUserTimeline("@Nightleaf475");
	} catch (TwitterException e) {
	    e.printStackTrace();
	}
	return statusList.get(0).getText();
    }

}
