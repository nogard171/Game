package core;

import java.awt.Rectangle;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import game.Base;
import utils.Renderer;

public class PlayerController {

	public static float walkFrame = 0;
	public static int maxWalkFrame = 4;

	public static void update() {
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			WorldManager.player.move(-1, 0);
			WorldManager.player.direction = "left";
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			WorldManager.player.move(1, 0);
			WorldManager.player.direction = "right";
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			WorldManager.player.move(0, -1);
			WorldManager.player.direction = "up";
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			WorldManager.player.move(0, 1);
			WorldManager.player.direction = "down";
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_A)
				| Keyboard.isKeyDown(Keyboard.KEY_W) | Keyboard.isKeyDown(Keyboard.KEY_D)) {
			WorldManager.player.isWalking = true;
		} else {
			WorldManager.player.isWalking = false;
			WorldManager.player.action = "idle";
		}

		if (WorldManager.player.isWalking) {
			walkFrame += 0.005f;
		}
		if (walkFrame > maxWalkFrame) {
			walkFrame = 0;
		}
		keepPlayerInView(Integer.parseInt(Base.settings.getProperty("control.movement_style")));
	}

	public static void render() {
		if (WorldManager.player.isWalking) {
			WorldManager.player.action = "walk_" + (int) Math.floor(walkFrame);
		}

		GL11.glBegin(GL11.GL_QUADS);
		Renderer.renderSprite(WorldManager.player.position.x, WorldManager.player.position.y,
				WorldManager.player.sprite + "_" + WorldManager.player.direction + "_" + WorldManager.player.action);
		GL11.glEnd();
	}

	public static Rectangle viewBounds = null;

	public static void keepPlayerInView(int style) {
		Player player = WorldManager.player;
		if (style == 0) {
			viewBounds = new Rectangle(100, 100, Integer.parseInt(Base.settings.getProperty("window.width")) - 200,
					Integer.parseInt(Base.settings.getProperty("window.height")) - 200);
			if ((int) player.position.x < viewBounds.x - WorldManager.view.x) {
				WorldManager.view.x += 1;
			}
			if ((int) player.position.x > viewBounds.width + 100 - 32 - WorldManager.view.x) {
				WorldManager.view.x -= 1;
			}

			if ((int) player.position.y < viewBounds.y - WorldManager.view.y) {
				WorldManager.view.y += 1;
			}
			if ((int) player.position.y > viewBounds.height + 100 - 64 - WorldManager.view.y) {
				WorldManager.view.y -= 1;
			}
		} else if (style == 1) {
			WorldManager.view.x = (int) -player.position.x
					+ ((Integer.parseInt(Base.settings.getProperty("window.width")) / 2) - 16);
			WorldManager.view.y = (int) -player.position.y
					+ ((Integer.parseInt(Base.settings.getProperty("window.height")) / 2) - 32);
		}
	}
}
