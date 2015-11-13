local ByteBuf = ByteBuf
local type	= type

module("net")

local callbacks = {}

function Receive(name, func)
	if (not type(name) == "string") then return end
	if (not type(func) == "function") then return end

	callbacks[name] = func
end

function Incoming(buff, ply)
	local name = buff:ReadString()
	local func = callbacks[name]
	
	if not func then return end
	if (not type(func) == "function") then return end
	
	callbacks[name]( buff, ply )
end

function Start(name,length)
	local buff = ByteBuf(length)
	buff:WriteString(name)
	return buff
end