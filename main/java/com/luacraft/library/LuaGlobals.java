package com.luacraft.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.luacraft.LuaCraft;
import com.luacraft.LuaCraftState;
import com.luacraft.classes.Angle;
import com.luacraft.classes.Color;
import com.luacraft.classes.FileMount;
import com.luacraft.classes.Vector;
import com.luacraft.console.ConsoleManager;
import com.luacraft.meta.LuaObject;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaException;
import com.naef.jnlua.LuaRuntimeException;
import com.naef.jnlua.LuaState;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class LuaGlobals {
	/**
	 * @author Jake
	 * @function Angle
	 * @info Creates a new [[Angle]] object
	 * @arguments [ [[Number]]:p, [[Number]]:y, [[Number]]:r ]
	 * @return [[Angle]]:angle
	 */

	public static JavaFunction Angle = new JavaFunction() {
		public int invoke(LuaState l) {
			Angle ang = new Angle(l.checkNumber(1, 0), l.checkNumber(2, 0), l.checkNumber(3, 0));
			ang.push(l);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function ByteBuf
	 * @info Creates a new [[ByteBuf]] object
	 * @arguments [ [[Number]]:limit ]
	 * @return [[ByteBuf]]:buffer
	 */

	public static JavaFunction ByteBuf = new JavaFunction() {
		public int invoke(LuaState l) {
			PacketBuffer buffer = new PacketBuffer(Unpooled.buffer(l.checkInteger(1, 32766)));
			l.pushUserdataWithMeta(buffer, "ByteBuf");
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function Color
	 * @info Creates a new [[Color]] object
	 * @arguments [[Number]]:red, [[Number]]:green, [[Number]]:blue, [ [[Number]]:alpha ]
	 * @return [[Color]]:color
	 */

	public static JavaFunction Color = new JavaFunction() {
		public int invoke(LuaState l) {
			Color self = new Color(l.checkInteger(1), l.checkInteger(2), l.checkInteger(3), l.checkInteger(4, 255));
			l.pushUserdataWithMeta(self, "Color");
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function DamageSource
	 * @info Creates a new [[DamageSource]] object
	 * @arguments [[String]]:type, [ [[Entity]]:source ]
	 * @return [[DamageSource]]:damagesource
	 */

	public static JavaFunction DamageSource = new JavaFunction() {
		public int invoke(LuaState l) {
			String type = l.checkString(1);

			if (l.isUserdata(2, Entity.class))
				l.pushUserdataWithMeta(
						new EntityDamageSource(type, (Entity) l.checkUserdata(2, Entity.class, "Entity")),
						"EntityDamageSource");
			else
				l.pushUserdataWithMeta(new DamageSource(type), "DamageSource");
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function ItemStack
	 * @info Creates a new [[ItemStack]] object
	 * @arguments [ [[Number]]:id, [[Number]]:size, [[Number]]:damage ]
	 * @return [[ItemStack]]:item
	 */

	public static JavaFunction ItemStack = new JavaFunction() {
		public int invoke(LuaState l) {
			int itemID = l.checkInteger(1, 1);
			int stackSize = l.checkInteger(2, 1);
			int stackDamage = l.checkInteger(3, 0);

			ItemStack self = new ItemStack(Item.getItemById(itemID), stackSize, stackDamage);
			l.pushUserdataWithMeta(self, "ItemStack");
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function NBTTag
	 * @info Creates a new [[NBTTag]] object
	 * @arguments nil
	 * @return [[NBTTag]]:tag
	 */

	public static JavaFunction NBTTag = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushUserdataWithMeta(new NBTTagCompound(), "NBTTag");
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function Resource
	 * @info Creates a new [[Resource]] object
	 * @arguments [[String]]:path, [ [[String]]:modid ]
	 * @return [[Resource]]:resource
	 */

	public static JavaFunction Resource = new JavaFunction() {
		public int invoke(LuaState l) {
			if (l.getTop() > 1)
				l.pushUserdataWithMeta(new ResourceLocation(l.checkString(1), l.checkString(2)), "Resource");
			else
				l.pushUserdataWithMeta(new ResourceLocation(l.checkString(1)), "Resource");
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function Vector
	 * @info Creates a new [[Vector]] object
	 * @arguments [ [[Number]]:x, [[Number]]:y, [[Number]]:z ]
	 * @return [[Vector]]:vector
	 */

	public static JavaFunction Vector = new JavaFunction() {
		public int invoke(LuaState l) {
			Vector vec = new Vector(l.checkNumber(1, 0), l.checkNumber(2, 0), l.checkNumber(3, 0));
			vec.push(l);
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsFunction
	 * @info Returns if the argument is a function
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:function
	 */

	public static JavaFunction IsFunction = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isFunction(1));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsNumber
	 * @info Returns if the argument is a number
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:number
	 */

	public static JavaFunction IsNumber = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isNumber(1));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsString
	 * @info Returns if the argument is a string
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:string
	 */

	public static JavaFunction IsString = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isString(1));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsBool
	 * @info Returns if the argument is a bool
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:bool
	 */

	public static JavaFunction IsBool = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isBoolean(1));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function IsTable
	 * @info Returns if the argument is a table
	 * @arguments [[Object]]:object
	 * @return [[Boolean]]:table
	 */

	public static JavaFunction IsTable = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushBoolean(l.isTable(1));
			return 1;
		}
	};

	/**
	 * @author Jake
	 * @function FindMetaTable
	 * @info Returns the meta table for the given type
	 * @arguments [[String]]:meta
	 * @return [[Table]]:table
	 */

	public static JavaFunction FindMetaTable = new JavaFunction() {
		public int invoke(LuaState l) {
			String meta = l.checkString(1);
			l.newMetatable(meta);
			return 1;
		}
	};

	private static JavaFunction loadfileInternal = new JavaFunction() {
		public int invoke(LuaState l) {
			String fileName = l.checkString(1);

			if (!fileName.endsWith(".lua"))
				throw new LuaRuntimeException("File must be a Lua file");

			File file = FileMount.GetFile("lua/" + fileName);

			InputStream in = null;
			try {
				in = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				throw new LuaRuntimeException("Cannot open " + file.getName() + ": No such file or directory");
			}
			try {
				l.load(in, file.getName());
			} catch (IOException e) {
				throw new LuaRuntimeException(e.getMessage());
			}
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return 1;
		}
	};

	public static JavaFunction dofile = new JavaFunction() {
		public int invoke(LuaState l) {
			loadfileInternal.invoke(l);
			l.call(0, 0);
			return 0;
		}
	};

	public static JavaFunction loadfile = new JavaFunction() {
		public int invoke(LuaState l) {
			try {
				loadfileInternal.invoke(l);
				return 1;
			} catch (LuaException e) {
				l.pushNil();
				l.pushString(e.getMessage());
				return 2;
			}
		}
	};

	public static JavaFunction nanotime = new JavaFunction() {
		public int invoke(LuaState l) {
			l.pushNumber(System.nanoTime());
			return 1;
		}
	};

	public static JavaFunction print_lua_stack = new JavaFunction() {
		public int invoke(LuaState l) {
			((LuaCraftState)l).printStack();
			return 0;
		}
	};

	public static void Init(final LuaState l) {
		l.pushJavaFunction(Angle);
		l.setGlobal("Angle");
		l.pushJavaFunction(ByteBuf);
		l.setGlobal("ByteBuf");
		l.pushJavaFunction(Color);
		l.setGlobal("Color");
		l.pushJavaFunction(DamageSource);
		l.setGlobal("DamageSource");
		l.pushJavaFunction(ItemStack);
		l.setGlobal("ItemStack");
		l.pushJavaFunction(NBTTag);
		l.setGlobal("NBTTag");
		l.pushJavaFunction(Resource);
		l.setGlobal("Resource");
		l.pushJavaFunction(Vector);
		l.setGlobal("Vector");
		l.pushJavaFunction(IsFunction);
		l.setGlobal("IsFunction");
		l.pushJavaFunction(IsNumber);
		l.setGlobal("IsNumber");
		l.pushJavaFunction(IsString);
		l.setGlobal("IsString");
		l.pushJavaFunction(IsBool);
		l.setGlobal("IsBool");
		l.pushJavaFunction(IsTable);
		l.setGlobal("IsTable");

		l.pushJavaFunction(LuaObject.IsBlock);
		l.setGlobal("IsBlock");
		l.pushJavaFunction(LuaObject.IsWorld);
		l.setGlobal("IsWorld");
		l.pushJavaFunction(LuaObject.IsThread);
		l.setGlobal("IsThread");
		l.pushJavaFunction(LuaObject.IsAngle);
		l.setGlobal("IsAngle");
		l.pushJavaFunction(LuaObject.IsVector);
		l.setGlobal("IsVector");
		l.pushJavaFunction(LuaObject.IsColor);
		l.setGlobal("IsColor");

		/**
		 * @author Jake
		 * @function IsEntity
		 * @info Check if the argument is an entity
		 * @arguments [[Object]]:object
		 * @return [[Boolean]]:entity
		 */

		l.pushJavaFunction(LuaObject.IsEntity);
		l.setGlobal("IsEntity");

		/**
		 * @author Jake
		 * @function IsPlayer
		 * @info Check if the argument is a player
		 * @arguments [[Object]]:object
		 * @return [[Boolean]]:player
		 */

		l.pushJavaFunction(LuaObject.IsPlayer);
		l.setGlobal("IsPlayer");

		l.pushJavaFunction(LuaObject.IsMob);
		l.setGlobal("IsMob");
		l.pushJavaFunction(LuaObject.IsAnimal);
		l.setGlobal("IsAnimal");
		l.pushJavaFunction(LuaObject.IsTameable);
		l.setGlobal("IsTameable");
		l.pushJavaFunction(LuaObject.IsLiving);
		l.setGlobal("IsLiving");
		l.pushJavaFunction(LuaObject.IsItem);
		l.setGlobal("IsItem");
		l.pushJavaFunction(LuaObject.IsChunk);
		l.setGlobal("IsChunk");
		l.pushJavaFunction(LuaObject.IsExplosion);
		l.setGlobal("IsExplosion");
		l.pushJavaFunction(LuaObject.IsBiomeGenBase);
		l.setGlobal("IsBiomeGenBase");

		l.pushJavaFunction(FindMetaTable);
		l.setGlobal("FindMetaTable");

		l.pushJavaFunction(dofile);
		l.setGlobal("dofile");
		l.pushJavaFunction(dofile);
		l.setGlobal("include");
		l.pushJavaFunction(loadfile);
		l.setGlobal("loadfile");

		l.pushJavaFunction(nanotime);
		l.setGlobal("nanotime");

		l.pushJavaFunction(print_lua_stack);
		l.setGlobal("print_lua_stack");

		/*
		 * @enum WORLD_NETHER
		 * 
		 * @enum WORLD_OVERWORLD
		 * 
		 * @enum WORLD_END
		 * 
		 * @enum RESULT_ALLOW
		 * 
		 * @enum RESULT_DENY
		 * 
		 * @enum RESULT_DEFAULT
		 * 
		 * @enum MODE_NONE
		 * 
		 * @enum MODE_SURVIVAL
		 * 
		 * @enum MODE_CREATIVE
		 * 
		 * @enum MODE_ADVENTURE
		 * 
		 * @enum MODE_SPECTATOR
		 * 
		 * @enum DIFFICULTY_PEACEFUL
		 * 
		 * @enum DIFFICULTY_EASY
		 * 
		 * @enum DIFFICULTY_NORMAL
		 * 
		 * @enum DIFFICULTY_HARD
		 */

		l.pushNumber(-1);
		l.setGlobal("WORLD_NETHER");
		l.pushNumber(0);
		l.setGlobal("WORLD_OVERWORLD");
		l.pushNumber(1);
		l.setGlobal("WORLD_END");

		l.pushNumber(Result.ALLOW.ordinal());
		l.setGlobal("RESULT_ALLOW");
		l.pushNumber(Result.DENY.ordinal());
		l.setGlobal("RESULT_DENY");
		l.pushNumber(Result.DEFAULT.ordinal());
		l.setGlobal("RESULT_DEFAULT");

		l.pushNumber(GameType.NOT_SET.ordinal());
		l.setGlobal("MODE_NONE");
		l.pushNumber(GameType.SURVIVAL.ordinal());
		l.setGlobal("MODE_SURVIVAL");
		l.pushNumber(GameType.CREATIVE.ordinal());
		l.setGlobal("MODE_CREATIVE");
		l.pushNumber(GameType.ADVENTURE.ordinal());
		l.setGlobal("MODE_ADVENTURE");
		l.pushNumber(GameType.SPECTATOR.ordinal());
		l.setGlobal("MODE_SPECTATOR");

		l.pushNumber(EnumDifficulty.PEACEFUL.ordinal());
		l.setGlobal("DIFFICULTY_PEACEFUL");
		l.pushNumber(EnumDifficulty.EASY.ordinal());
		l.setGlobal("DIFFICULTY_EASY");
		l.pushNumber(EnumDifficulty.NORMAL.ordinal());
		l.setGlobal("DIFFICULTY_NORMAL");
		l.pushNumber(EnumDifficulty.HARD.ordinal());
		l.setGlobal("DIFFICULTY_HARD");

		new Vector().push(l);
		l.setGlobal("vector_origin");
		new Angle().push(l);
		l.setGlobal("angle_zero");

		l.newTable();
		{
			new Color(0, 0, 0, 255).push(l);
			l.setField(-2, "Black");
			new Color(255, 255, 255, 255).push(l);
			l.setField(-2, "White");
			new Color(255, 0, 0, 255).push(l);
			l.setField(-2, "Red");
			new Color(0, 255, 0, 255).push(l);
			l.setField(-2, "Green");
			new Color(0, 0, 255, 255).push(l);
			l.setField(-2, "Blue");
			new Color(255, 255, 0, 255).push(l);
			l.setField(-2, "Yellow");
			new Color(0, 255, 255, 255).push(l);
			l.setField(-2, "Cyan");
			new Color(255, 0, 255, 255).push(l);
			l.setField(-2, "Magenta");
		}
		l.setGlobal("color");

		l.newTable();
		{
			l.pushNumber(TextFormatting.BLACK.ordinal());
			l.setField(-2, "Black");
			l.pushNumber(TextFormatting.DARK_BLUE.ordinal());
			l.setField(-2, "DarkBlue");
			l.pushNumber(TextFormatting.DARK_GREEN.ordinal());
			l.setField(-2, "DarkGreen");
			l.pushNumber(TextFormatting.DARK_AQUA.ordinal());
			l.setField(-2, "DarkTeal");
			l.pushNumber(TextFormatting.DARK_RED.ordinal());
			l.setField(-2, "DarkRed");
			l.pushNumber(TextFormatting.DARK_PURPLE.ordinal());
			l.setField(-2, "Purple");
			l.pushNumber(TextFormatting.GOLD.ordinal());
			l.setField(-2, "Gold");
			l.pushNumber(TextFormatting.GRAY.ordinal());
			l.setField(-2, "Gray");
			l.pushNumber(TextFormatting.DARK_GRAY.ordinal());
			l.setField(-2, "DarkGray");
			l.pushNumber(TextFormatting.BLUE.ordinal());
			l.setField(-2, "Blue");
			l.pushNumber(TextFormatting.GREEN.ordinal());
			l.setField(-2, "Green");
			l.pushNumber(TextFormatting.AQUA.ordinal());
			l.setField(-2, "Teal");
			l.pushNumber(TextFormatting.RED.ordinal());
			l.setField(-2, "Red");
			l.pushNumber(TextFormatting.LIGHT_PURPLE.ordinal());
			l.setField(-2, "Pink");
			l.pushNumber(TextFormatting.YELLOW.ordinal());
			l.setField(-2, "Yellow");
			l.pushNumber(TextFormatting.WHITE.ordinal());
			l.setField(-2, "White");

			l.pushNumber(TextFormatting.OBFUSCATED.ordinal());
			l.setField(-2, "Random");
			l.pushNumber(TextFormatting.BOLD.ordinal());
			l.setField(-2, "Bold");
			l.pushNumber(TextFormatting.STRIKETHROUGH.ordinal());
			l.setField(-2, "Strike");
			l.pushNumber(TextFormatting.UNDERLINE.ordinal());
			l.setField(-2, "Underline");
			l.pushNumber(TextFormatting.ITALIC.ordinal());
			l.setField(-2, "Italic");
			l.pushNumber(TextFormatting.RESET.ordinal());
			l.setField(-2, "Reset");
		}
		l.setGlobal("chat");
	}
}