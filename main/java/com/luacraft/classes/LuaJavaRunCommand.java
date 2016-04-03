package com.luacraft.classes;

import java.util.ArrayList;
import java.util.List;

import com.luacraft.LuaCraft;
import com.luacraft.LuaCraftState;
import com.naef.jnlua.LuaException;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;

public class LuaJavaRunCommand extends CommandBase {
	private Side runState = Side.SERVER;
	private static TextComponentTranslation clientName = new TextComponentTranslation("luacraft.state.client");
	private static TextComponentTranslation serverName = new TextComponentTranslation("luacraft.state.server");

	private TextComponentTranslation getSideChatName() {
		if (runState == Side.CLIENT)
			return clientName;
		else
			return serverName;
	}

	private TextFormatting getSideChatColor() {
		if (runState == Side.CLIENT)
			return TextFormatting.GOLD;
		else
			return TextFormatting.DARK_AQUA;
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

	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
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

	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length <= 0) {
			TextComponentTranslation usage = new TextComponentTranslation(getCommandUsage(sender));
			usage.getChatStyle().setColor(TextFormatting.RED);
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

			TextComponentTranslation chatCT = new TextComponentTranslation("luacraft.state.changed", getSideChatName());
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
				TextComponentTranslation noLua = new TextComponentTranslation("luacraft.state.notinit");
				noLua.getChatStyle().setColor(TextFormatting.RED);
				sender.addChatMessage(noLua);
				return;
			}

			String strLua = "";
			for (int i = 0; i < args.length; i++)
				strLua += args[i] + " ";

			LuaCraft.getLogger().info(sender.getName() + " Lua > " + strLua);

			try {

				l.load(strLua, I18n.translateToLocal("luacraft.console"));
				l.call(0, 0);
			} catch (LuaException e) {
				TextComponentString chatCT = new TextComponentString(e.getMessage());
				chatCT.getChatStyle().setColor(TextFormatting.RED);
				sender.addChatMessage(chatCT);
			}
		}
	}
}