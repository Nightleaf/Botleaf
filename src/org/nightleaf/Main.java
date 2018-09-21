package org.nightleaf;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;

import org.nightleaf.chat.RateLimiter;
import org.nightleaf.chat.discord.MessageListener;
import org.nightleaf.chat.message.MessageManager;
import org.nightleaf.chat.message.UniqueMessages;
import org.nightleaf.data.Config;
import org.nightleaf.data.ConfigSerializer;
import org.nightleaf.json.twitch.TwitchAPI;
import org.nightleaf.script.JythonScriptManager;
import org.nightleaf.sql.SQLManager;
import org.nightleaf.twitter.TwitterManager;
import org.nightleaf.ui.NGUI;
import org.nightleaf.ui.UIUtility;

public class Main {

    public static Botleaf bl;
    public static NGUI ngui;
    public static RateLimiter limiter;
    public static MessageManager messageManager;
    public static JythonScriptManager scripter;
    public static JDA jda;

    public static void main(String[] args) throws Exception {
	try {

	    SQLManager.init();

	    ngui = new NGUI();
	    ngui.setVisible(true);

	    TwitterManager.init();

	    // Setup the config
	    Config.init();
	    ConfigSerializer.load();

	    // load commands.
	    UniqueMessages.loadMessages();

	    // Setup scripts
	    scripter = new JythonScriptManager();
	    scripter.load();

	    // Setup the bot
	    bl = new Botleaf();

	    // Enable debugging output.
	    bl.setVerbose(false);

	    limiter = new RateLimiter();
	    Thread thread = new Thread(limiter);
	    thread.start();

	    messageManager = new MessageManager();
	    Thread mThread = new Thread(messageManager);
	    mThread.start();

	    Main.ngui.setTitle("Botleaf - #"
		    + Config.getConfig("TWITCH_CHANNEL"));

	    // Join the IRC server.
	    bl.connect("irc.twitch.tv", 6667, Config.getConfig("TWITCH_OAUTH"));
	    bl.sendRawLine("CAP REQ :twitch.tv/membership");
	    bl.sendRawLine("CAP REQ :twitch.tv/commands");

	    UIUtility.addChatMessage(Config.getConfig("TWITCH_CHANNEL"),
		    "SYSTEM", "Connecting to Twitch..");
	    // Join the channel
	    bl.joinChannel("#" + Config.getConfig("TWITCH_CHANNEL"));

	    UIUtility.addChatMessage(Config.getConfig("TWITCH_CHANNEL"),
		    "SYSTEM",
		    "Joining channel: " + Config.getConfig("TWITCH_CHANNEL")
			    + ".");

	    TwitchAPI.update();
	    jda = new JDABuilder().setEmail("emeraldlegiongaming@gmail.com")
		    .setPassword("teml#").buildBlocking();
	    jda.addEventListener(new MessageListener());

	    TrayIcon trayIcon = null;
	    if (SystemTray.isSupported()) {
		// get the SystemTray instance
		SystemTray tray = SystemTray.getSystemTray();
		// load an image
		Image image = Toolkit.getDefaultToolkit().getImage(
			"icons/mod.png");
		// create a action listener to listen for default action
		// executed on the tray icon
		ActionListener listener = new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equalsIgnoreCase("GUI")) {
			    if (!ngui.isVisible()) {
				ngui.setVisible(true);
			    }
			} else if (e.getActionCommand()
				.equalsIgnoreCase("Exit")) {
			    System.exit(0);
			}
		    }
		};
		// create a popup menu
		PopupMenu popup = new PopupMenu();
		// create menu item for the default action
		MenuItem guiItem = new MenuItem("GUI");
		MenuItem exitItem = new MenuItem("Exit");
		guiItem.addActionListener(listener);
		exitItem.addActionListener(listener);
		popup.add(guiItem);
		popup.add(exitItem);
		// / ... add other items
		// construct a TrayIcon
		trayIcon = new TrayIcon(image, "Botleaf", popup);
		// set the TrayIcon properties
		trayIcon.addActionListener(listener);
		// ...
		// add the tray image
		try {
		    tray.add(trayIcon);
		} catch (AWTException e) {
		    System.err.println(e);
		}
		// ...
	    } else {
		// disable tray option in your application or
		// perform other actions

	    }

	    // Shutdown operations
	    Runtime.getRuntime().addShutdownHook(new Thread() {
		public void run() {
		    ConfigSerializer.save();
		}
	    });
	} catch (Exception e) {
	    UIUtility.addChatMessage(Config.getConfig("TWITCH_CHANNEL"),
		    "SYSTEM", "ERROR: " + e.getMessage());
	}
    }
}
