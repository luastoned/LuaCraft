package com.luacraft;

import com.naef.jnlua.LuaRuntimeException;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientCustomPacketEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;

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
				PacketBuffer buffer = new PacketBuffer(event.packet.payload());
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
				PacketBuffer buffer = new PacketBuffer(event.packet.payload());
				EntityPlayerMP player = ((NetHandlerPlayServer) event.handler).playerEntity;
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
