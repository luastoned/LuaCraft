package com.luacraft.classes.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import com.luacraft.LuaCraftState;

public class GuiLuaButton extends GuiButton
{
	LuaGuiHandler l = null;
	
	public GuiLuaButton(LuaCraftState luaState)
	{
		super(0, 0, 0, "Button");
		l = new LuaGuiHandler(luaState);
	}
	
	public void drawButton(Minecraft client, int par1, int par2)
	{
		l.pushField("Paint");
		l.pushInteger(par1);
		l.pushInteger(par2);
		l.call(2,1);

		if (l.isNoneOrNil(1))
			super.drawButton(client, par1, par2);

		l.setTop(0);
	}
	
	public void mouseDragged(Minecraft client, int par1, int par2)
	{
		l.pushField("MouseDragged");
		l.pushInteger(par1);
		l.pushInteger(par2);
		l.call(2,1);

		if (l.isNoneOrNil(1))
			super.mouseDragged(client, par1, par2);

		l.setTop(0);
	}
	
	public boolean mousePressed(Minecraft client, int par1, int par2)
	{
		l.pushField("MousePressed");
		l.pushInteger(par1);
		l.pushInteger(par2);
		l.call(2,0);
		return super.mousePressed(client, par1, par2);
	}
	
	public void mouseReleased(Minecraft client, int par1, int par2)
	{
		l.pushField("MouseReleased");
		l.pushInteger(par1);
		l.pushInteger(par2);
		l.call(2,0);
	}
}
