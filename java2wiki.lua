--[[
	/**
	* @author LuaStoned
	* @function Player:Kick()
	* Kick the player
	* @arguments String:arg
	* @return nil
	*/
]]

local fileDir = "D:/Developer/Java/LuaCraft-Forge/"
local javaDir = "src/main/java/com/luacraft/"
 
local pattern           = "/%*%*([%w%s%p]-)%*/"
local pattern_info		= "%*%s([^@].-)\n"

local fmt_header        = "==== %s(%s) ===="
local fmt_alias         = "* '''Alias''' to this function: '''%s'''"
local fmt_return        = "* Return value: '''%s'''"
local fmt_info          = "* '''Information''': %s"
local fmt_derive		= "* '''Inherited''' from [[%s]]"

function fileRead(file)
    local f = assert(io.open(file, "rb"))
    local content = f:read("*all")
    f:close()
    return content
end

function fileWrite(file,data)
    local f = assert(io.open(file, "w"))
    f:write(data)
	f:close()
end

function fileAppend(file,data)
    local f = assert(io.open(file, "a"))
    f:write(data)
	f:close()
end

function grabdoc(fileName)
	local fullPath = fileDir .. javaDir .. fileName .. ".java"
	local str = fileRead(fullPath)
	local tbl = {}

	for doc in str:gmatch(pattern) do
		local author	= doc:match("@author%s(.-)\r\n")
		local func		= doc:match("@function%s(.-)\r\n")
		local alias		= doc:match("@alias%s(.-)\r\n")
		local arg		= doc:match("@arguments%s(.-)\r\n")
		local ret		= doc:match("@return%s(.-)\r\n")
		
		arg = (arg == "nil") and "" or arg -- (" " .. arg .. " ")
		arg = arg:gsub(":", " ")
		ret = ret:gsub(":", " ")

		local info = {}
		for add in doc:gmatch(pattern_info) do
			table.insert(info, add)
		end    
		local add_info = table.concat(info, "\n")
		
		local fmt = {}
		table.insert(fmt, string.format(fmt_header, func, arg))
		if (alias) then table.insert(fmt, string.format(fmt_alias, alias)) end
		table.insert(fmt, string.format(fmt_return, ret))
		if (#info > 0) then table.insert(fmt, string.format(fmt_info, add_info)) end
		--if (... == true) then table.insert(fmt, string.format(fmt_derive, name)) end
		table.insert(tbl, {alias = alias, func = func, fmt = table.concat(fmt, "\n")})
	end
	
	return tbl
end

function java2wiki(out, space, str, append)
	local function funcSort(a, b)
		return a.func < b.func
	end
	
	local doc = grabdoc(str)
	table.sort(doc, funcSort)
	
	local fmt = {}
	for k, tbl in pairs(doc) do
		table.insert(fmt, tbl.fmt)	
	end
	
	local docStr = string.format("= %s =\n\n%s", space, table.concat(fmt, "\n\n"))
	if (append) then
		docStr = "\n\n" .. docStr
		fileAppend(fileDir .. "wiki/" .. out .. ".txt", docStr)
	else
		fileWrite(fileDir .. "wiki/" .. out .. ".txt", docStr)
	end
end

-- Library

java2wiki("Input", "Client", "library/client/LuaLibInput")
java2wiki("Profiler", "Client", "library/client/LuaLibProfiler")
java2wiki("Render", "Client", "library/client/LuaLibRender")
java2wiki("Surface", "Client", "library/client/LuaLibSurface")

java2wiki("Globals", "Client", "library/client/LuaGlobals")
java2wiki("Globals", "Shared", "library/LuaGlobals", true)
java2wiki("Globals", "Server", "library/server/LuaGlobals", true)

java2wiki("Game", "Client", "library/client/LuaLibGame")
java2wiki("Game", "Server", "library/server/LuaLibGame", true)

java2wiki("SQL", "Shared", "library/LuaLibSQL")
java2wiki("Util", "Shared", "library/LuaLibUtil")

java2wiki("Command", "Server", "library/server/LuaLibCommand")

-- Meta

java2wiki("Font", "Client", "meta/client/LuaFont")

java2wiki("Angle", "Shared", "meta/LuaAngle")
java2wiki("Block", "Shared", "meta/LuaBlock")
java2wiki("Color", "Shared", "meta/LuaColor")
java2wiki("Container", "Shared", "meta/LuaContainer")
java2wiki("DataWatcher", "Shared", "meta/LuaDataWatcher")
java2wiki("ItemStack", "Shared", "meta/LuaItemStack")
java2wiki("Living", "Shared", "meta/LuaLiving")
java2wiki("LivingBase", "Shared", "meta/LuaLivingBase")
java2wiki("NBTTag", "Shared", "meta/LuaNBTTag")
java2wiki("Resource", "Shared", "meta/LuaResource")
java2wiki("SQLDatabase", "Shared", "meta/LuaSQLDatabase")
java2wiki("SQLQuery", "Shared", "meta/LuaSQLQuery")
java2wiki("Thread", "Shared", "meta/LuaThread")
java2wiki("World", "Shared", "meta/LuaWorld")

java2wiki("ByteBuf", "Client", "meta/client/LuaByteBuf")
java2wiki("ByteBuf", "Shared", "meta/LuaByteBuf", true)
java2wiki("ByteBuf", "Server", "meta/server/LuaByteBuf", true)

java2wiki("Entity", "Client", "meta/client/LuaEntity")
java2wiki("Entity", "Shared", "meta/LuaEntity", true)

java2wiki("EntityItem", "Shared", "meta/LuaEntityItem")

java2wiki("Player", "Shared", "meta/LuaPlayer")
java2wiki("Player", "Server", "meta/server/LuaPlayer", true)

java2wiki("Vector", "Client", "meta/client/LuaVector")
java2wiki("Vector", "Shared", "meta/LuaVector", true)

java2wiki("PropertyManager", "Server", "meta/server/LuaPropertyManager")

java2wiki("Resource", "Shared", "meta/LuaResource")























