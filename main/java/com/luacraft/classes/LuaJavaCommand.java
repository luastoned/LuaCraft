package com.luacraft.classes;

import java.util.ArrayList;
import java.util.List;

import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdata;
import com.naef.jnlua.LuaRuntimeException;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class LuaJavaCommand extends CommandBase {
	private LuaCraftState l;
	private String commandName;
	private String commandUsage;

	private void pushCommandFunc(String command) {
		l.newMetatable("CommandCallbacks");
		l.getField(-1, command);
		l.remove(-2);
	}

	private void pushAutoCompleteFunc(String command) {
		l.newMetatable("CommandAutoComplete");
		l.getField(-1, command);
		l.remove(-2);
	}

	public LuaJavaCommand(LuaCraftState state, String command, String usage) {
		l = state;
		commandName = command;
		commandUsage = usage;
	}

	@Override
	public String getName() {
		return commandName;
	}

	public String getUsage(ICommandSender iCommandSender) {
		return commandUsage;
	}

	public boolean canCommandSenderUseCommand(ICommandSender iCommandSender) {
		return true; // The command callback should be checking whether or not the command can be ran
	}

	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		synchronized (l) {
			try {
				pushAutoCompleteFunc(commandName);

				if (l.isNil(-1) || !l.isFunction(-1))
					return null;

				if (sender instanceof EntityPlayer)
					LuaUserdata.PushUserdata(l, (EntityPlayer) sender);
				else
					l.pushNil();

				l.pushString(commandName);
				l.newTable();
				for (int i = 0; i < args.length; i++) {
					String s = args[i];
					l.pushInteger(i + 1);
					l.pushString(s);
					l.setTable(-3);
				}
				l.call(3, 1);

				List<String> options = new ArrayList<String>();

				if (!l.isNil(-1)) {
					if (l.isString(-1))
						options.add(l.toString(-1));
					else if (l.isTable(-1)) {
						l.pushNil();
						while (l.next(-2)) {
							if (l.isString(-1))
								options.add(l.toString(-1));
							l.pop(1);
						}
					}
				}

				return options;
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}

			return null;
		}
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		synchronized (l) {
			StringBuilder rawString = new StringBuilder();

			try {
				pushCommandFunc(commandName);

				if (l.isNil(-1) || !l.isFunction(-1))
					return;

				if (sender instanceof EntityPlayer)
					LuaUserdata.PushUserdata(l, (EntityPlayer) sender);
				else
					l.pushNil();

				l.pushString(commandName);
				l.newTable();
				for (int i = 0; i < args.length; i++) {
					String s = args[i];
					l.pushInteger(i + 1);
					l.pushString(s);
					l.setTable(-3);
					rawString.append(s);
					rawString.append(' ');
				}
				l.pushString(rawString.toString());
				l.call(4, 0);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			} finally {
				l.setTop(0);
			}
		}
	}
}