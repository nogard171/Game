package ui;

import java.awt.Point;
import java.awt.Rectangle;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import utils.Renderer;
import utils.Window;

public class Button {
	public String text = "";
	public Rectangle bounds = new Rectangle(0, 0, 0, 0);
	public boolean hovered = false;
	public AFunction func;
	private int clicked = 0;

	private Color color = new Color(1, 1, 1, 0.5f);

	public Button(String newText, AFunction newFunc) {
		func = newFunc;
		this.text = newText;

	}

	public Button(Rectangle newBounds, String newText, AFunction newFunc) {
		bounds = newBounds;
		func = newFunc;
		this.text = newText;

	}

	public Button() {
	}

	public void click() {
		if (func != null) {
			if (clicked == 0) {
				func.click();
				clicked++;
			}
		}
	}

	public void unclick() {
		clicked = 0;
	}

	public void update() {
		if (this.bounds != null) {
			this.hovered = bounds.contains(new Point(Window.getMouseX(), Window.getMouseY()));
		}
		if (this.hovered) {
			color = new Color(1, 0, 0, 0.5f);
		} else {
			color = new Color(1, 1, 1, 0.5f);
		}
		if (this.hovered && Window.isMainAction()) {
			this.click();
		} else {
			this.unclick();
		}
	}

	public void render() {
		Renderer.renderText(new Vector2f(bounds.x + 7, bounds.y), this.text, 12, Color.white);
		Renderer.renderRectangle(bounds.x, bounds.y, bounds.width, bounds.height, color);
	}
}
