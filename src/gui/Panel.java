package gui;

import java.awt.Rectangle;

import org.newdawn.slick.Color;

import utils.Renderer;

public class Panel {
	private Rectangle bounds;

	public Panel(Rectangle newBounds) {
		bounds = newBounds;
	}

	public void render() {
		// System.out.println("tesT:" + bounds);
		Renderer.renderQuad(bounds, new Color(0, 0, 0, 0.5f));
	}

	public void setBounds(Rectangle newBounds) {
		bounds = newBounds;
	}

	public Rectangle getBounds() {
		return this.bounds;
	}
}
