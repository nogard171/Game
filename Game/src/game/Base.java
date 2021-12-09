package game;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;

import core.Cell;
import core.Index;
import core.View;
import core.Window;
import core.World;
import gui.Menu;
import gui.Panel;
import gui.Telemetry;
import utils.FPS;
import utils.Input;
import utils.Renderer;

public class Base {

	boolean isRunning = true;

	Menu menu;

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
		FPS.setup();

		Data.setup();

		Database.build();

		world = new World();
		world.setup();

		Database.viewFrame = new View(0, 0, Window.width, Window.height);
		menu = new Menu();
		menu.setup();
	}

	LinkedList<Index> hoveredBounds;

	public void update() {
		Window.update();

		if (Window.isClose()) {
			this.isRunning = false;
		}
		menu.update();
		if (!menu.isVisible()) {
			Database.mousePosition = new Point(Mouse.getX() + Database.viewFrame.x,
					(Window.height - Mouse.getY()) + Database.viewFrame.y);
			hoveredBounds = world.getHoveredIndexes(new Point(Input.getMousePoint().x + Database.viewFrame.x,
					Input.getMousePoint().y + Database.viewFrame.y));

			if (Window.wasResized) {
				Database.viewFrame.w = Window.width;
				Database.viewFrame.h = Window.height;
				Window.wasResized = false;
			}

			FPS.updateFPS();

			float speed = FPS.getDelta();
			int forceX = 0;
			int forceY = 0;
			if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
				forceX = (int) -speed;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
				forceX = (int) speed;

			}
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				forceY = (int) -speed;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				forceY = (int) speed;
			}
			Database.viewFrame.move(forceX, forceY);

			world.update();
		}
	}

	World world;

	public void render() {
		Window.render();
		Input.poll();
		Renderer.bindTexture();
		GL11.glPushMatrix();
		GL11.glTranslatef(-Database.viewFrame.x, -Database.viewFrame.y, 0);

		world.render();
		/*
		 * if (hoveredBounds.size() > 0) { Index i = hoveredBounds.getLast(); float
		 * localX = (i.x - i.z) * 33; float localY = i.y * 33; float localZ = ((i.z +
		 * i.x) * 17) + localY;
		 * 
		 * Renderer.renderBounds(localX, localZ);
		 * 
		 * }
		 */
		if (hoveredBounds != null) {
			int c = 0;
			for (Index i : hoveredBounds) {
				float localX = (i.x - i.z) * 32;
				float localY = i.y * 32;
				float localZ = ((i.z + i.x) * 16) + localY;
				Cell cell = world.getCell(i);
				if (cell != null) {
					if (c >= hoveredBounds.size() - 1) {
						Renderer.renderBounds(cell.getBounds(), Color.red);
					} else {
						Renderer.renderBounds(cell.getBounds(), new Color(0, 0, 0, 0.5f));
					}
					c++;
				}
			}
		}
		GL11.glPopMatrix();
		Telemetry.render();

		menu.render();
	}

	public void destroy() {
		Window.destroy();
	}
}
