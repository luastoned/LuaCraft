package com.luacraft.meta.client;

import com.luacraft.LuaCraft;
import com.luacraft.LuaCraftState;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class LuaByteBuf {

	private static Minecraft client = LuaCraft.getForgeClient().getClient();

	/**
	 * @author Jake
	 * @function SendToServer
	 * @info Sends the buffer to to the server
	 * @arguments nil
	 * @return nil
	 */

	public static JavaFunction SendToServer = new JavaFunction() {
		public int invoke(LuaState l) {
			if (client.thePlayer == null)
				return 0;

			PacketBuffer self = (PacketBuffer) l.checkUserdata(1, PacketBuffer.class, "ByteBuf");
			C17PacketCustomPayload packet = new C17PacketCustomPayload("LuaCraft", self);
			client.thePlayer.sendQueue.addToSendQueue(packet);
			return 0;
		}
	};

	public static void Init(final LuaCraftState l) {
		l.newMetatable("ByteBuf");
		{
			l.pushJavaFunction(SendToServer);
			l.setField(-2, "SendToServer");
		}
		l.pop(1);
	}
}
