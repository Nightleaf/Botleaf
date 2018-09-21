from org.nightleaf import Botleaf
from org.nightleaf.twitter import TwitterManager
from org.nightleaf.chat import ChatManager
from org.jibble.pircbot import PircBot
from org.nightleaf import Main

def command_helloworld(channel, sender, isOp, command, message, params):
	Main.bl.sendQueuedMessage(channel, "Hello world!  Scripting works!");

def command_slap(channel, sender, command, isOp, message, params):
	if (isOp):
		if (len(params) > 0):
			Main.bl.sendQueuedMessage(channel, "/me slaps the shit out of " + params[1] + ".");

def command_testp(channel, sender, command, isOp, message, params):
	if (len(params) > 0):
		Main.bl.sendQueuedMessage(channel, "/timeout " + params[1] + " 1");
		Main.bl.sendQueuedMessage(channel, "Trying to purge " + params[1]);

def command_test(channel, sender, command, isOp, message, params):
		Main.bl.sendQueuedMessage(channel, "Queued messaging appears to be working!");

def command_unban(channel, sender, command, isOp, message, params):
	if (isOp):
		if (len(params) > 0):
			Main.bl.sendQueuedMessage(channel, "/unban " + params[1]);
			Main.bl.sendQueuedMessage(channel, "@" + sender + " - " + params[1] + " should be unbanned.");

def command_lasttweet(channel, sender, command, isOp, message, params):
	Main.bl.sendQueuedMessage(channel, "[Latest Tweet] @Nightleaf475: " + TwitterManager.getLatestTweet());		

def command_permit(channel, sender, command, isOp, message, params):
	if (isOp):
		if (len(params) > 0):
			ChatManager.addPermit(params[1]);
			Main.bl.sendQueuedMessage(channel, "@" + params[1] + ", you are now permitted to post 1 link in the chat.");

def command_schedule(channel, sender, command, isOp, message, params):
	Main.bl.sendQueuedMessage(channel, "@" + sender + ", Nightleaf begins streaming at 4:00 PM EST if streams are planned for that day, follow the channel to know when he goes live.");		

def command_albion(channel, sender, command, isOp, message, params):
	if(len(params) > 0):
		Main.bl.sendQueuedMessage(channel, "@" + sender + ", Albion Online is a open world sandbox MMO in development by Sandbox Interactive, Register an Albion Online account using Nightleaf's referral link: https://albiononline.com/?ref=8YBGND8SFA")	

def command_framedrop(channel, sender, command, isOp, message, params):
	Main.bl.sendQueuedMessage(channel, "Apologies for the frame drops, occasionally the internet drops in and out during Nightleaf's stream, it usually clears up after a few minutes!  Thanks for your patience!")

def command_schedule(channel, sender, command, isOp, message, params):	
	Main.bl.sendQueuedMessage(channel, "Nightleaf streams every MWF at 12:00 PM EST! Check @Nightleaf475 for more information!")

def command_rs(channel, sender, command, isOp, message, params):	
	Main.bl.sendQueuedMessage(channel, "Nightleaf is playing RuneScape!  Add him to your friends list: Nightleaf")

def command_donategp(channel, sender, command, isOp, message, params):
	Main.bl.sendQueuedMessage(channel, "You can support the stream by giving Nightleaf ingame currency to help pay for bonds!  Send a PM to Nightleaf ingame.")

def command_legion(channel, sender, command, isOp, message, params):
	Main.bl.sendQueuedMessage(channel, "Join our gaming community!  http://emeraldlegion.com/  Also join our steam group: http://steamcommunity.com/groups/TheEmeraldLegion")