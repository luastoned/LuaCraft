local testThread = thread.NewThread("diamond_thread.lua")
testThread:Start()

local chan = thread.GetChannel("diamonds")

local diamonds = {}

hook.Add("game.tick", "Diamond Sync", function()
	while true do
		local pop = chan:Pop()
		if not pop then break end
		diamonds[pop] = true
	end
end)

local block_size = Vector(1,1,1)

hook.Add("render.world", "Diamond Render", function()
	render.IgnoreZ(true)

	render.SetDrawColor(0,128,255)

	for pos,_ in pairs(diamonds) do
		render.DrawBoundingBox(pos, pos + block_size)
	end

	render.IgnoreZ(false)
end)