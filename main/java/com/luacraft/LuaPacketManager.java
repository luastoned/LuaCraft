package com.luacraft;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;

import com.naef.jnlua.LuaException;

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
				PacketBuffer buffer = new PacketBuffer(event.packet.payload());
				l.pushIncomingNet();
				l.pushUserdataWithMeta(buffer, "ByteBuf");
				l.call(1, 0);
			} catch (LuaException e) {
				l.handleException(e);
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
			} catch (LuaException e) {
				l.handleException(e);
			}
		}
	}
}
