package gui;

import java.awt.Point;
import java.util.LinkedList;

import org.newdawn.slick.Color;

import core.Cell;
import core.Index;
import core.TextureData;
import core.World;
import game.Database;
import utils.Input;
import utils.Renderer;

public class HUD {
	LinkedList<Index> hoveredBounds;
	ContextMenu context;

	public void setup() {
		context = new ContextMenu();
		context.setup();
	}

	public void update(World world) {
		hoveredBounds = world.getHoveredIndexes(new Point(Input.getMousePoint().x + Database.viewFrame.x,
				Input.getMousePoint().y + Database.viewFrame.y));
		context.update();
		if (Input.isMousePressed(1)) {
			context.position = Input.getMousePointInWorld();
			context.updateBounds();
			context.show(world.getCells(hoveredBounds));
		}
	} 

	public void render(World world) {
		if (hoveredBounds != null) {
			int c = 0;
			for (Index i : hoveredBounds) {
				LinkedList<Cell> cells = world.getCells(i);
				for (Cell cell : cells) {
					if (cell != null) {
						if (c >= (int) ((hoveredBounds.size() + (int) cells.size()) - 2)) {
							TextureData data = Database.textureData.get(cell.getTexture());
							if (data != null) {

							}
							Renderer.renderBounds(cell.getBounds(), Color.red);
						} else {
							Renderer.renderBounds(cell.getBounds(), new Color(0, 0, 0, 0.5f));
						}
						c++;
					}
				}
			}
		}
		context.render();
	}
}
