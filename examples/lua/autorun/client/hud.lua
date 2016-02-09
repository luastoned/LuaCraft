local font = surface.GetDefaultFont()

local timeMat = Resource("luacraft:silkicons/time.png")
local vectMat = Resource("luacraft:silkicons/vector.png")
local eyeMat = Resource("luacraft:silkicons/eye.png")
local mapMat = Resource("luacraft:silkicons/map.png")

function _R.World:GetTimeString( ampm )
	local thing = "AM"
	local time = ( self:GetTime() + 8000 ) % 24000
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

hook.Add( "render.world", "debug", function(ticks)
	local me = LocalPlayer()

	render.SetDrawColor(255,255,255)

	for _,v in pairs(World():GetEntities()) do
		if v == me then continue end
		local text = v:GetClass()
		local w,h = font:GetSize(text)
		render.DrawText(text, v:GetEyePos(), Angle(-90,os.clock()*35,0), 0.25, -w/2, -h*2, true)
	end

	local tr = me:GetEyeTrace()

	render.SetDrawColor(0,0,255)
	--render.DrawLine(tr.StartPos, tr.HitPos)

	if tr.Hit then
		local norm = tr.HitNormal

		render.SetDrawColor(0,0,255)
		render.DrawCircle(tr.HitPos + tr.HitNormal * 0.01, 0.1, 0.05, norm:Angle())

		render.SetDrawColor(255,0,0)
		render.DrawLine(tr.HitPos, tr.HitPos + tr.HitNormal * 1)
	end

	local ent = tr.HitEntity

	if ent then
		local pos = ent:GetPos()
		render.SetDrawColor(0,255,0)
		render.DrawBoundingBox(ent:OBBMins(), ent:OBBMaxs())
	end
end)

hook.Add( "render.gameoverlay", "debug", function(time)
	local me = LocalPlayer()
	local world = World()
	local pos = me:GetPos()

	surface.SetDrawColor(255, 255, 255)
	surface.SetTexture( timeMat )
	surface.DrawTexturedRect( 2, 2, 8, 8 )
	font:DrawText( world:GetTimeString(true), 12, 2, true )
	
	surface.SetTexture( vectMat )
	surface.DrawTexturedRect( 2, 12, 8, 8 )
	font:DrawText( string.format("%d, %d", math.floor(pos.x), math.floor(pos.y)), 12, 12, true )
	
	surface.SetTexture( mapMat )
	surface.DrawTexturedRect( 2, 22, 8, 8 )
	font:DrawText( world:GetTopBlock(pos):GetBiome(), 12, 22, true )

	local tr = me:GetEyeTrace()

	surface.SetTexture( eyeMat )
	if tr.HitBlock then
		surface.DrawTexturedRect( 2, 32, 8, 8 )
		font:DrawText( tr.HitBlock:GetName(), 12, 32, true )
	elseif tr.HitEntity then
		local ent = tr.HitEntity
		local pos = ent:GetEyePos()

		surface.DrawTexturedRect( 2, 32, 8, 8 )
		font:DrawText( ent:GetClass(), 12, 32, true )
	end
end )