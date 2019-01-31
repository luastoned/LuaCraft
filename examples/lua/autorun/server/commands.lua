local function searchPlayer(arg)
	local matches = {}
	for k,v in pairs(game.GetPlayers()) do
		if v:GetName():find(arg) then
			table.insert(matches, v:GetName())
		end
	end
	return matches
end

command.Add("hello", function(ply, cmd, args)
	local target = game.GetPlayerByName(args[1])
	if target then
		target:ChatPrint(ply:GetName(), " says hello!")
	end
end):SetUsage("/hello <player>")

command.AutoComplete("hello", function(ply, cmd, args)
	return searchPlayer(args[1])
end)