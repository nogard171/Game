package threads;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

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
	}

	@Override
	public void update() {
		super.update();

		Input.poll();
		if (Input.isKeyDown(Keyboard.KEY_A)) {
			System.out.println("pressed");
		}
		Display.setTitle("Mouse: " + Input.getMousePoint());
	}

	int d = 0;

	@Override
	public void render() {
		super.render();
		// set the color of the quad (R,G,B,A)
		GL11.glColor3f(0.5f, 0.5f, 1.0f);

		// draw quad
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3f(100, 100, d);
		GL11.glVertex3f(100 + 200, 100, d);
		GL11.glVertex3f(100 + 200, 100 + 200, d);
		GL11.glVertex3f(100, 100 + 200, d);
		GL11.glEnd();
	}

	@Override
	public void clean() {
		super.clean();
	}
}
