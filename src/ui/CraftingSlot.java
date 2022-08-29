package ui;

import java.awt.Point;
import java.awt.Rectangle;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import utils.Renderer;
import utils.Window;

public class CraftingSlot {
	private Point slotOffset;
	private Rectangle slotBounds;
	private boolean slotHovered = false;
	public InventoryItem slotItem;

	public CraftingSlot(int x, int y) {
		slotOffset = new Point(x, y);
		slotBounds = new Rectangle(0, 0, 32, 32);
	}

	public void update() {
		slotHovered = slotBounds.contains(new Point(Window.getMouseX(), Window.getMouseY()));
		if (slotHovered && UserInterface.inventory.draggedItem != null && Window.isMainAction()) {
			UserInterface.inventory.moveItemOut(true);
		}
		if (slotHovered && UserInterface.inventory.draggedItem != null && !Window.isMainAction() && slotItem == null) {
			slotItem = UserInterface.inventory.getDraggedItem();
			UserInterface.inventory.moveItemOut(false);
		}
	}

	public void render(int x, int y) {
		slotBounds.x = x + slotOffset.x;
		slotBounds.y = y + slotOffset.y;
		Renderer.renderRectangle(slotBounds.x, slotBounds.y, slotBounds.width, slotBounds.height,
				new Color(1, 1, 1, 0.5f));
		if (slotItem != null) {
			GL11.glBegin(GL11.GL_TRIANGLES);
			Renderer.renderModel(slotBounds.x, slotBounds.y, "SQUARE", slotItem.getMaterial(), new Color(1, 1, 1, 1f));
			GL11.glEnd();
			if (slotItem.count > 1) {
				Renderer.renderText(new Vector2f(slotBounds.x + 24, slotBounds.y + 17), slotItem.count + "", 12,
						Color.white);
			}
		}
	}

	public boolean isHovered() {
		return this.slotHovered;
	}

	public Point getPosition() {
		return new Point(slotBounds.x, slotBounds.y);
	}

	public InventoryItem getItem() {
		// TODO Auto-generated method stub
		return slotItem;
	}
}
