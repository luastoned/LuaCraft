package com.luacraft.library.server;

import java.util.List;

import com.luacraft.LuaCraftState;
import com.luacraft.LuaUserdata;
import com.luacraft.library.LuaLibUtil;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class LuaLibGame {
	private static MinecraftServer server = null;

	/**
	 * @author Jake
	 * @library game
	 * @function GetPlayers
	 * @info Get a table of all the currently connected players on the server
	 * @arguments nil
	 * @return [[Table]]:players
	 */

	public static JavaFunction GetPlayers = new JavaFunction() {
		public int invoke(LuaState l) {
			List<EntityPlayerMP> playerList = server.getPlayerList().getPlayerList();

			l.newTable();
			int i = 1;
			for (EntityPlayerMP player : playerList) {
				l.pushInteger(i++);
				LuaUserdata.PushUserdata(l, player);
				l.setTable(-3);
			}
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function GetPlayerByName
	 * @info Attempts to find a player using the provided string
	 * @arguments [[String]]:search
	 * @return [[Player]]:player
	 */

	public static JavaFunction GetPlayerByName = new JavaFunction() {
		public int invoke(LuaState l) {
			String search = l.checkString(1);
			List<EntityPlayerMP> playerList = server.getPlayerList().getPlayerList();

			for (EntityPlayer player : playerList) {
				String playerName = player.getDisplayNameString().toLowerCase();
				if (playerName.contains(search.toLowerCase())) {
					LuaUserdata.PushUserdata(l, player);
					return 1;
				}
			}
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library client
	 * @function ChatPrint
	 * @info Send a chat message to all the players on the server
	 * @arguments [[String]]:msg, [[Number]]:color, ...
	 * @return nil
	 */

	public static JavaFunction ChatPrint = new JavaFunction() {
		public int invoke(LuaState l) {
			List<EntityPlayerMP> playerList = server.getPlayerList().getPlayerList();

			String chatMsg = LuaLibUtil.toChat(l, 1);
			for (EntityPlayerMP player : playerList)
				player.addChatMessage(new TextComponentString(chatMsg));
			return 0;
		}
	};

	/**
	 * @author Matt
	 * @library game
	 * @function MaxPlayers
	 * @info Get the number of available player slots
	 * @arguments nil
	 * @return [[Number]]:slots
	 */

	public static JavaFunction MaxPlayers = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushNumber(server.getPlayerList().getMaxPlayers());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function GetMOTD
	 * @info Get the current MOTD of the server
	 * @arguments nil
	 * @return [[String]]:motd
	 */

	public static JavaFunction GetMOTD = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushString(server.getMOTD());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function SetMOTD
	 * @info Set the current MOTD of the server
	 * @arguments [[String]]:motd
	 * @return nil
	 */

	public static JavaFunction SetMOTD = new JavaFunction() {
		public int invoke(LuaState l) {
			server.setMOTD(l.checkString(1));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function GetPVP
	 * @info Get if PVP is enabled on the server
	 * @arguments nil
	 * @return [[Boolean]]:pvp
	 */

	public static JavaFunction GetPVP = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(server.isPVPEnabled());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function SetPVP
	 * @info Set if PVP is enabled on the server
	 * @arguments [[Boolean]]:pvp
	 * @return nil
	 */

	public static JavaFunction SetPVP = new JavaFunction() {
		public int invoke(LuaState l) {
			server.setAllowPvp(l.checkBoolean(1));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function GetOnlineMode
	 * @info Get if the server is in online mode
	 * @arguments nil
	 * @return [[Boolean]]:online
	 */

	public static JavaFunction GetOnlineMode = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(server.isServerInOnlineMode());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function SetOnlineMode
	 * @info Set if the server is in online mode
	 * @arguments [[Boolean]]:online
	 * @return nil
	 */

	public static JavaFunction SetOnlineMode = new JavaFunction() {
		public int invoke(LuaState l) {
			server.setOnlineMode(l.checkBoolean(1));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function GetCanSpawnAnimals
	 * @info Get if the server can spawn animals
	 * @arguments nil
	 * @return [[Boolean]]:animals
	 */

	public static JavaFunction GetCanSpawnAnimals = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(server.getCanSpawnAnimals());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function SetCanSpawnAnimals
	 * @info Set if the server can spawn animals
	 * @arguments [[Boolean]]:animals
	 * @return nil
	 */

	public static JavaFunction SetCanSpawnAnimals = new JavaFunction() {
		public int invoke(LuaState l) {
			server.setCanSpawnAnimals(l.checkBoolean(1));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function GetCanSpawnNPCs
	 * @info Get if the server can spawn NPCs
	 * @arguments nil
	 * @return [[Boolean]]:npcs
	 */

	public static JavaFunction GetCanSpawnNPCs = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(server.getCanSpawnNPCs());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function SetCanSpawnNPCs
	 * @info Set if the server can spawn npcs
	 * @arguments [[Boolean]]:npcs
	 * @return nil
	 */

	public static JavaFunction SetCanSpawnNPCs = new JavaFunction() {
		public int invoke(LuaState l) {
			server.setCanSpawnNPCs(l.checkBoolean(1));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function GetFlightEnabled
	 * @info Get if the server allows players to fly
	 * @arguments nil
	 * @return [[Boolean]]:flight
	 */

	public static JavaFunction GetFlightEnabled = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(server.isFlightAllowed());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function SetFlightEnabled
	 * @info Set if the server allows players to fly
	 * @arguments [[Boolean]]:flight
	 * @return nil
	 */

	public static JavaFunction SetFlightEnabled = new JavaFunction() {
		public int invoke(LuaState l) {
			server.setAllowFlight(l.checkBoolean(1));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function GetBuildHeight
	 * @info Get the maximum height a player can build
	 * @arguments nil
	 * @return [[Number]]:height
	 */

	public static JavaFunction GetBuildHeight = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushInteger(server.getBuildLimit());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function SetBuildHeight
	 * @info Set the maximum height a player can build
	 * @arguments [[Number]]:height
	 * @return nil
	 */

	public static JavaFunction SetBuildHeight = new JavaFunction() {
		public int invoke(LuaState l) {
			server.setBuildLimit(l.checkInteger(1));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function GetHostName
	 * @info Get the hostname of the server
	 * @arguments nil
	 * @return [[String]]:hostname
	 */

	public static JavaFunction GetHostName = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushString(server.getServerHostname());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function SetHostName
	 * @info Set the hostname of the server
	 * @arguments [[String]]:hostname
	 * @return nil
	 */

	public static JavaFunction SetHostName = new JavaFunction() {
		public int invoke(LuaState l) {
			server.setHostname(l.checkString(1));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function ConCommand
	 * @info Run a command as if it were ran through the server console
	 * @arguments [[String]]:command
	 * @return [[String]]:output
	 */

	public static JavaFunction ConCommand = new JavaFunction() {
		public int invoke(LuaState l) {
			RConConsoleSource source = new RConConsoleSource(server);
			server.getCommandManager().executeCommand(source, l.checkString(1));
			String result = source.getLogContents();
			l.pushString(result);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function GetViewDistance
	 * @info Get the viewdistance of the server
	 * @arguments nil
	 * @return [[Number]]:distance
	 */

	public static JavaFunction GetViewDistance = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushNumber(server.getPlayerList().getViewDistance());
			return 1;
		}
	};

	public static void Init(final LuaCraftState l) {
		server = l.getServer();

		l.newTable();
		{
			l.pushJavaFunction(GetPlayers);
			l.setField(-2, "GetPlayers");
			l.pushJavaFunction(GetPlayerByName);
			l.setField(-2, "GetPlayerByName");
			l.pushJavaFunction(ChatPrint);
			l.setField(-2, "ChatPrint");
			l.pushJavaFunction(MaxPlayers);
			l.setField(-2, "MaxPlayers");
			l.pushJavaFunction(GetMOTD);
			l.setField(-2, "GetMOTD");
			l.pushJavaFunction(SetMOTD);
			l.setField(-2, "SetMOTD");
			l.pushJavaFunction(GetPVP);
			l.setField(-2, "GetPVP");
			l.pushJavaFunction(SetPVP);
			l.setField(-2, "SetPVP");
			l.pushJavaFunction(GetOnlineMode);
			l.setField(-2, "GetOnlineMode");
			l.pushJavaFunction(SetOnlineMode);
			l.setField(-2, "SetOnlineMode");
			l.pushJavaFunction(GetCanSpawnAnimals);
			l.setField(-2, "GetCanSpawnAnimals");
			l.pushJavaFunction(SetCanSpawnAnimals);
			l.setField(-2, "SetCanSpawnAnimals");
			l.pushJavaFunction(GetCanSpawnNPCs);
			l.setField(-2, "GetCanSpawnNPCs");
			l.pushJavaFunction(SetCanSpawnNPCs);
			l.setField(-2, "SetCanSpawnNPCs");
			l.pushJavaFunction(GetFlightEnabled);
			l.setField(-2, "GetFlightEnabled");
			l.pushJavaFunction(SetFlightEnabled);
			l.setField(-2, "SetFlightEnabled");
			l.pushJavaFunction(GetBuildHeight);
			l.setField(-2, "GetBuildHeight");
			l.pushJavaFunction(SetBuildHeight);
			l.setField(-2, "SetBuildHeight");
			l.pushJavaFunction(SetHostName);
			l.setField(-2, "SetHostName");
			l.pushJavaFunction(GetHostName);
			l.setField(-2, "GetHostName");
			l.pushJavaFunction(ConCommand);
			l.setField(-2, "ConCommand");
			l.pushJavaFunction(GetViewDistance);
			l.setField(-2, "GetViewDistance");
		}
		l.setGlobal("game");
	}
}
