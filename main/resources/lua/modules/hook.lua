local pairs	= pairs
local type	= type

module("hook")

local hooks = {}
function GetTable() return hooks end

function Add(hook, name, func)
	if (not type(hook) == "string") then return end
	if (not type(name) == "string") then return end
	if (not type(func) == "function") then return end

	if hooks[hook] == nil then
		hooks[hook] = {}
	end

	hooks[hook][name] = func
end

function Remove(hook, name)
	if (not type(hook) == "string") then return end
	if (not type(name) == "string") then return end

	hooks[hook][name] = nil
end

function Call(name, ...)
	local hook = hooks[name]
	if hook == nil then return end
	
	local a, b, c, d, e, f
	for _, func in pairs(hook) do
		a, b, c, d, e, f = func(...)
		
		if a ~= nil then
			return a, b, c, d, e, f
		end
	end
end