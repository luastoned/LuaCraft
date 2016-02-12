local diamond_thread = thread.NewThread("diamond_thread.lua")
print(diamond_thread)
if not diamond_thread:IsAlive() then
	print("Starting Thread")
	diamond_thread:Start()
end

local chan = thread.GetChannel("diamonds")

local diamonds = {}

local function indexVec(pos, vec)
	local index = ("%i%i%i"):format(pos.x,pos.y,pos.z)
	diamonds[index] = vec
end

hook.Add("game.tick", "Diamond Sync", function()
	while true do
		local pop = chan:Pop()
		if not pop then break end
		indexVec(pop, pop)
	end
end)

hook.Add("player.mineblock", "Diamond Break", function(ply, block, exp)
	if block:GetID() == 56 then
		indexVec(block:GetPos(), nil)
	end
end)

local block_size = Vector(1,1,1)

hook.Add("render.world", "Diamond Render", function()
	render.IgnoreZ(true)

	render.SetDrawColor(0,128,255)

	for _,pos in pairs(diamonds) do
		render.DrawBoundingBox(pos, pos + block_size)
	end

	render.IgnoreZ(false)
end)