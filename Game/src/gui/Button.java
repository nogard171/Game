package gui;

import java.awt.Dimension;
import java.awt.Rectangle;

import org.newdawn.slick.Color;

import utils.FPS;
import utils.Input;
import utils.Renderer;

public class Button {
	private String name = "";
	private Rectangle bounds;
	private Action action;
	private Color color = new Color(0, 0, 0, 0.5f);
	private Color activeColor = new Color(1, 0, 0, 0.5f);
	private Color defaultColor = new Color(0, 0, 0, 0.5f);
	private int hoverCount = 0;

	public Button(String newName, Rectangle newBounds) {
		name = newName;
		bounds = newBounds;
	}

	public Button(String newName, Rectangle newBounds, Action newClick) {
		name = newName;
		bounds = newBounds;
		action = newClick;
	}

	public void update() {
		if (action != null) {
			if (bounds.contains(Input.getMousePoint())) {
				if (hoverCount == 0) {
					action.in(this);
					action.in();
					hoverCount++;
				}
				if (Input.isMousePressed(0)) {
					action.click(this);
					action.click();
				}
			} else {
				if (hoverCount > 0) {
					action.out(this);
					action.out();
				}
				hoverCount = 0;
			}
		}
	}

	public void render() {
		Renderer.renderQuad(bounds, this.color);
		Renderer.renderText(bounds.x, bounds.y, name, 20, Color.white);
	}

	public void setPosition(int x, int y) {
		bounds.x = x;
		bounds.y = y;
	}

	public Dimension getSize() {
		return new Dimension(bounds.width, bounds.height);
	}

	public Color getColor() {
		return color;
	}

	public void setActive(boolean b) {
		if (b) {
			defaultColor = this.color;
			this.color = activeColor;

		} else {
			this.color = defaultColor;
		}
	}
}
