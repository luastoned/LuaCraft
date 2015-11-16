package com.luacraft.library.server;

import net.minecraft.server.MinecraftServer;

import com.luacraft.LuaCraftState;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

public class LuaLibGame {
	private static MinecraftServer server = null;

	/**
	 * @author Matt
	 * @library game
	 * @function MaxPlayers Get the number of available player slots
	 * @arguments nil
	 * @return [[Number]]:slots
	 */

	public static JavaFunction MaxPlayers = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushNumber(server.getConfigurationManager().getMaxPlayers());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function GetMOTD Get the current MOTD of the server
	 * @arguments nil
	 * @return [[String]]:motd
	 */

	public static JavaFunction GetMOTD = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushString(server.getMotd());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function SetMOTD Set the current MOTD of the server
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
	 * @function GetPVP Get if PVP is enabled on the server
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
	 * @function SetPVP Set if PVP is enabled on the server
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
	 * @function GetOnlineMode Get if the server is in online mode
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
	 * @function SetOnlineMode Set if the server is in online mode
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
	 * @function GetCanSpawnAnimals Get if the server can spawn animals
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
	 * @function SetCanSpawnAnimals Set if the server can spawn animals
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
	 * @function GetCanSpawnNPCs Get if the server can spawn NPCs
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
	 * @function SetCanSpawnNPCs Set if the server can spawn npcs
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
	 * @function GetFlightEnabled Get if the server allows players to fly
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
	 * @function SetFlightEnabled Set if the server allows players to fly
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
	 * @function GetBuildHeight Get the maximum height a player can build
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
	 * @function SetBuildHeight Set the maximum height a player can build
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
	 * @function GetHostName Get the hostname of the server
	 * @arguments nil
	 * @return [[String]]:hostname
	 */

	public static JavaFunction GetHostName = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushString(server.getHostname());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function SetHostName Set the hostname of the server
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
	 * @function ConCommand Run a command as if it were ran through the server
	 *           console
	 * @arguments [[String]]:command
	 * @return [[String]]:output
	 */

	public static JavaFunction ConCommand = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushString(server.handleRConCommand(l.checkString(1)));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @library game
	 * @function GetViewDistance Get the viewdistance of the server
	 * @arguments nil
	 * @return [[Number]]:distance
	 */

	public static JavaFunction GetViewDistance = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushNumber(server.getConfigurationManager().getViewDistance());
			return 1;
		}
	};

	public static void Init(final LuaCraftState l) {
		server = l.getMinecraftServer();

		l.newTable();
		{
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
