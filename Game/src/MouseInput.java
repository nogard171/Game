import org.lwjgl.input.Mouse;
import org.lwjgl.util.Point;

public class MouseInput {
	Point mousePosition = new Point(0, 0);
	boolean[] mouseButtons = {};

	public MouseInput() {
		mouseButtons = new boolean[java.awt.MouseInfo.getNumberOfButtons()];
	}

	public void poll(int displayHeight) {
		for (int i = 0; i < java.awt.MouseInfo.getNumberOfButtons(); i++) {
			mouseButtons[i] = Mouse.isButtonDown(i);
		}
		mousePosition = new Point(Mouse.getX(),  Mouse.getY());
	}

	public Point getPosition() {
		// TODO Auto-generated method stub
		return mousePosition;
	}

	public boolean mouseDown(int i) {
		// TODO Auto-generated method stub
		return mouseButtons[i];
	}
}
