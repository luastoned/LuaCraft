local AMPMTime = true

local translateWorldName = {
	[WORLD_NETHER] = "hell",
	[WORLD_OVERWORLD] = "main",
	[WORLD_END] = "END",
}

local easyWorldName = {
	["hell"] = WORLD_NETHER,
	["nether"] = WORLD_NETHER,
	["main"] = WORLD_OVERWORLD,
	["default"] = WORLD_OVERWORLD,
	["world"] = WORLD_OVERWORLD,
	["end"] = WORLD_END,
	["theend"] = WORLD_END,
	["the_end"] = WORLD_END,
}

local function searchPlayer(arg)
	local matches = {}
	for k,v in pairs(game.GetPlayers()) do
		if v:GetName():find(arg) then
			table.insert(matches, v:GetName())
		end
	end
	return matches
end

local function playerAutoComplete(ply, cmd, args)
	return searchPlayer(args[1])
end

local function playerHasPermission(ply)
	if game.IsSinglePlayer() then return true end
	return ply:IsOP()
end

command.Add( "rain", function( ply, cmd, args )
	if not playerHasPermission(ply) then return end
	local world = args[2] and easyWorldName[ args[2] ] and World( easyWorldName[ args[2] ] ) or ply:GetWorld()
	args[1] = args[1] or ( world:IsRaining() and 0 or 1 )
	if args[1] then
		local boolean = tonumber( args[1] ) == 1 and true or false
		world:SetRaining( boolean )
		game.ChatPrint( "Admin ", chat.Green, ply:GetName(), chat.Teal, ( boolean and " enabled" or " disabled" ), chat.White, " rain on the ", translateWorldName[ world:GetDimension() ] or "Unknown", " world" )
	else
		ply:ChatPrint( string.format( "/%s <1=on|0=off> [world]", cmd ) )
	end
end ):SetUsage("/rain <1=on|0=off> [world]")

command.Add( "storm", function( ply, cmd, args )
	if not playerHasPermission(ply) then return end
	local world = args[2] and easyWorldName[ args[2] ] and World( easyWorldName[ args[2] ] ) or ply:GetWorld()
	args[1] = args[1] or ( world:IsStorming() and 0 or 1 )
	if args[1] then
		local boolean = tonumber( args[1] ) == 1 and true or false
		world:SetStorming( boolean )
		game.ChatPrint( "Admin ", chat.Green, ply:GetName(), chat.Teal, ( boolean and " enabled" or " disabled" ), chat.White, " storming on the ", translateWorldName[ world:GetDimension() ] or "Unknown", " world" )
	else
		ply:ChatPrint( string.format( usage, "/%s <1=on|0=off> [world]" ) )
	end
end ):SetUsage("/storm <1=on|0=off> [world]")

command.Add( "pvp", function( ply, cmd, args )
	if not playerHasPermission(ply) then return end
	args[1] = args[1] or ( game.GetPVP() and 0 or 1 )
	if args[1] then
		local boolean = tonumber( args[1] ) == 1 and true or false
		game.SetPVP( boolean )
		game.ChatPrint( "Admin ", chat.Green, ply:GetName(), chat.Teal, ( boolean and " enabled" or " disabled" ), chat.White, " PVP" )
	else
		ply:ChatPrint( string.format( "/%s <1=on|0=off>", cmd ) )
	end
end ):SetUsage("/pvp <1=on|0=off>")

command.Add( "goto", function( ply, cmd, args )
	if not playerHasPermission(ply) then return end
	if args[1] then
		local target = game.GetPlayerByName( args[1] )
		if target ~= nil then
			ply:TravelToWorld( target:GetWorld() )
			ply:SetPos( target:GetPos() )
		else
			ply:ChatPrint( "Player not found" )
		end
	else
		ply:ChatPrint( string.format( "/%s <player>", cmd ) )
	end
end ):SetUsage("/goto <player>")
command.AutoComplete("goto", playerAutoComplete)

command.Add( "bring", function( ply, cmd, args )
	if not playerHasPermission(ply) then return end
	if args[1] then
		local target = game.GetPlayerByName( args[1] )
		if target ~= nil then
			target:TravelToWorld( ply:GetWorld() )
			target:SetPos( ply:GetPos() )
		else
			ply:ChatPrint( "Player not found" )
		end
	else
		ply:ChatPrint( string.format( "/%s <player>", cmd ) )
	end
end ):SetUsage("/bring <player>")
command.AutoComplete("bring", playerAutoComplete)

command.Add( "burn", function( ply, cmd, args )
	if not playerHasPermission(ply) then return end
	if args[1] then
		local target = game.GetPlayerByName( args[1] )
		if target ~= nil then
			if target:IsBurning() then
				target:Extinguish()
				game.ChatPrint( "Admin ", chat.Green, ply:GetName(), chat.White, " extinguished player ", chat.Green, target:GetName() )
			else
				target:Ignite()
				game.ChatPrint( "Admin ", chat.Green, ply:GetName(), chat.White, " burned player ", chat.Green, target:GetName() )
			end
		else
			ply:ChatPrint("Player not found")
		end
	else
		ply:ChatPrint( string.format( "/%s <player>", cmd ) )
	end
end ):SetUsage("/burn <player>")
command.AutoComplete("burn", playerAutoComplete)

