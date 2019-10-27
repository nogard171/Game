dofile "system/scripts/keyboard_keys.lua"
function setup()
	
end
function update()
	
	speed = 0.08f * fps:getCurrentDelta()
	
	xSpeed = 0
	ySpeed = 0
	rot=0
	walking=false
	if texX == nil then
	   texX=0
	end
	texSpeed= 0.002f * fps:getCurrentDelta()
	if keyboard:isKeyDown(SHIFT) then
		speed=speed*2;
		texSpeed=texSpeed*2
	end
	
	if keyboard:isKeyDown(D) then
		xSpeed = speed
		texY=2
		walking=true
	end
	
	if keyboard:isKeyDown(A) then
		xSpeed = -speed
		texY=3
		walking=true
	end
	
	if keyboard:isKeyDown(W) then
		ySpeed = speed
		texY=1
		walking=true
	end
	
	if keyboard:isKeyDown(S) then
		ySpeed = -speed
		texY=0
		walking=true
	end
	
	
	texX= texX + texSpeed
	
	
	
	if not walking then
		--audio:stop()
		texX=0
	else
		--audio:play()
	end
	
	if texX>4 then
		texX=0
	end
	
	animator:setFrameY(texY)
	animator:setFrameX(texX)
	transform:Move(xSpeed,ySpeed)

end