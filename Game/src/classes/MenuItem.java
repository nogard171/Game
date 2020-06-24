package classes;

import java.awt.Rectangle;

public class MenuItem {
	public String text = "";
	public Rectangle bounds = new Rectangle(0, 0, 0, 0);
	public boolean hovered = false;
	public AFunction func;
	private int clicked = 0;

	public MenuItem(AFunction newFunc) {
		func = newFunc;
	}

	public MenuItem() {
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
}
