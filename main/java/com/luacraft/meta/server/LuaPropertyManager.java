package com.luacraft.meta.server;

import net.minecraft.server.dedicated.PropertyManager;

import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;

public class LuaPropertyManager
{
	public static JavaFunction __tostring = new JavaFunction()
	{
		public int invoke(LuaState l)
		{
			PropertyManager self = (PropertyManager) l.checkUserdata(1, PropertyManager.class, "PropertyManager");
			l.pushString(String.format("PropertyManager: 0x%08x", l.toPointer(1)));
			return 1;
		}
	};
	
	/**
	 * @author Jake
	 * @function GetString
	 * Get a property value as a string
	 * @arguments [[String]]:key
	 * @return [[String]]:value
	 */

	public static JavaFunction GetString = new JavaFunction()
	{
		public int invoke(LuaState l)
		{
			PropertyManager self = (PropertyManager) l.checkUserdata(1, PropertyManager.class, "PropertyManager");
			l.pushString(self.getStringProperty(l.checkString(2), l.checkString(3)));
			return 1;
		}
	};
	
	/**
	 * @author Jake
	 * @function SetString
	 * Set a property value using a string
	 * @arguments [[String]]:key, [[String]]:value
	 * @return nil
	 */

	public static JavaFunction SetString = new JavaFunction()
	{
		public int invoke(LuaState l)
		{
			PropertyManager self = (PropertyManager) l.checkUserdata(1, PropertyManager.class, "PropertyManager");
			self.setProperty(l.checkString(2), l.checkString(3));
			return 0;
		}
	};
	
	/**
	 * @author Jake
	 * @function GetInt
	 * Get a property value as an integer
	 * @arguments [[String]]:key
	 * @return [[Number]]:value
	 */

	public static JavaFunction GetInt = new JavaFunction()
	{
		public int invoke(LuaState l)
		{
			PropertyManager self = (PropertyManager) l.checkUserdata(1, PropertyManager.class, "PropertyManager");
			l.pushInteger(self.getIntProperty(l.checkString(2), l.checkInteger(3)));
			return 1;
		}
	};
	
	/**
	 * @author Jake
	 * @function SetInt
	 * Set a property value using an integer
	 * @arguments [[String]]:key, [[Number]]:value
	 * @return nil
	 */

	public static JavaFunction SetInt = new JavaFunction()
	{
		public int invoke(LuaState l)
		{
			PropertyManager self = (PropertyManager) l.checkUserdata(1, PropertyManager.class, "PropertyManager");
			self.setProperty(l.checkString(2), l.checkInteger(3));
			return 0;
		}
	};
	
	/**
	 * @author Jake
	 * @function GetBool
	 * Get a property value as a boolean
	 * @arguments [[String]]:key
	 * @return [[Boolean]]:value
	 */

	public static JavaFunction GetBool = new JavaFunction()
	{
		public int invoke(LuaState l)
		{
			PropertyManager self = (PropertyManager) l.checkUserdata(1, PropertyManager.class, "PropertyManager");
			l.pushBoolean(self.getBooleanProperty(l.checkString(2), l.checkBoolean(3)));
			return 1;
		}
	};
	
	/**
	 * @author Jake
	 * @function SetBool
	 * Set a property value using a boolean
	 * @arguments [[String]]:key, [[Boolean]]:value
	 * @return nil
	 */

	public static JavaFunction SetBool = new JavaFunction()
	{
		public int invoke(LuaState l)
		{
			PropertyManager self = (PropertyManager) l.checkUserdata(1, PropertyManager.class, "PropertyManager");
			self.setProperty(l.checkString(2), l.checkBoolean(3));
			return 0;
		}
	};
	
	/**
	 * @author Jake
	 * @function Save
	 * Save all changes made to the property file
	 * @arguments nil
	 * @return nil
	 */

	public static JavaFunction Save = new JavaFunction()
	{
		public int invoke(LuaState l)
		{
			PropertyManager self = (PropertyManager) l.checkUserdata(1, PropertyManager.class, "PropertyManager");
			self.saveProperties();
			return 0;
		}
	};
	
	public static void Init(LuaState l)
	{		
		l.newMetatable("PropertyManager");
		{
			l.pushValue(-1);
			l.setField(-2, "__index");

			l.pushJavaFunction(__tostring);
			l.setField(-2, "__tostring");

			l.pushJavaFunction(GetString);
			l.setField(-2, "GetString");
			l.pushJavaFunction(SetString);
			l.setField(-2, "SetString");
			l.pushJavaFunction(GetInt);
			l.setField(-2, "GetInt");
			l.pushJavaFunction(SetInt);
			l.setField(-2, "SetInt");
			l.pushJavaFunction(GetBool);
			l.setField(-2, "GetBool");
			l.pushJavaFunction(SetBool);
			l.setField(-2, "SetBool");
			l.pushJavaFunction(Save);
			l.setField(-2, "Save");
		}
	}
}
