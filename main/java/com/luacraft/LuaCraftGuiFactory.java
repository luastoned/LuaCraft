package com.luacraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.util.Set;

public class LuaCraftGuiFactory implements IModGuiFactory
{
    @Override
    public void initialize(Minecraft minecraft) {

    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return LuaCraftGuiConfig.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement runtimeOptionCategoryElement) {
        return null;
    }
}
