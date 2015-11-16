package com.luacraft.asm;

import java.io.IOException;

import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;

/**
 * @author dmillerw
 */
public class LuaCraftAccessTransformer extends AccessTransformer {
	public LuaCraftAccessTransformer() throws IOException {
		super("luacraft_at.cfg");
	}
}