package com.luacraft.meta.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

import com.luacraft.LuaCraftState;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

public class LuaByteBuf {

	public static Minecraft client = null;

	/**
	 * @author Jake
	 * @function SendToServer Sends the buffer to to the server
	 * @arguments nil
	 * @return nil
	 */

	public static JavaFunction SendToServer = new JavaFunction() {
		public int invoke(LuaState l) {
			if (client.thePlayer == null)
				return 0;

			PacketBuffer self = (PacketBuffer) l.checkUserdata(1,
					PacketBuffer.class, "ByteBuf");
			C17PacketCustomPayload packet = new C17PacketCustomPayload(
					"LuaCraft", self);
			client.thePlayer.sendQueue.addToSendQueue(packet);
			return 0;
		}
	};

	public static void Init(final LuaCraftState l) {
		client = l.getMinecraft();

		l.newMetatable("ByteBuf");
		{
			l.pushJavaFunction(SendToServer);
			l.setField(-2, "SendToServer");
		}
		l.pop(1);
	}
}
