package game;

import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import core.ANode;
import core.APathFinder;
import core.Chunk;
import core.ChunkManager;
import core.Entity;
import core.GameDatabase;
import core.GroundItem;
import core.Input;
import core.ItemType;
import core.Renderer;
import core.Resource;
import core.ResourceDatabase;
import core.TaskType;
import core.TextureType;
import core.Tile;
import core.View;
import core.Window;
import ui.Skill;
import ui.SkillData;
import ui.SkillManager;
import ui.SkillName;
import ui.UIInventory;
import ui.UIManager;
import core.Task;
import utils.FPS;
import utils.Ticker;

public class Base {

	boolean isRunning = true;

	public static View view;

	ChunkManager chunkMgr;

	TaskManager taskMgr;
	UIManager uiMgr;

	public void start() {
		this.setup();

		while (this.isRunning) {
			this.update();
			this.render();
		}
		this.destroy();

	}

	public void setup() {
		Window.start();
		Window.setup();

		Input.setup();

		view = new View(0, 0, Window.width, Window.height);

		FPS.setup();

		ResourceDatabase.load();
		GameDatabase.load();

		taskMgr = new TaskManager();
		taskMgr.setup();

		TextureType type = TextureType.GRASS;
		chunkMgr = new ChunkManager();
		chunkMgr.setup();

		uiMgr = new UIManager();
		uiMgr.setup();

	}

	public void update() {
		Window.update();

		taskMgr.update();
		chunkMgr.update();
		uiMgr.update();

		if (Window.close()) {
			this.isRunning = false;
		}

		if (Window.wasResized) {
			view.w = Window.width;
			view.h = Window.height;
			Window.wasResized = false;
		}

		FPS.updateFPS();
		float forceX = 0;
		float forceY = 0;
		if (Input.isKeyDown(Keyboard.KEY_F1)) {
			ResourceDatabase.load();
		}
		moveDown = Input.isMouseDown(2);
		if (offsetPosition == null && moveDown) {
			offsetPosition = new Point((int) (Input.getMousePoint().x + view.x),
					(int) (Input.getMousePoint().y + view.y));
		} else if (!moveDown) {
			offsetPosition = null;
		}
		if (offsetPosition != null) {
			forceX = Input.getMousePoint().x - offsetPosition.x;
			forceY = Input.getMousePoint().y - offsetPosition.y;
			view.x = -forceX;
			view.y = -forceY;
		}
	}

	boolean moveDown = false;
	Point offsetPosition;

	public void render() {
		Window.render();
		Input.poll();

		Renderer.bindTexture(ResourceDatabase.texture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);

		GL11.glPushMatrix();
		GL11.glTranslatef(-view.x, -view.y, 0);
		chunkMgr.render();
		GL11.glPopMatrix();

		// GL11.glBegin(GL11.GL_QUADS);
		// Renderer.renderTexture(TextureType.TREE, 200, 200, 64, 64);
		// GL11.glEnd();

		uiMgr.render();

		Vector2f pos = new Vector2f(Window.width - 350, 0);

		Renderer.renderQuad(new Rectangle((int) pos.x, (int) pos.y, 250, 80), new Color(0, 0, 0, 0.5f));
		Renderer.renderText(new Vector2f(pos.x, pos.y), "FPS: " + FPS.getDelta() + "/" + FPS.getFPS(), 12, Color.white);
		Renderer.renderText(new Vector2f(pos.x, pos.y + 16), "Hover Index: " + UIManager.hoverIndex, 12, Color.white);
		TextureType type = ChunkManager.getTypeByIndexWithTiles(UIManager.hoverIndex);

		if (type != null) {
			Renderer.renderText(new Vector2f(pos.x, pos.y + 32), "Hover Type: " + type.toString(), 12, Color.white);
		}

		Renderer.renderText(new Vector2f(pos.x, pos.y + 48), "Chunks In View: " + chunkMgr.chunksInView.size(), 12,
				Color.white);

		Renderer.renderText(new Vector2f(pos.x, pos.y + 64), "Input Moved: " + "" + Input.moved, 12, Color.white);

	}

	public void destroy() {
		Window.destroy();
	}
}
