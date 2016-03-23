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
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.server.management.UserListBansEntry;

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
				l.pushBoolean(self.worldObj.getScoreboard().func_151392_a(self.getDisplayName(), l.checkString(2)));
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

			String ip = self.playerNetServerHandler.netManager.getSocketAddress().toString();
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
			self.playerNetServerHandler.setPlayerLocation(pos.x, pos.z, pos.y, self.rotationYaw, self.rotationPitch);
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
			self.playerNetServerHandler.kickPlayerFromServer("You were removed");
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
			ServerConfigurationManager config = server.getConfigurationManager();
			l.pushBoolean(config.func_152596_g(self.getGameProfile()));
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

			ServerConfigurationManager config = server.getConfigurationManager();
			GameProfile user = self.getGameProfile();

			if (state)
				config.func_152605_a(user);
			else
				config.func_152610_b(user);

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
			self.playerNetServerHandler.kickPlayerFromServer(l.checkString(2, "You have been kicked."));
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

			ServerConfigurationManager config = server.getConfigurationManager();

			UserListBansEntry banEntry = new UserListBansEntry(self.getGameProfile(), date, banner,
					cal != null ? cal.getTime() : null, reason);

			self.playerNetServerHandler.kickPlayerFromServer(reason);
			config.func_152608_h().func_152687_a(banEntry);
			return 0;
		}
	};

	public static void Init(final LuaCraftState l) {
		server = l.getServer();

		l.newMetatable("Player");
		{
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
