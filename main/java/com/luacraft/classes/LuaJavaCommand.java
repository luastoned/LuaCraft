package com.luacraft.classes;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import com.luacraft.LuaUserdataManager;
import com.naef.jnlua.LuaState;

public class LuaJavaCommand extends CommandBase {
	private LuaState l;
	private String commandName;
	private String commandUsage;

	private void pushCommandFunc(String command) {
		l.newMetatable("CommandCallbacks");
		l.getField(-1, command);
		l.remove(-2);
	}

	public LuaJavaCommand(LuaState state, String command, String usage) {
		l = state;
		commandName = command;
		commandUsage = usage;
	}

	@Override
	public String getName() {
		return commandName;
	}

	public String getCommandUsage(ICommandSender iCommandSender) {
		return commandUsage;
	}

	public boolean canCommandSenderUseCommand(ICommandSender iCommandSender) {
		return true; // The command callback should be checking whether or not the command can be ran
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		synchronized (l) {
			String rawString = "";

			pushCommandFunc(commandName);

			if (sender instanceof EntityPlayer)
				LuaUserdataManager.PushUserdata(l, (EntityPlayer) sender);
			else
				l.pushNil();

			l.pushString(commandName);
			l.newTable();
			for (int i = 0; i < args.length; i++) {
				String s = args[i];
				l.pushInteger(i + 1);
				l.pushString(s);
				l.setTable(-3);
				rawString += s + " ";
			}
			l.pushString(rawString);
			l.call(4, 0);
		}
	}
}