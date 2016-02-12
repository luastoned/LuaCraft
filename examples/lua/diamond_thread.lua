local script = thread.GetThread()

local chan = thread.GetChannel("diamonds")

local function VectorPairs( vec1, vec2 )
	local tbl = {}
	-- There HAS to be a better way..
	local startx = vec1.x < vec2.x and vec1.x or vec2.x
	local starty = vec1.y < vec2.y and vec1.y or vec2.y
	local startz = vec1.z < vec2.z and vec1.z or vec2.z
	local endx = vec1.x >= vec2.x and vec1.x or vec2.x
	local endy = vec1.y >= vec2.y and vec1.y or vec2.y
	local endz = vec1.z >= vec2.z and vec1.z or vec2.z
	for x=startx,endx do
		for y=starty,endy do
			for z=startz,endz do
				table.insert( tbl, Vector( x, y, z ) )
			end
		end
	end
	return pairs( tbl )
end

local chunks = 2

local min = Vector(-16*chunks, -16*chunks, 0)
local max = Vector(16*chunks, 16*chunks, 15)

while not script:IsInterrupted() do -- Run until something stops us
	if LocalPlayer() then
		local world = World()

		local pos = LocalPlayer():GetPos()
		pos.z = 0

		local block

		for _,pos in VectorPairs(pos + min, pos + max) do
			block = Block(pos)
			if not block or block:GetID() ~= 56 then continue end
			chan:Push(block:GetPos()) -- Send positions to the channel that the main thread can read from
		end
	end
	collectgarbage()
	script:Sleep(1000)
end