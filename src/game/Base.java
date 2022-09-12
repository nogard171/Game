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

import core.Agent;
import core.AgentManager;
import core.Cell;
import core.Index;
import core.View;
import core.Window;
import core.World;
import gui.HUD;
import gui.Menu;
import gui.Panel;
import gui.Telemetry;
import utils.Debugger;
import utils.FPS;
import utils.Input;
import utils.Renderer;

public class Base {

	boolean isRunning = true;

	public void start() {
		this.setup();

		while (this.isRunning) {
			this.update();
			this.render();
			if (Window.isClose()) {
				this.isRunning = false;
			}
		}
		this.destroy();

	}

	public void setup() {
		Debugger.log("Base Begin Setup");
		Window.start();
		Window.setup();
		Input.setup();
		FPS.setup();

		Data.setup();

		Database.build();

		Database.agentMgr = new AgentManager();
		Database.agentMgr.start();

		Database.world = new World();
		Database.world.setup();

		Database.viewFrame = new View(0, 0, Database.width, Database.height);
		Database.menu = new Menu();
		Database.menu.setup();
		Database.hud = new HUD();
		Database.hud.setup();
		Debugger.log("Base Complete Setup");
	}

	public void update() {

		Window.update();
		// Database.agentMgr.update();
		if (Window.isClose()) {
			this.isRunning = false;
		}
		Database.menu.update();
		if (!Database.menu.isVisible()) {
			Database.mousePosition = new Point(Mouse.getX() + Database.viewFrame.x,
					(Database.height - Mouse.getY()) + Database.viewFrame.y);

			if (Window.wasResized) {
				Database.viewFrame.w = Database.width;
				Database.viewFrame.h = Database.height;
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
			float wheel = Mouse.getDWheel();
			if (wheel > 0&&scale.x<2f) {

				scale.x += 0.1f;
				scale.y += 0.1f;
			}
			else if (wheel < 0&&scale.x>0.2f) {
				System.out.println("Wheel:" + scale.x);
				scale.x -= 0.1f;
				scale.y -= 0.1f;
			}
			Database.viewFrame.move(forceX, forceY);

			Database.world.update();
			Database.hud.update(Database.world);
		}
		if (Database.agentMgr.availableAgents.size() > 0 && currentTask == null) {
			currentTask = Database.agentMgr.availableAgents.removeFirst();
		}
	}

	int keyCount = 0;

	Vector2f scale = new Vector2f(1, 1);
	Agent currentTask;

	public void render() {
		Window.render();
		Input.poll();
		Renderer.bindTexture();
		GL11.glPushMatrix();
		GL11.glTranslatef(-Database.viewFrame.x, -Database.viewFrame.y, 0);

		GL11.glScalef(scale.x, scale.y, 1);

		Database.world.render();
		Database.hud.render(Database.world);

		GL11.glPopMatrix();
		Telemetry.render();

		Database.menu.render();
	}

	public void destroy() {
		Database.agentMgr.active = false;
		Window.destroy();
	}
}
