package com.luacraft;

import com.naef.jnlua.LuaRuntimeException;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;

public class LuaPacketManager {

	private LuaCraftState l;

	public LuaPacketManager(LuaCraftState state) {
		l = state;
	}

	@SubscribeEvent
	public void onClientPacket(ClientCustomPacketEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				PacketBuffer buffer = new PacketBuffer(event.getPacket().payload());

				// Peek into the packet
				String func = buffer.readStringFromBuffer(32767);

				// If it's a LuaFile handle it internally
				if (func.equals("LuaFile")) {
					String file = buffer.readStringFromBuffer(32767);
					byte[] data = buffer.readByteArray();
					l.downloadLuaFile(file, data);
					return;
				}

				buffer.readerIndex(0);
				l.pushIncomingNet();
				l.pushUserdataWithMeta(buffer, "ByteBuf");
				l.call(1, 0);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			}
		}
	}

	@SubscribeEvent
	public void onServerPacket(ServerCustomPacketEvent event) {
		synchronized (l) {
			if (!l.isOpen())
				return;

			try {
				PacketBuffer buffer = new PacketBuffer(event.getPacket().payload());
				EntityPlayerMP player = ((NetHandlerPlayServer) event.getHandler()).playerEntity;
				l.pushIncomingNet();
				l.pushUserdataWithMeta(buffer, "ByteBuf");
				l.pushUserdataWithMeta(player, "Player");
				l.call(2, 0);
			} catch (LuaRuntimeException e) {
				l.handleLuaError(e);
			}
		}
	}
}
