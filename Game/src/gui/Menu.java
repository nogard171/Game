package gui;

import java.awt.Rectangle;

import core.Window;

public class Menu {
	private Panel backgroundPanel;

	public void setup() {

		backgroundPanel = new Panel(new Rectangle(0, 0, Window.width, Window.height));
	}

	public void update() {
		if (Window.wasResized) {
			backgroundPanel.setBounds(new Rectangle(0, 0, Window.width, Window.height));
		}
	}

	public void render() {
		// backgroundPanel.render();
	}
}
