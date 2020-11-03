package ui;

import java.awt.Point;
import java.awt.Rectangle;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.ItemData;
import data.UIData;
import data.WorldData;
import utils.Renderer;
import utils.Window;

public class BuildingListView extends ListView {

	public String selectedRecipe = "";

	public BuildingListView(int x, int y, int w, int h) {
		super(x, y, w, h);
	}

	@Override
	public void setup() {
		super.setup();

		MenuItem item1 = new MenuItem();
		item1.text = "WALL_BOTTOM";
		item1.alwaysVisible = true;
		items.add(item1);

	}

	@Override
	public void update() {
		super.update();
		int wheel = Mouse.getDWheel();
		if (wheel > 0) {
			System.out.println("up");
		} else if (wheel < 0) {
			System.out.println("down");
		}
	}

	@Override
	public void handleClick(MenuItem item) {
		selectedRecipe = item.text;
	}

	@Override
	public void render(int x, int y) {
		bounds.x = x;
		bounds.y = y;
		Renderer.renderRectangle(bounds.x, bounds.y, bounds.width, bounds.height, new Color(1, 1, 1, 0.5f));
		int itemIndex = 0;
		// for (MenuItem item : items.values()) {
		for (int i = 0; i < 18; i++) {
			if (i < items.size()) {
				MenuItem item = items.get(i);
				if (item != null) {
					ItemData itemData = UIData.itemData.get(item.text);
					System.out.println("tesT: " + item.text);
					if (itemData != null) {
						item.bounds = new Rectangle(bounds.x, bounds.y + (itemIndex * 16) + 2, bounds.width, 16);
						if (item.hovered) {
							Renderer.renderRectangle(item.bounds.x, item.bounds.y, item.bounds.width,
									item.bounds.height, new Color(1, 0, 0, 0.5f));
						}
						GL11.glBegin(GL11.GL_TRIANGLES);
						Renderer.renderModel(bounds.x, bounds.y + (itemIndex * 16), "SQUARE",
								itemData.inventoryMaterial, new Color(1, 1, 1, 1f));
						GL11.glEnd();
						Renderer.renderText(new Vector2f(bounds.x + 32, bounds.y + (itemIndex * 16)),
								itemData.commonName, 12, Color.white);
						itemIndex++;
					}
				}
			}
		}
	}
}
