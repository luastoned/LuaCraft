package com.luacraft.classes;

import java.util.ArrayList;
import java.util.List;

import com.luacraft.LuaCraft;
import com.luacraft.LuaCraftState;
import com.naef.jnlua.LuaException;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class LuaJavaRunCommand extends CommandBase {
	private Side runState = Side.SERVER;
	private static ChatComponentTranslation clientName = new ChatComponentTranslation("luacraft.state.client");
	private static ChatComponentTranslation serverName = new ChatComponentTranslation("luacraft.state.server");

	private ChatComponentTranslation getSideChatName() {
		if (runState == Side.CLIENT)
			return clientName;
		else
			return serverName;
	}

	private EnumChatFormatting getSideChatColor() {
		if (runState == Side.CLIENT)
			return EnumChatFormatting.GOLD;
		else
			return EnumChatFormatting.DARK_AQUA;
	}

	private boolean isStateOpen() {
		LuaCraftState state = LuaCraft.getLuaState(runState);
		return state != null;
	}

	@Override
	public String getCommandName() {
		return "lua";
	}

	public String getCommandUsage(ICommandSender iCommandSender) {
		return "commands.lua.usage";
	}

	public boolean canCommandSenderUseCommand(ICommandSender iCommandSender) {
		return true;
	}

	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
		List<String> options = new ArrayList<String>();

		if (args.length < 2) {
			if (("switch").startsWith(args[0]))
				options.add("switch");
			if (("reload").startsWith(args[0]))
				options.add("reload");
		} else if (args.length == 2 && (args[0].equalsIgnoreCase("switch") || args[0].equalsIgnoreCase("reload"))) {
			options.add("client");
			options.add("server");
		}

		return options;
	}

	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if (args.length <= 0) {
			ChatComponentTranslation usage = new ChatComponentTranslation(getCommandUsage(sender));
			usage.getChatStyle().setColor(EnumChatFormatting.RED);
			sender.addChatMessage(usage);
			return;
		}

		if (args[0].equalsIgnoreCase("switch")) {
			if (args.length < 2)
				runState = runState == Side.CLIENT ? Side.SERVER : Side.CLIENT;
			else if (args[1].equalsIgnoreCase("client"))
				runState = Side.CLIENT;
			else if (args[1].equalsIgnoreCase("server"))
				runState = Side.SERVER;

			ChatComponentTranslation chatCT = new ChatComponentTranslation("luacraft.state.changed", getSideChatName());
			chatCT.getChatStyle().setColor(getSideChatColor());
			sender.addChatMessage(chatCT);
			return;
		} else if (args[0].equalsIgnoreCase("reload")) {
			if (args.length < 2) {
				LuaCraft.reloadServerState();
				LuaCraft.reloadClientState();
			} else if (args[1].equalsIgnoreCase("client"))
				LuaCraft.reloadClientState();
			else if (args[1].equalsIgnoreCase("server"))
				LuaCraft.reloadServerState();

			return;
		}

		LuaCraftState l = LuaCraft.getLuaState(runState);

		synchronized (l) {
			if (l == null) {
				ChatComponentTranslation noLua = new ChatComponentTranslation("luacraft.state.notinit");
				noLua.getChatStyle().setColor(EnumChatFormatting.RED);
				sender.addChatMessage(noLua);
				return;
			}

			String strLua = "";
			for (int i = 0; i < args.length; i++)
				strLua += args[i] + " ";

			LuaCraft.getLogger().info(sender.getCommandSenderName() + " Lua > " + strLua);

			try {
				l.load(strLua, StatCollector.translateToLocal("luacraft.console"));
				l.call(0, 0);
			} catch (LuaException e) {
				ChatComponentText chatCT = new ChatComponentText(e.getMessage());
				chatCT.getChatStyle().setColor(EnumChatFormatting.RED);
				sender.addChatMessage(chatCT);
			}
		}
	}
}