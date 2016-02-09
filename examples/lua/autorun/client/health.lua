local font = surface.GetDefaultFont()

hook.Add( "render.gameoverlay", "Game - Render Overlay", function()
	local me = LocalPlayer()

	for _,v in pairs( World():GetEntities() ) do
		if v == me then continue end
		
		local pos = v:GetPos()

		if v:IsLiving() then
			pos = v:GetEyePos()
		end

		local vis, x, y = pos:ToScreen()

		if not vis then continue end

		surface.SetDrawColor(255, 255, 255, 255)

		local dist = pos:Distance(me:GetPos())
		
		if dist <= 30 then
			local alpha = math.Clamp( 1 - (dist/32), 0, 1 ) * 255
			
			if v:IsMob() then
				surface.SetDrawColor(255, 0, 0, alpha)
			elseif v:IsAnimal() then
				surface.SetDrawColor(0, 255, 0, alpha)
			else
				surface.SetDrawColor(0, 0, 255, alpha)
			end
			
			surface.DrawRect(x - 1, y - 1, 2, 2)

			local text = v:GetClass()

			if v:IsItem() then
				text = v:GetItemStack():GetName()
			end
			
			surface.SetDrawColor(255, 255, 255, alpha)
			local w,h = font:GetSize(text)
			font:DrawText(text, x - w / 2, y - 16, true)
			
			if v:IsLiving() then
				local healthPerc = v:GetHealth() / v:GetMaxHealth()
				surface.SetDrawColor(0, 0, 0, alpha)
				surface.DrawRect(x - 8, y - 8, 16, 2)
				surface.SetDrawColor( (1 - healthPerc) * 255, healthPerc * 255, 0, alpha)
				surface.DrawRect(x - 8, y - 8, 16 * healthPerc, 2)
			end
		end
	end
end )