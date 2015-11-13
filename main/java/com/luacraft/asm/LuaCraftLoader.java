package com.luacraft.asm;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

/**
 * @author dmillerw
 */
@IFMLLoadingPlugin.SortingIndex(1001) // get after deobfuscation
public class LuaCraftLoader implements IFMLLoadingPlugin
{
	@Override
	public String[] getASMTransformerClass() {
		return new String[] {LuaCraftAccessTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return "com.luacraft.asm.CoreContainer";
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {

	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
