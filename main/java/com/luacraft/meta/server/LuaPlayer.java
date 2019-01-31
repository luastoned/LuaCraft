package com.luacraft.meta.server;

import java.util.Calendar;
import java.util.Date;

import com.luacraft.LuaCraftState;
import com.luacraft.classes.Vector;
import com.mojang.authlib.GameProfile;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.server.management.UserListBansEntry;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.GameType;

public class LuaPlayer {
	public static MinecraftServer server = null;

	/**
	 * @author Gregor
	 * @function SetTeam
	 * @info Sets a players team
	 * @arguments [[String]]:team
	 * @return [[Boolean]]:status, [ [[String]]:error ]
	 */

	public static JavaFunction SetTeam = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayerMP self = (EntityPlayerMP) l.checkUserdata(1, EntityPlayerMP.class, "Player");
			try {
				l.pushBoolean(self.world.getScoreboard().addPlayerToTeam(self.getName(), l.checkString(2)));
				return 1;
			} catch (IllegalArgumentException e) {
				l.pushBoolean(false);
				l.pushString(e.getMessage());
				return 2;
			}
		}
	};

	/**
	 * @author Jake
	 * @function SetGamemode
	 * @info Set the players gamemode
	 * @arguments [[Number]]:gamemode
	 * @return nil
	 */

	public static JavaFunction SetGamemode = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayerMP self = (EntityPlayerMP) l.checkUserdata(1, EntityPlayerMP.class, "Player");
			self.setGameType(GameType.getByID(l.checkInteger(2)));
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function GetGamemode
	 * @info Get the players gamemode
	 * @arguments nil
	 * @return [[Number]]:gamemode
	 */

	public static JavaFunction GetGamemode = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayerMP self = (EntityPlayerMP) l.checkUserdata(1, EntityPlayerMP.class, "Player");
			l.pushNumber(self.interactionManager.getGameType().ordinal());
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetPing
	 * @info Return the players ping
	 * @arguments nil
	 * @return [[Number]]:ping
	 */

	public static JavaFunction GetPing = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayerMP self = (EntityPlayerMP) l.checkUserdata(1, EntityPlayerMP.class, "Player");
			l.pushInteger(self.ping);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function GetIP
	 * @info Return the players ping
	 * @arguments nil
	 * @return [[Number]]:ping
	 */

	public static JavaFunction GetIP = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayerMP self = (EntityPlayerMP) l.checkUserdata(1, EntityPlayerMP.class, "Player");

			String ip = self.connection.netManager.getRemoteAddress().toString();
			ip = ip.substring(ip.indexOf("/") + 1);
			ip = ip.substring(0, ip.indexOf(":"));
			l.pushString(ip);
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function SetPos
	 * @info Sets the players position
	 * @arguments [[Vector]]:pos
	 * @return nil
	 */

	public static JavaFunction SetPos = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayerMP self = (EntityPlayerMP) l.checkUserdata(1, EntityPlayerMP.class, "Player");
			Vector pos = (Vector) l.checkUserdata(2, Vector.class, "Vector");
			self.connection.setPlayerLocation(pos.x, pos.z, pos.y, self.rotationYaw, self.rotationPitch);
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function Remove
	 * @info Remove the entity
	 * @arguments nil
	 * @return nil
	 */

	public static JavaFunction Remove = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayerMP self = (EntityPlayerMP) l.checkUserdata(1, EntityPlayerMP.class, "Player");
			self.connection.disconnect(new TextComponentString("You were removed"));
			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @function IsOP
	 * @info Check if a player is op
	 * @arguments nil
	 * @return [[Boolean]]:op
	 */

	public static JavaFunction IsOP = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayerMP self = (EntityPlayerMP) l.checkUserdata(1, EntityPlayerMP.class, "Player");
			l.pushBoolean(server.getPlayerList().getOppedPlayers().getEntry(self.getGameProfile()) != null);
			return 1;
		}
	};

	/**
	 * @author Gregor
	 * @function SetOP
	 * @info Give op to a player
	 * @arguments [[Boolean]]:op
	 * @return nil
	 */

	public static JavaFunction SetOP = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayerMP self = (EntityPlayerMP) l.checkUserdata(1, EntityPlayerMP.class, "Player");
			boolean state = l.checkBoolean(2);

			PlayerList config = server.getPlayerList();
			GameProfile user = self.getGameProfile();

			if (state)
				config.addOp(user);
			else
				config.removeOp(user);

			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @function Kick
	 * @info Kick a player from the server
	 * @arguments [ [[String]]:reason ]
	 * @return nil
	 */

	public static JavaFunction Kick = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayerMP self = (EntityPlayerMP) l.checkUserdata(1, EntityPlayerMP.class, "Player");
			self.connection.disconnect(new TextComponentString(l.checkString(2, "You have been kicked.")));
			return 0;
		}
	};

	/**
	 * @author Gregor
	 * @function Ban
	 * @info Ban a player from the server
	 * @arguments [ [[String]]:reason, [[Number]]:minutes, [[String]]:banner ]
	 * @return nil
	 */

	public static JavaFunction Ban = new JavaFunction() {
		public int invoke(LuaState l) {
			EntityPlayerMP self = (EntityPlayerMP) l.checkUserdata(1, EntityPlayerMP.class, "Player");
			String reason = l.checkString(2, "You have been banned.");

			Date date = new Date();

			Calendar cal = null;

			if (l.isNumber(3)) {
				cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.MINUTE, l.checkInteger(3, 0));
			}

			String banner = l.checkString(4, "LuaCraft");

			UserListBansEntry banEntry = new UserListBansEntry(self.getGameProfile(), date, banner,
					cal != null ? cal.getTime() : null, reason);

			self.connection.disconnect(new TextComponentString(reason));
			server.getPlayerList().getBannedPlayers().addEntry(banEntry);
			return 0;
		}
	};

	public static void Init(final LuaCraftState l) {
		server = l.getServer();

		l.newMetatable("Player");
		{
			l.pushJavaFunction(SetTeam);
			l.setField(-2, "SetTeam");
			l.pushJavaFunction(SetGamemode);
			l.setField(-2, "SetGamemode");
			l.pushJavaFunction(GetGamemode);
			l.setField(-2, "GetGamemode");
			l.pushJavaFunction(GetPing);
			l.setField(-2, "GetPing");
			l.pushJavaFunction(GetIP);
			l.setField(-2, "GetIP");
			l.pushJavaFunction(SetPos);
			l.setField(-2, "SetPos");
			l.pushJavaFunction(Remove);
			l.setField(-2, "Remove");
			l.pushJavaFunction(IsOP);
			l.setField(-2, "IsOP");
			l.pushJavaFunction(SetOP);
			l.setField(-2, "SetOP");
			l.pushJavaFunction(Kick);
			l.setField(-2, "Kick");
			l.pushJavaFunction(Ban);
			l.setField(-2, "Ban");
		}
		l.pop(1);
	}
}
