package core;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.GLIndex;
import classes.GLMenu;
import classes.GLObject;
import game.GLWorldMenu;
import game.Main;
import utils.GLDebug;
import utils.GLFPS;

public class GLUIManager {
	public static ArrayList<GLMessage> messages = new ArrayList<GLMessage>();

	GLWorldMenu menu;

	public void setup() {
		menu = new GLWorldMenu();
		menu.setup();
	}

	public void update() {
		if (Mouse.isButtonDown(1)) {
			if (GLChunkManager.mouseGLIndex.size() > 0) {
				GLIndex hover = GLChunkManager.mouseGLIndex.get(GLChunkManager.mouseGLIndex.size() - 1);
				GLChunk chunk = GLChunkManager.chunks.get(new GLIndex(hover.chunkX, hover.chunkY, hover.chunkZ));
				if (chunk != null) {
					GLObject obj = chunk.objects.get(new GLIndex(hover.x, hover.y, hover.z));
					if (obj != null) {
						if (obj.isKnown()) {
							int posX = (int) (chunk.position.x
									+ ((obj.getPositionGLIndex().x - obj.getPositionGLIndex().z) * 32));
							int posY = chunk.position.y - ((obj.getPositionGLIndex().y - 1) * 32);
							int posZ = (int) (((obj.getPositionGLIndex().z + obj.getPositionGLIndex().x) * 16) + posY);

							menu.setIndex(hover);
							menu.setPosition(posX, posZ);
							menu.showMenu(obj.getType());
						}
					}
				}
			}
		}

		menu.update();

		for (GLMessage message : messages) {
			message.setShowTime(System.currentTimeMillis());
			if (message.getTimeout() - message.getShowTime() < 0) {
				messages.remove(message);
				break;
			}
		}
	}

	public void render() {

		menu.render();

		GLDebug.RenderBackground(0, 0, 200, 78);
		GLDebug.RenderString(0, 0, "FPS: " + GLFPS.fps, 12, Color.white);

		GLDebug.RenderString(0, 12, "Render Count: " + GLChunkManager.totalRenderCount, 12, Color.white);
		GLDebug.RenderString(0, 24, "Level: " + GLChunkManager.currentLevel, 12, Color.white);
		if (GLChunkManager.mouseGLIndex.size() > 0) {

			GLIndex hover = GLChunkManager.mouseGLIndex.get(GLChunkManager.mouseGLIndex.size() - 1);
			GLDebug.RenderString(0, 36,
					"Hover Chunk GLIndex: " + hover.chunkX + "," + hover.chunkY + "," + hover.chunkZ, 12, Color.white);
			GLDebug.RenderString(0, 48, "Hover GLIndex: " + hover.x + "," + hover.y + "," + hover.z, 12, Color.white);

			GLChunk chunk = GLChunkManager.chunks.get(new GLIndex(hover.chunkX, hover.chunkY, hover.chunkZ));
			if (chunk != null) {
				GLObject obj = chunk.objects.get(new GLIndex(hover.x, hover.y, hover.z));
				if (obj != null) {
					if (obj.isKnown()) {
						GLDebug.RenderString(0, 60, "Hover Type: " + obj.getType().toString(), 12, Color.white);
					} else {
						GLDebug.RenderString(0, 60, "Hover Type: UNKNOWN", 12, Color.white);
					}
				}
			}
		}

		for (GLMessage message : messages) {

			GLDebug.RenderBackground(-Main.view.getPosition().getX() + message.getPosition().x,
					-Main.view.getPosition().getY() + message.getPosition().y, message.getMessage().length() * 8, 16);
			GLDebug.RenderString(-Main.view.getPosition().getX() + message.getPosition().x,
					-Main.view.getPosition().getY() + message.getPosition().y, message.getMessage(), 12, Color.white);

		}
	}

	public static void showMessage(Vector2f position, String string) {
		GLMessage newMessage = new GLMessage();
		newMessage.setMessage(string);
		newMessage.setPosition(position);
		newMessage.setShowTime(System.currentTimeMillis());
		newMessage.setTimeout(10);
		newMessage.setVisible(true);
		messages.add(newMessage);
	}

	public void destroy() {

	}
}
