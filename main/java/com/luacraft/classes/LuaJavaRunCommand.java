package com.luacraft.classes;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.luacraft.LuaCraft;
import com.luacraft.LuaCraftState;
import com.naef.jnlua.LuaException;

import net.minecraftforge.fml.relauncher.Side;

public class LuaJavaRunCommand extends CommandBase {
	private boolean switchState = false;
	private static ChatComponentTranslation clientName = new ChatComponentTranslation("luacraft.state.client");
	private static ChatComponentTranslation serverName = new ChatComponentTranslation("luacraft.state.server");

	@Override
	public String getName() {
		return "lua";
	}

	public String getCommandUsage(ICommandSender iCommandSender) {
		return "commands.lua.usage";
	}

	// TODO: hook for lua command?
	/*
	 * public boolean canCommandSenderUseCommand(ICommandSender iCommandSender) { return true; }
	 */

	public void execute(ICommandSender sender, String[] args) throws CommandException {
		String strLua = "";
		for (int i = 0; i < args.length; i++)
			strLua += args[i] + " ";

		if (args.length <= 0) {
			ChatComponentTranslation usage = new ChatComponentTranslation(getCommandUsage(sender));
			usage.getChatStyle().setColor(EnumChatFormatting.RED);
			sender.addChatMessage(usage);
			return;
		}

		if (args[0].equals("switch")) {
			switchState = !switchState;

			ChatComponentTranslation chatCT = new ChatComponentTranslation("luacraft.state.changed",
					switchState ? clientName : serverName);
			chatCT.getChatStyle().setColor(EnumChatFormatting.GREEN);
			sender.addChatMessage(chatCT);
			return;
		} else if (args[0].equals("reload")) {
			LuaCraft.reloadLuaStates();
			return;
		}

		LuaCraftState l = LuaCraft.getLuaState(switchState ? Side.CLIENT : Side.SERVER);

		synchronized (l) {
			if (l == null) {
				ChatComponentTranslation noLua = new ChatComponentTranslation("luacraft.state.notinit");
				noLua.getChatStyle().setColor(EnumChatFormatting.RED);
				sender.addChatMessage(noLua);
				return;
			}

			LuaCraft.getLogger().info(sender.getName() + " Lua > " + strLua);

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