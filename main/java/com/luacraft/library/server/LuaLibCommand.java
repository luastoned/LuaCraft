package com.luacraft.library.server;

import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdata;
import com.luacraft.classes.LuaJavaCommand;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.LuaType;

import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class LuaLibCommand {
	private static CommandHandler commandHandler = null;

	/**
	 * @author Jake
	 * @library command
	 * @function Add
	 * @info Adds a chat command
	 * @arguments [[String]]:command, [[Function]]:callback, [ [[String]]:usage ]
	 * @return nil
	 */

	public static JavaFunction Add = new JavaFunction() {
		public int invoke(LuaState l) {
			String commandName = l.checkString(1);
			l.checkType(2, LuaType.FUNCTION);
			String usageStr = l.checkString(3, "/" + commandName);

			l.newMetatable("CommandCallbacks");
			
			l.getField(-1, commandName);
			if (l.isNil(-1)) {
				// Only register on first creation
				LuaJavaCommand command = new LuaJavaCommand((LuaCraftState) l, commandName, usageStr);
				commandHandler.registerCommand(command);
			}
			l.pop(1);
			
			// Update the callback function
			l.pushString(commandName); // Push the command name
			l.pushValue(2); // Push the callback function
			l.setTable(-3); // CommandCallbacks[commandName] = callback
			
			// Return our command object
			l.pushUserdataWithMeta(commandHandler.getCommands().get(commandName), "Command");
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library command
	 * @function Remove
	 * @info Remove a chat command
	 * @arguments [[String]]:command
	 * @return nil
	 */

	public static JavaFunction Remove = new JavaFunction() {
		public int invoke(LuaState l) {
			String commandName = l.checkString(1);
			commandHandler.getCommands().remove(commandName);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library command
	 * @function AutoComplete
	 * @info Adds a callback for command auto completion
	 * @arguments [[String]]:command, [[Function]]:callback
	 * @return nil
	 */

	public static JavaFunction AutoComplete = new JavaFunction() {
		public int invoke(LuaState l) {
			String commandName = l.checkString(1);
			l.checkType(2, LuaType.FUNCTION);

			l.newMetatable("CommandAutoComplete");
			l.pushString(commandName);
			l.pushValue(2);
			l.setTable(-3);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library command
	 * @function GetAll
	 * @info Gets a table of all registered commmands
	 * @arguments nil
	 * @return [[Table]]:commands
	 */

	public static JavaFunction GetAll = new JavaFunction() {
		public int invoke(LuaState l) {
			l.newMetatable("CommandCallbacks");
			return 1;
		}
	};
	
	public static JavaFunction __tostring = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaCommand self = (LuaJavaCommand) l.checkUserdata(1, LuaJavaCommand.class, "Command");
			l.pushString(String.format("Command: 0x%08x", l.toPointer(1)));
			return 1;
		}
	};
	
	public static JavaFunction AddAlias = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaCommand self = (LuaJavaCommand) l.checkUserdata(1, LuaJavaCommand.class, "Command");
			self.addAlias(l.checkString(2));
			l.setTop(1); // Return ourself to chain aliases and stuff
			return 1;
		}
	};
	
	public static JavaFunction SetUsage = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaCommand self = (LuaJavaCommand) l.checkUserdata(1, LuaJavaCommand.class, "Command");
			self.setUsage(l.checkString(2));
			l.setTop(1); // Return ourself to chain aliases and stuff
			return 1;
		}
	};
	
	public static JavaFunction CanPlayerUseCommand = new JavaFunction() {
		public int invoke(LuaState l) {
			LuaJavaCommand self = (LuaJavaCommand) l.checkUserdata(1, LuaJavaCommand.class, "Command");
			ICommandSender player = (ICommandSender) l.checkUserdata(2, EntityPlayer.class, "Player");
			l.pushBoolean(player.canUseCommand(self.getRequiredPermissionLevel(), self.getName()));
			return 1;
		}
	};

	public static void Init(final LuaCraftState l) {
		MinecraftServer server = l.getServer();
		commandHandler = (CommandHandler) server.getCommandManager();
		
		l.newMetatable("Command");
		{
			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");

			LuaUserdata.SetupBasicMeta(l);
			LuaUserdata.SetupMeta(l, false);

			l.newMetatable("Object");
			l.setField(-2, "__basemeta");

			l.pushJavaFunction(AddAlias);
			l.setField(-2, "AddAlias");
			l.pushJavaFunction(SetUsage);
			l.setField(-2, "SetUsage");
		}

		l.newTable();
		{
			l.pushJavaFunction(Add);
			l.setField(-2, "Add");
			l.pushJavaFunction(AutoComplete);
			l.setField(-2, "AutoComplete");
			l.pushJavaFunction(GetAll);
			l.setField(-2, "GetAll");
		}
		l.setGlobal("command");
	}
}
