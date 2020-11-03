package threads;

import java.awt.Point;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import classes.Camera;
import classes.Face;
import utils.Input;
import utils.Renderer;
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
	Face til = new Face(new Vector3f(0, 1, 0), new Vector3f(0, 0, 0));

	@Override
	public void update() {
		super.update();

		Input.poll();
		if (Input.isKeyDown(Keyboard.KEY_A)) {
			System.out.println("pressed");
			til = new Face(new Vector3f(0, -1, 0), new Vector3f(0, 0, 0));
		}
		// Display.setTitle("Mouse: " + Input.getMousePoint());
		cam.acceptInput(1);
		cam.apply();
	}

	@Override
	public void render() {
		super.render();
		Window.setupViewport3D();
		Renderer.renderFace(til);

		Window.setupViewport2D();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor4f(0, 0, 0, 0.5f);
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

}
