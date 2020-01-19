package game;

import java.awt.Point;

import org.lwjgl.util.vector.Vector2f;

import classes.GLAction;
import classes.GLActionHandler;
import classes.GLIndex;
import classes.GLMenu;
import classes.GLMenuItem;
import classes.GLObject;
import classes.GLTask;
import classes.GLType;
import core.GLChunk;
import core.GLChunkManager;
import core.GLQueueManager;
import core.GLUIManager;

public class GLWorldMenu extends GLMenu {

	@Override
	public void setup() {
		GLMenuItem collectItem = new GLMenuItem("Collect");
		collectItem.click(new GLActionHandler() {
			public void onClick(GLMenuItem obj) {
				GLTask task = new GLTask();
				task.endIndex = index;
				task.action = GLAction.COLLECT;
				GLQueueManager.addTask(task);
				visible = false;
			}
		});
		menuItems.put(collectItem.getValue().toLowerCase(), collectItem);

		GLMenuItem harvestItem = new GLMenuItem("Harvest");
		harvestItem.click(new GLActionHandler() {
			public void onClick(GLMenuItem obj) {
				GLTask task = new GLTask();
				task.endIndex = index;
				task.action = GLAction.HARVEST;
				GLQueueManager.addTask(task);
				visible = false;
			}
		});
		menuItems.put(harvestItem.getValue().toLowerCase(), harvestItem);

		GLMenuItem mineItem = new GLMenuItem("Mine");
		mineItem.click(new GLActionHandler() {
			public void onClick(GLMenuItem obj) {
				GLTask task = new GLTask();
				task.endIndex = index;
				task.action = GLAction.MINE;
				GLQueueManager.addTask(task);
				visible = false;
			}
		});
		menuItems.put(mineItem.getValue().toLowerCase(), mineItem);

		/*
		 * GLMenuItem digItem = new GLMenuItem("Dig"); digItem.click(new
		 * GLActionHandler() { public void onClick(GLMenuItem obj) { GLTask task = new
		 * GLTask(); task.endIndex = index; task.action = GLAction.DIG;
		 * GLQueueManager.addTask(task); visible = false; } });
		 * menuItems.put(digItem.getValue().toLowerCase(), digItem);
		 */

		GLMenuItem moveItem = new GLMenuItem("Move");
		moveItem.click(new GLActionHandler() {
			public void onClick(GLMenuItem obj) {
				GLTask task = new GLTask();
				task.endIndex = index;
				task.action = GLAction.MOVE;
				GLQueueManager.addTask(task);
				visible = false;
			}
		});
		menuItems.put(moveItem.getValue().toLowerCase(), moveItem);

		GLMenuItem chopItem = new GLMenuItem("Chop");
		chopItem.click(new GLActionHandler() {
			public void onClick(GLMenuItem obj) {
				GLTask task = new GLTask();
				task.endIndex = index;
				task.action = GLAction.CHOP;
				GLQueueManager.addTask(task);
				visible = false;
			}
		});
		menuItems.put(chopItem.getValue().toLowerCase(), chopItem);

		GLMenuItem menuItem4 = new GLMenuItem("Inspect");
		menuItem4.click(new GLActionHandler() {
			public void onClick(GLMenuItem obj) {
				if (index != null) {
					GLChunk chunk = GLChunkManager.chunks.get(new GLIndex(index.chunkX, index.chunkY, index.chunkZ));
					if (chunk != null) {
						GLObject object = chunk.objects.get(new GLIndex(index.x, index.y, index.z));
						if (object != null) {
							Point position = new Point((int) ((index.chunkX - index.chunkZ) * (chunk.size.x * 32)),
									(int) (((index.chunkZ + index.chunkX) * (chunk.size.x * 16))
											+ (index.chunkY * (chunk.size.y * 32))));
							int posX = position.x + (index.x - index.z) * 32;
							int posY = position.y + (index.y - 1) * 32;
							int posZ = ((index.z + index.x) * 16) + posY;
							GLUIManager.showMessage(new Vector2f(posX, posZ), "Type: " + object.getType());
							visible = false;
						}
					}
				}
			}
		});
		menuItems.put("inspect", menuItem4);

		GLMenuItem menuItem3 = new GLMenuItem("Close");
		menuItem3.click(new GLActionHandler() {
			public void onClick(GLMenuItem obj) {
				visible = false;
			}
		});
		menuItems.put("close", menuItem3);
		updateBounds(100, 100);
	}

	public void clearMenu() {
		menuItems.get("harvest").setVisible(false);
		menuItems.get("collect").setVisible(false);
		menuItems.get("chop").setVisible(false);
		menuItems.get("mine").setVisible(false);
		menuItems.get("move").setVisible(false);
	}

	public void showHarvestableMenu() {
		menuItems.get("harvest").setVisible(true);
	}

	public void showCollectableMenu() {
		menuItems.get("collect").setVisible(true);

	}

	public void showMineableMenu() {
		menuItems.get("mine").setVisible(true);

	}

	public void showChopableMenu() {
		menuItems.get("chop").setVisible(true);

	}

	public void showMoveableMenu() {
		menuItems.get("move").setVisible(true);

	}

	public void showMenu(GLType type) {
		clearMenu();
		if (type.isHarvestable()) {
			showHarvestableMenu();
		}
		if (type.isCollectable()) {
			showCollectableMenu();
		}
		if (type.isMineable()) {
			showMineableMenu();
		}
		if (type.isChopable()) {
			showChopableMenu();
		}
		if (type.isMoveable()) {
			showMoveableMenu();
		}
		this.visible = true;
	}
}
