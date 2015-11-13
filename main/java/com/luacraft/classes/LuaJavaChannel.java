package com.luacraft.classes;

import java.util.Stack;

import com.naef.jnlua.LuaState;
import com.naef.jnlua.LuaType;
import com.naef.jnlua.LuaUserdata;

public class LuaJavaChannel extends Stack implements LuaUserdata
{
	public String getTypeName()
	{
		return "Channel";
	}
	
	public void pushStackItem(LuaState l, LuaChannelObject item)
	{		
		switch (item.type)
		{
		case BOOLEAN:
			l.pushBoolean((Boolean) item.object);
			break;
		case LIGHTUSERDATA:
			l.pushUserdata(item.object);
			break;
		case NIL:
			l.pushNil();
			break;
		case NUMBER:
			l.pushNumber((Double) item.object);
			break;
		case STRING:
			l.pushString((String) item.object);
			break;
		case USERDATA:
			if (item.meta != null)
				l.pushUserdataWithMeta(item.object, item.meta);
			else
				l.pushUserdata(item.object);
			break;
		default:
			l.pushNil();
			break;
		}
	}
	
	public LuaChannelObject getObject(LuaState l, int index)
	{
		LuaType type = l.type(index);
		Object object = null;
		String meta = null;
		
		switch (type)
		{
		case BOOLEAN:
			object = l.toBoolean(index);
			break;
		case LIGHTUSERDATA:
			object = l.toUserdata(index);
		case NIL:
			object = null;
			break;
		case NUMBER:
			object = l.toNumber(index);
			break;
		case STRING:
			object = l.toString(index);
			break;
		case USERDATA:
			object = l.toUserdata(index);
			if (object instanceof LuaUserdata)
				meta = ((LuaUserdata) object).getTypeName();
			break;
		default:
			break;
		}
		
		return new LuaChannelObject(type, object, meta);
	}
	
	public synchronized boolean empty()
	{
		return super.empty();
	}

	public synchronized void peek(LuaState l)
	{
		LuaChannelObject object = (LuaChannelObject) peek();
		pushStackItem(l,object);
	}

	public synchronized void pop(LuaState l)
	{
		LuaChannelObject object = (LuaChannelObject) pop();
		pushStackItem(l,object);
	}

	public synchronized void push(LuaState l, int index)
	{
		LuaChannelObject object = getObject(l,index);
		push(object);
	}

}
