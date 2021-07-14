package engine;

import java.awt.Point;

import org.lwjgl.util.vector.Vector2f;

public class Entity extends Object {
	public float gravity = 9.8f;
	public float jumpValue = 0f;
	public Vector2f position;
	public boolean moved = false;
	public boolean jumping = false;
	public boolean grounded = false;

	public Entity(int x, int y) {
		position = new Vector2f(x, y);
	}

	public void move(float forceX, float forceY) {
		if (forceX != 0 || forceY != 0) {
			moved = true;
		}

		if (moved) {
			position.x += forceX;
			position.y += forceY;
		}
	}

	float jumpHeight = 100;
	float jumpY = 0;
	boolean falling = true;

	public void jump() {
		if (!jumping && grounded && !falling) {
			jumping = true;
			grounded = false;
			jumpY = position.y - jumpHeight;
		}

	}

	private void handleGravity() {
		Point playerIndex = new Point((int) Math.floor(position.x / 32), (int) Math.floor(position.y / 32));
		Point rightIndex = new Point((int) Math.floor(position.x / 32), (int) Math.floor((position.y + 32) / 32));
		Point leftIndex = new Point((int) Math.floor((position.x + 32) / 32), (int) Math.floor((position.y + 32) / 32));

		System.out.println("Index: " + playerIndex + "=" + rightIndex + "/" + leftIndex);

		if (position.y + 32 <= 0) {
			grounded = false;

		}
		if (position.y + 32 >= 0) {
			grounded = true;
			falling = false;
		}

		if (jumping && position.y >= jumpY && !falling) {
			jumpValue = 0.1f;
			position.y -= jumpValue * gravity;
		}
		if (position.y < jumpY && !grounded) {
			falling = true;
			jumping = false;
		}
		if (!grounded && falling) {
			position.y += (gravity / 50);
		}
	}

	public void finalizeMove() {
		handleGravity();
		if (moved) {
			moved = false;
		}
	}
}
