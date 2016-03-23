package com.luacraft.classes.gui;

import com.luacraft.LuaCraftState;

import net.minecraft.client.gui.GuiScreen;

public class GuiLuaPanel extends GuiScreen {
	LuaGuiHandler l = null;

	public GuiLuaPanel(LuaCraftState luaState) {
		l = new LuaGuiHandler(luaState);
	}

	public void initGui() {
		l.pushField("Init");
		l.call(1, 0);
	}

	public void keyTyped(char par1, int par2) {
		l.pushField("OnKeyPress");
		l.pushInteger(par1);
		l.pushInteger(par2);
		l.call(2, 1);

		if (l.isNoneOrNil(1))
			super.keyTyped(par1, par2);

		l.setTop(0);
	}

	public void mouseClicked(int par1, int par2, int par3) {
		l.pushField("OnMouseClicked");
		l.pushInteger(par1);
		l.pushInteger(par2);
		l.pushInteger(par3);
		l.call(3, 1);

		if (l.isNoneOrNil(1))
			super.mouseClicked(par1, par2, par3);

		l.setTop(0);
	}

	public void onGuiClosed() {
		l.pushField("OnClosed");
		l.call(1, 0);
	}

	public void updateScreen() {
		l.pushField("Think");
		l.call(1, 0);
	}

	public void drawScreen(int par1, int par2, float par3) {
		l.pushField("Paint");
		l.pushInteger(par1);
		l.pushInteger(par2);
		l.pushNumber(par3);
		l.call(3, 1);

		if (l.isNoneOrNil(1))
			super.drawScreen(par1, par2, par3);

		l.setTop(0);
	}
}
