package classes;

import java.awt.Point;
import java.awt.Rectangle;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import game.Main;

public class GLMenuItem {
	private String value = "test";
	private Rectangle bounds = new Rectangle(0, 0, 32, 32);
	GLActionHandler action;

	public GLMenuItem(String newValue) {
		this.value = newValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void click(GLActionHandler newAction) {
		action = newAction;
	}

	public void update() {
		if (action != null) {
			if (Mouse.isButtonDown(0)) {
				Point mousePoint = new Point(Mouse.getX() + (int) Main.view.getPosition().x,
						Display.getHeight() - Mouse.getY() + (int) Main.view.getPosition().y);
				if (this.bounds.contains(mousePoint)) {
					action.onClick(this);
				}
			}

		}
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
}