command.Add( "ban", function( ply, cmd, args )
	if not playerHasPermission(ply) then return end
	if args[1] then
		local target = game.GetPlayerByName( args[1] )
		if target ~= nil then
			target:Ban( args[2] or "No reason given" )
			game.ChatPrint( "Admin ", chat.Green, ply:GetName(), chat.White, " banned player ", chat.Green, target:GetName() )
		else
			ply:ChatPrint( "Player not found" )
		end
	else
		ply:ChatPrint( string.format( "/%s <player> [reason]", cmd ) )
	end
end ):SetUsage("/ban <player> [reason]")
command.AutoComplete("ban", playerAutoComplete)

command.Add( "kick", function( ply, cmd, args )
	if not playerHasPermission(ply) then return end
	if args[1] then
		local target = game.GetPlayerByName( args[1] )
		if target ~= nil then
			target:Kick( args[2] or "No reason given" )
			game.ChatPrint( "Admin ", chat.Green, ply:GetName(), chat.White, " kicked player ", chat.Green, target:GetName() )
		else
			ply:ChatPrint( "Player not found" )
		end
	else
		ply:ChatPrint( string.format( "/%s <player> [reason]", cmd ) )
	end
end ):SetUsage("/kick <player> [reason]")
command.AutoComplete("kick", playerAutoComplete)

command.Add( "slay", function( ply, cmd, args )
	if not playerHasPermission(ply) then return end
	if args[1] then
		local target = game.GetPlayerByName( args[1] )
		if target ~= nil then
			target:Kill()
			game.ChatPrint( "Admin ", chat.Green, ply:GetName(), chat.White, " slayed player ", chat.Green, target:GetName() )
		else
			ply:ChatPrint( "Player not found" )
		end
	else
		ply:ChatPrint( string.format( "/%s <player>", cmd ) )
	end
end ):SetUsage("/slay <player>")
command.AutoComplete("slay", playerAutoComplete)

function _R.World:GetTimeString( ampm )
	local thing = "AM"
	local time = ( self:GetTime() + 6000 ) % 24000
	local hour = math.floor( time / 1000 )
	local hourRemainder = time % 1000
	local minute = math.floor( ( hourRemainder / 1000 ) * 60 ) % 60
	if ampm then
		if hour > 12 then
			hour = hour - 12
			thing = "PM"
		elseif hour == 0 then
			hour = 12
			thing = "AM"
		end
		return string.format("%02d:%02d %s", hour, minute, thing)
	end
	
	return string.format("%02d:%02d", hour, minute)
end

command.Add( "thetime", function( ply, cmd, args )
	ply:ChatPrint( "World Time: ", chat.Green, ply:GetWorld():GetTimeString( AMPMTime ) )
	ply:ChatPrint( "Server Time: ", chat.Green, os.date( "%I:%M %p" ) )
end )

local TimeTranslate = {
	["sunrise"] = 5, -- Very start of daybreak
	["day"] = 6, -- Sun has just finished rising
	["noon"] = 12, -- Sun is directly overhead
	["sunset"] = 18, -- Sun is just about to set
	["night"] = 20, -- Very start of night
	["midnight"] = 0, -- Moon is directly overhead
}

command.Add( "time", function( ply, cmd, args )
	if not playerHasPermission(ply) then return end
	if args[1] then
		args[1] = TimeTranslate[ args[1]:lower() ] or tonumber( args[1] )
		local world = args[2] and easyWorldName[ args[2] ] and World( easyWorldName[ args[2] ] ) or ply:GetWorld()
		world:SetTime( ( ( args[1] - 6 ) % 24 ) * 1000 )
		game.ChatPrint( "Admin ", chat.Green, ply:GetName(), chat.White, " set time to " .. world:GetTimeString( AMPMTime ) .. " on the ", translateWorldName[ world:GetDimension() ] or "Unknown", " world" )
	else
		ply:ChatPrint( string.format( "/%s <sunrise|day|noon|sunset|night|midnight|number> [world]", cmd ) )
	end
end ):SetUsage("/time <sunrise|day|noon|sunset|night|midnight|number> [world]")

command.Add( "i", function( ply, cmd, args )
	if not playerHasPermission(ply) then return end
	if args[1] then
		local id = item[ args[1] ] or tile[ args[1] ] or tonumber( args[1] )
		local stack = Item( id, args[2], args[3] )
		ply:ChatPrint( chat.White, "You recieved", chat.Teal, " " .. stack:GetSize(), chat.White, " of ", chat.Red, stack:GetName() )
		ply:GetContainer():Give( stack )
	else
		ply:ChatPrint( string.format( "/%s <item> [amount]", cmd ) )
	end
end ):SetUsage("/i <item> [amount]")

