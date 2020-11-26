package ui;

import java.awt.Point;
import java.awt.Rectangle;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.BuildingData;
import classes.ItemData;
import data.UIData;
import data.WorldData;
import utils.Renderer;
import utils.Window;

public class BuildingListView extends ListView {

	public BuildingListView(int x, int y, int w, int h) {
		super(x, y, w, h);
	}

	@Override
	public void setup() {
		super.setup();
		MenuItem item0 = new MenuItem();
		item0.text = "DECONSTRUCT";
		item0.alwaysVisible = true;
		items.add(item0);

		for (String buildingName : UIData.buildingData.keySet()) {
			BuildingData data = UIData.buildingData.get(buildingName);
			if (data != null) {
				MenuItem item = new MenuItem();
				item.text = buildingName;
				item.alwaysVisible = true;
				items.add(item);
			}
		}
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
		BuildingSystem.building = true;
		BuildingSystem.selectedBuilding = item.text;
	}

	@Override
	public void render(int x, int y) {
		bounds.x = x;
		bounds.y = y;
		Renderer.renderRectangle(bounds.x, bounds.y, bounds.width, bounds.height, new Color(1, 1, 1, 0.5f));
		int itemIndex = 0;
		for (int i = 0; i < 18; i++) {
			if (i < items.size()) {
				MenuItem item = items.get(i);
				if (item != null) {
					if (item.text.equals("DECONSTRUCT")) {
						item.bounds = new Rectangle(bounds.x, bounds.y + (itemIndex * 16) + 2, bounds.width, 16);
						if (item.hovered) {
							Renderer.renderRectangle(item.bounds.x, item.bounds.y, item.bounds.width,
									item.bounds.height, new Color(1, 0, 0, 0.5f));
						}
						Renderer.renderText(new Vector2f(bounds.x + 2, bounds.y + (itemIndex * 16)), item.text, 12,
								Color.white);
						itemIndex++;
					} else {
						BuildingData buildData = UIData.buildingData.get(item.text);
						if (buildData != null) {
							item.bounds = new Rectangle(bounds.x, bounds.y + (itemIndex * 16) + 2, bounds.width, 16);

							if (buildData.materials.size() > 0) {
								if (item.hovered) {
									Renderer.renderRectangle(item.bounds.x, item.bounds.y, item.bounds.width,
											item.bounds.height, new Color(1, 0, 0, 0.5f));
								}
								Renderer.renderText(new Vector2f(bounds.x + 2, bounds.y + (itemIndex * 16)),
										buildData.name, 12, Color.white);
								itemIndex++;
							}
						}
					}
				}
			}
		}
	}
}
