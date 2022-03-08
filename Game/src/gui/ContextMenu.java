package gui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;

import org.lwjgl.input.Keyboard;

import core.ANode;
import core.AgentManager;
import core.Cell;
import core.Index;
import core.Task;
import core.TaskType;
import core.Window;
import game.Database;
import utils.Input;

public class ContextMenu {
	public boolean show = false;
	public Point position;
	private Panel backgroundPanel;
	private LinkedList<Button> menuBtns = new LinkedList<Button>();
	int width = 150;
	LinkedList<Cell> hoveredCells = new LinkedList<Cell>();

	public void setup() {
		position = new Point(0, 0);
		backgroundPanel = new Panel(new Rectangle(0, 0, width, 10));

	}

	public void addButton(String type) {
		if (type == "tree") {
			menuBtns.add(new Button(
					"Chop " + type.substring(0, 1).toUpperCase() + "" + type.substring(1, type.length()).toLowerCase(),
					new Rectangle(300, 300, width, 25), "tree", new Action() {
						@Override
						public void click() {
							Task task = new Task();
							task.type = TaskType.CHOP;
							Cell cell = hoveredCells.getLast();
							task.end = cell.getIndex();
							AgentManager.addTask(task);
						}

						@Override
						public void in(Button self) {
							self.setActive(true);
						}

						@Override
						public void out(Button self) {
							self.setActive(false);
						}
					}));
		}
		if (type == "grass") {
			menuBtns.add(new Button("Move", new Rectangle(300, 300, width, 25), "move", new Action() {
				@Override
				public void click() {
					Task task = new Task();
					task.type = TaskType.MOVE;
					Cell cell = hoveredCells.getLast();
					task.end = cell.getIndex();/*new Index(
							(int) ((cell.getRegionIndex().x * Database.regionSize.getWidth()) + cell.getIndex().x),
							cell.getIndex().y,
							(int) ((cell.getRegionIndex().z * Database.regionSize.getHeight()) + cell.getIndex().z));*/

					System.out.println("Search Time:" +task.end );
					AgentManager.addTask(task);
				}

				@Override
				public void in(Button self) {
					self.setActive(true);
				}

				@Override
				public void out(Button self) {
					self.setActive(false);
				}
			}));
		}
		/*
		 * menuBtns.add(new Button("Mine", new Rectangle(300, 300, width, 25), "rock",
		 * new Action() {
		 * 
		 * @Override public void click() {
		 * 
		 * }
		 * 
		 * @Override public void in(Button self) { self.setActive(true); }
		 * 
		 * @Override public void out(Button self) { self.setActive(false); } }));
		 * menuBtns.add(new Button("Harvest", new Rectangle(300, 300, width, 25),
		 * "plant", new Action() {
		 * 
		 * @Override public void click() {
		 * 
		 * }
		 * 
		 * @Override public void in(Button self) { self.setActive(true); }
		 * 
		 * @Override public void out(Button self) { self.setActive(false); } }));
		 * menuBtns.add(new Button("Search", new Rectangle(300, 300, width, 25),
		 * "container", new Action() {
		 * 
		 * @Override public void click() {
		 * 
		 * }
		 * 
		 * @Override public void in(Button self) { self.setActive(true); }
		 * 
		 * @Override public void out(Button self) { self.setActive(false); } }));
		 */

	}

	public void updateBounds() {
		int y = 0;
		for (Button b : menuBtns) {
			b.setPosition(position.x, position.y + (28 * y));
			y++;
		}
		backgroundPanel.setBounds(new Rectangle(position.x, position.y, backgroundPanel.getBounds().width, y * 28));
	}

	public void update() {
		if (Window.wasResized) {
			updateBounds();
		}
		if (show) {
			for (Button b : menuBtns) {
				b.update(Input.getMousePointInWorld());
			}

			if (!backgroundPanel.getBounds().contains(Input.getMousePointInWorld())) {
				show = false;
				hoveredCell = null;
			}
		}
	}

	Cell hoveredCell;

	public void show(LinkedList<Cell> cells) {
		menuBtns.clear();
		hoveredCells = cells;
		int y = 0;
		for (Cell c : hoveredCells) {
			// for (Button b : menuBtns) {
			if (c != null) {
				// if (b.getRenderForTexture() == c.getTexture() || b.getRenderForTexture() ==
				// "") {
				// b.setPosition(position.x, position.y + (28 * y));
				addButton(c.getTexture());
				y++;
				// }
			} else {
				// y++;
			}

			// }
		}
		y += 2;

		boolean hasInspect = false;
		boolean hasCancel = false;
		for (Button btn : menuBtns) {
			if (btn.getName() == "Inspect") {
				hasInspect = true;
			}
			if (btn.getName() == "Cancel") {
				hasCancel = true;
			}
		}
		if (!hasInspect) {
			menuBtns.add(new Button("Inspect", new Rectangle(300, 300, width, 25), new Action() {
				@Override
				public void click() {

				}

				@Override
				public void in(Button self) {
					self.setActive(true);
				}

				@Override
				public void out(Button self) {
					self.setActive(false);
				}
			}));
		}
		if (!hasCancel) {
			menuBtns.add(new Button("Cancel", new Rectangle(0, 0, width, 25), new Action() {
				@Override
				public void click() {
					show = false;
				}

				@Override
				public void in(Button self) {
					self.setActive(true);
				}

				@Override
				public void out(Button self) {
					self.setActive(false);
				}
			}));
		}

		updateBounds();
		backgroundPanel.setBounds(new Rectangle(position.x, position.y, backgroundPanel.getBounds().width, y * 28));
		show = true;
		System.out.println("Test" + y);
	}

	public void render() {
		if (show) {
			backgroundPanel.render();
			for (Button b : menuBtns) {
				if (hoveredCell != null) {
					if (b.getRenderForTexture() == hoveredCell.getTexture() || b.getRenderForTexture() == "") {
						b.render(false);
					}
				} else {
					b.render(false);
				}
			}
		}
	}
}
