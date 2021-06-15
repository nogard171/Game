package core;

import java.awt.Rectangle;

public class UIMenuItem {
	public String value = "";
	public UIAction onClickAction;
	public Rectangle bounds;
	public boolean hovered = false;
	public boolean isVisible = true;
	public boolean defaultVisibility = true;

	public UIMenuItem(String newValue) {
		this.value = newValue;
	}

	public UIMenuItem(String newValue, boolean newVisible) {
		this.value = newValue;
		this.isVisible = newVisible;
		this.defaultVisibility = newVisible;
	}

	public void update() {
		if (bounds != null) {
			if (bounds.contains(Input.getMousePoint()) && Input.isMousePressed(0)) {

			}
		}
	}

	public void onClick(UIMenu menu) {
		if (onClickAction != null) {
			onClickAction.onClick(menu, this);
		}
	}
}
