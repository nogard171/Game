package threads;

import java.awt.Point;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import classes.Camera;
import utils.Input;
import utils.Window;

public class GameThread extends BaseThread {
	@Override
	public void run() {
		super.run();
	}

	@Override
	public void setup() {
		super.setup();
		cam = new Camera();
		cam.create();

	}

	Camera cam;

	@Override
	public void update() {
		super.update();

		Input.poll();
		if (Input.isKeyDown(Keyboard.KEY_A)) {
			System.out.println("pressed");
		}
		// Display.setTitle("Mouse: " + Input.getMousePoint());
		cam.acceptInput(1);
		cam.apply();
	}


	@Override
	public void render() {
		super.render();
		Window.setupViewport3D();
		renderCube(0, 0, 0);
		
		Window.setupViewport2D();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(0,0,0,0.5f);
		GL11.glVertex2i(0, 0);
		GL11.glVertex2i(100, 0);
		GL11.glVertex2i(100, 100);
		GL11.glVertex2i(0, 100);
		GL11.glEnd();
	}

	@Override
	public void clean() {
		super.clean();
	}

	public void renderCube(float x, float y, float z) {

		float size = 1f;

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(0.5f, 0.75f, 0.5f);
		// top
		GL11.glVertex3f(x + size, y , z);
		GL11.glVertex3f(x, y , z);
		GL11.glVertex3f(x, y , z + size);
		GL11.glVertex3f(x + size, y , z + size);
		float differnce = -0.0005f;
		GL11.glColor3f(0.5f, 0.2f, 0f);
		// bottom
		GL11.glVertex3f(x + size, y+differnce, z + size);
		GL11.glVertex3f(x, y+differnce, z + size);
		GL11.glVertex3f(x, y+differnce, z);
		GL11.glVertex3f(x + size, y+differnce, z);

		GL11.glEnd();
	}
}
