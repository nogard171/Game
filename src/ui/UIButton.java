package ui;

import java.awt.Rectangle;

import core.Input;

public class UIButton {
	public UITextureType type;
	public String value;
	public Rectangle bounds;
	public boolean clicked = false;
	public boolean hovered = false;

	public UIAction action;

	public UIButton(UITextureType newType, Rectangle newBounds, UIAction newAction) {
		this.type = newType;
		this.bounds = newBounds;
		this.action = newAction;
	}

	public void poll() {
		if (Input.mousePoint != null) {
			this.hovered = this.bounds.contains(Input.mousePoint);
			this.clicked = (this.hovered && Input.isMousePressed(0) ? true : false);

			if (clicked) {
				if (action != null) {
					action.click(this);
				}
			}
		}
	}
}