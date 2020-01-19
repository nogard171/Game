package classes;

import java.awt.Point;
import org.lwjgl.util.Rectangle;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import game.Main;

public class GLMenuItem {
	private String value = "test";
	private Rectangle bounds = new Rectangle(0, 0, 32, 12);
	private int index = 0;
	public boolean hovered = false;
	GLActionHandler action;
	private int activated= 0;
	private boolean visible = true;

	public GLMenuItem(String newValue) {
		this.value = newValue;
		bounds.setWidth(100);
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

	public void update(Rectangle ParentBounds) {
		Point mousePoint = new Point(Mouse.getX() + (int) Main.view.getPosition().x,
				Display.getHeight() - Mouse.getY() + (int) Main.view.getPosition().y);
		if (new Rectangle(ParentBounds.getX() + bounds.getX(),
				ParentBounds.getY() + bounds.getY() + (index * 14), bounds.getWidth(), bounds.getHeight())
						.contains(mousePoint.x, mousePoint.y)) {
			hovered=true;
		}
		else
		{
			hovered=false;
		}
		
		if (action != null&&hovered) {
			if (Mouse.isButtonDown(0)&&activated==0) {
				action.onClick(this);
				activated++;
			}
			if (!Mouse.isButtonDown(0)&&activated>0) {
				activated = 0;
			}
		}
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public void setIndex(int i) {
		index = i;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public void setVisible(boolean b) {
		this.visible = b;
	}

	public int getIndex() {
		return this.index;
	}
}