command.Add( "give", function( ply, cmd, args )
	if not playerHasPermission(ply) then return end
	if args[1] then
		local target = game.GetPlayerByName( args[1] )
		if target ~= nil then
			local id = item[ args[2] ] or tile[ args[2] ] or tonumber( args[2] )
			local stack = Item( id, args[3], args[4] )
			if ply ~= target then
				ply:ChatPrint( chat.White, "You gave", chat.Teal, " " .. stack:GetSize(), chat.White, " of ", chat.Red, stack:GetName(), chat.White, " to ", chat.Green, target:GetName() )
				target:ChatPrint( chat.White, "You recieved", chat.Teal, " " .. stack:GetSize(), chat.White, " of ", chat.Red, stack:GetName(), chat.White, " from ", chat.Green, ply:GetName() )
			else
				ply:ChatPrint( chat.White, "You recieved", chat.Teal, " " .. stack:GetSize(), chat.White, " of ", chat.Red, stack:GetName() )
			end
			target:GetContainer():Give( stack )
		else
			ply:ChatPrint( "Player not found" )
		end
	else
		ply:ChatPrint( string.format( "/%s <player> <item> [amount]", cmd ) )
	end
end ):SetUsage("/give <player> <item> [amount]")
command.AutoComplete("give", playerAutoComplete)

command.Add( "heal", function( ply, cmd, args )
	if not playerHasPermission(ply) then return end
	if args[1] then
		local target = game.GetPlayerByName( args[1] )
		if target ~= nil then
			target:SetHealth( ply:GetMaxHealth() )
			target:ChatPrint( "Your health has been restored" )
			ply:ChatPrint( chat.White, "You healed ", chat.Green, target:GetName() )
		else
			ply:ChatPrint( "Player not found" )
		end
	else
		ply:SetHealth( ply:GetMaxHealth() )
		ply:ChatPrint( "Your health has been restored" )
	end
end ):SetUsage("/heal [player]")
command.AutoComplete("heal", playerAutoComplete)

command.Add( "god", function( ply, cmd, args )
	if not playerHasPermission(ply) then return end
	if args[1] then
		local target = game.GetPlayerByName( args[1] )
		if target ~= nil then
			local isgodded = target:IsGod()
			target:SetGod( not isgodded )
			target:ChatPrint( "Admin ", chat.Green, ply:GetName(), " made you", chat.Red, isgodded and " mortal" or " invincible" )
			ply:ChatPrint( chat.White, "You made ", chat.Green, target:GetName(), chat.Red, isgodded and " mortal" or " invincible" )
		else
			ply:ChatPrint( "Player not found" )
		end
	else
		local isgodded = ply:IsGod()
		ply:SetGod( not isgodded )
		ply:ChatPrint( isgodded and "You are now mortal" or "You are now invincible" )
	end
end ):SetUsage("/god [player]")
command.AutoComplete("god", playerAutoComplete)

command.Add( "/", function( ply, cmd, args )
	if not playerHasPermission(ply) then return end
	ply.SuperPickAxe = not ply.SuperPickAxe
	ply:ChatPrint( chat.Red, string.format( "Super dig %s", ( ply.SuperPickAxe and "enabled" or "disabled" ) ) )
end )

hook.Add( "player.digblock", "Superdig", function( ply, block )
	if ply.SuperPickAxe then
		block:Break()
	end
end )

command.Add( "warp", function( ply, cmd, args )
	if not playerHasPermission(ply) then return end
	local tr = ply:GetEyeTrace()
	if tr.HitPos then
		ply:SetPos( tr.HitPos )
	end
end )

local easymodes = {
	["creative"] = CREATIVE,
	["survival"] = SURVIVAL,
	["create"] = CREATIVE,
	["survive"] = SURVIVAL,
}

command.Add( "gamemode", function( ply, cmd, args )
	if not playerHasPermission(ply) then return end
	if args[1] and args[2] then
		local target = game.GetPlayerByName( args[1] )
		args[2] = easymodes[ args[2]:lower() ] or args[2]
		if target ~= nil then
			target:SetGamemode( tonumber( args[2] ) )
			ply:ChatPrint( chat.White, "You set ", chat.Green, target:GetName(), chat.White, " gamemode to ", chat.Red, target:GetGamemode() == CREATIVE and "creative" or "survival" )
		else
			ply:ChatPrint( "Player not found" )
		end
	elseif args[1] then
		args[1] = easymodes[ args[1] ] or tonumber( args[1] )
		ply:SetGamemode( args[1] )
	else
		ply:SetGamemode( ply:GetGamemode() == SURVIVAL and CREATIVE or SURVIVAL )
	end
end ):SetUsage("/gamemode [player] <gamemode>")
command.AutoComplete("gamemode", playerAutoComplete)

command.Add( "create", function( ply, cmd, args )
	if not playerHasPermission(ply) then return end
	if args[1] then
		local tr = ply:GetEyeTrace ()
		ply:GetWorld():Spawn( args[1], tr.HitPos )
	else
		ply:ChatPrint( string.format( "/%s <class>", cmd ) )
	end
end )