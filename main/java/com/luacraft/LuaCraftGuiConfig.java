package com.luacraft;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;

public class LuaCraftGuiConfig extends GuiConfig
{
    public LuaCraftGuiConfig(GuiScreen parent)
    {
        super(parent,
                (new ConfigElement(LuaCraft.config.general)).getChildElements(),
                LuaCraft.MODID,
                false,
                false,
                "LuaCraft configuration");
        titleLine2 = LuaCraft.config.config.getConfigFile().getAbsolutePath();
    }

    @Override
    public void initGui()
    {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        super.actionPerformed(button);
    }
}
