package core;

import org.lwjgl.util.vector.Vector2f;

import utils.FPS;

public class Player {
	public Vector2f position;
	public String sprite = "character";
	public String direction ="down";
	public String action = "idle";
	public boolean isWalking = false;
	
	public Player()
	{
		position = new Vector2f(0,0);
	}

	public void move(float x, float y) {
		float delta = FPS.delta;
		float speed = delta/10;
		
		position.x += x*speed;
		position.y += y*speed;
	}
	
}
