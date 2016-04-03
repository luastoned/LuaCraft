package com.luacraft.meta.server;

import com.luacraft.LuaCraftState;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.server.MinecraftServer;

public class LuaByteBuf {

	public static MinecraftServer server = null;

	/**
	 * @author Jake
	 * @function Send
	 * @info Sends the buffer to a player or a table of palyers
	 * @arguments [[Player]]:player or [[Table]]:players, ...
	 * @return nil
	 */

	public static JavaFunction Send = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			SPacketCustomPayload packet = new SPacketCustomPayload("LuaCraft", self);

			for (int i = 2; i <= l.getTop(); i++) {
				if (l.isUserdata(i, EntityPlayerMP.class)) {
					EntityPlayerMP player = (EntityPlayerMP) l.checkUserdata(i, EntityPlayerMP.class, "Player");
					player.playerNetServerHandler.sendPacket(packet);
				} else if (l.isTable(i)) {
					l.pushNil();
					while (l.next(i)) {
						if (l.isUserdata(-1, EntityPlayerMP.class)) {
							EntityPlayerMP player = (EntityPlayerMP) l.checkUserdata(-1, EntityPlayerMP.class,
									"Player");
							player.playerNetServerHandler.sendPacket(packet);
						}
						l.pop(1);
					}
				}
			}
			return 0;
		}
	};

	/**
	 * @author Jake
	 * @function Broadcast
	 * @info Sends the buffer to all players or all players within a specific dimension
	 * @arguments [ [[Number]]:Dimension ]
	 * @return nil
	 */

	public static JavaFunction Broadcast = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			SPacketCustomPayload packet = new SPacketCustomPayload("LuaCraft", self);

			if (l.isNumber(2))
				server.getPlayerList().sendPacketToAllPlayersInDimension(packet, l.toInteger(2));
			else
				server.getPlayerList().sendPacketToAllPlayers(packet);

			return 0;
		}
	};

	public static void Init(final LuaCraftState l) {
		server = l.getServer();

		l.newMetatable("ByteBuf");
		{
			l.pushJavaFunction(Send);
			l.setField(-2, "Send");
			l.pushJavaFunction(Broadcast);
			l.setField(-2, "Broadcast");
		}
		l.pop(1);
	}
}
