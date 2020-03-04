package game;

import java.awt.Point;

import org.lwjgl.util.vector.Vector2f;

import classes.GLAction;
import classes.GLActionHandler;
import classes.GLIndex;
import classes.GLMenu;
import classes.GLMenuItem;
import classes.GLObject;
import classes.GLResourceData;
import classes.GLTask;
import classes.GLType;
import core.GLChunk;
import core.GLChunkManager;
import core.GLQueueManager;
import core.GLUIManager;

public class GLWorldMenu extends GLMenu {

	@Override
	public void setup() {
		GLMenuItem harvestItem = new GLMenuItem("Harvest");
		harvestItem.click(new GLActionHandler() {
			public void onClick(GLMenuItem obj) {

				GLChunk chunk = GLChunkManager.chunks
						.get(new GLIndex(0, 0, 0, index.chunkX, index.chunkY, index.chunkZ));

				if (chunk != null) {
					GLTask moveTask = new GLTask();
					boolean objectBesideEmpty = false;
					if (index.x + 1 < chunk.size.x) {
						GLObject northObj = chunk.objects.get(
								new GLIndex(index.x + 1, index.y, index.z, index.chunkX, index.chunkY, index.chunkZ));
						if (northObj.getType() == GLType.AIR) {
							moveTask.endIndex = northObj.getPositionGLIndex();
							objectBesideEmpty = true;
						}
					}

					if (index.x - 1 >= 0 && !objectBesideEmpty) {
						GLObject southObj = chunk.objects.get(
								new GLIndex(index.x - 1, index.y, index.z, index.chunkX, index.chunkY, index.chunkZ));
						if (southObj.getType() == GLType.AIR) {
							moveTask.endIndex = southObj.getPositionGLIndex();
							objectBesideEmpty = true;
						}
					}

					if (index.z - 1 < chunk.size.z && !objectBesideEmpty) {
						GLObject eastObj = chunk.objects.get(
								new GLIndex(index.x, index.y, index.z + 1, index.chunkX, index.chunkY, index.chunkZ));
						if (eastObj.getType() == GLType.AIR) {
							moveTask.endIndex = eastObj.getPositionGLIndex();
							objectBesideEmpty = true;
						}
					}
					if (index.z + 1 >= 0 && !objectBesideEmpty) {
						GLObject westObj = chunk.objects.get(
								new GLIndex(index.x, index.y, index.z - 1, index.chunkX, index.chunkY, index.chunkZ));
						if (westObj.getType() == GLType.AIR) {
							moveTask.endIndex = westObj.getPositionGLIndex();
							objectBesideEmpty = true;
						}
					}
					if (objectBesideEmpty) {
						moveTask.isLead = true;
						moveTask.action = GLAction.MOVE;
						GLQueueManager.addTask(moveTask);
						visible = false;

						GLTask task = new GLTask();
						task.endIndex = index;
						task.leadIndex = moveTask.endIndex;
						task.action = GLAction.HARVEST;
						GLQueueManager.addTask(task);
						visible = false;
					}
				}
			}
		});
		menuItems.put(harvestItem.getValue().toUpperCase(), harvestItem);

		GLMenuItem mineItem = new GLMenuItem("Mine");
		mineItem.click(new GLActionHandler() {
			public void onClick(GLMenuItem obj) {

				GLChunk chunk = GLChunkManager.chunks
						.get(new GLIndex(0, 0, 0, index.chunkX, index.chunkY, index.chunkZ));

				if (chunk != null) {
					GLTask moveTask = new GLTask();
					boolean objectBesideEmpty = false;
					if (index.x + 1 < chunk.size.x) {
						GLObject northObj = chunk.objects.get(
								new GLIndex(index.x + 1, index.y, index.z, index.chunkX, index.chunkY, index.chunkZ));
						if (northObj.getType() == GLType.AIR) {
							moveTask.endIndex = northObj.getPositionGLIndex();
							objectBesideEmpty = true;
						}
					}

					if (index.x - 1 >= 0 && !objectBesideEmpty) {
						GLObject southObj = chunk.objects.get(
								new GLIndex(index.x - 1, index.y, index.z, index.chunkX, index.chunkY, index.chunkZ));
						if (southObj.getType() == GLType.AIR) {
							moveTask.endIndex = southObj.getPositionGLIndex();
							objectBesideEmpty = true;
						}
					}

					if (index.z - 1 < chunk.size.z && !objectBesideEmpty) {
						GLObject eastObj = chunk.objects.get(
								new GLIndex(index.x, index.y, index.z + 1, index.chunkX, index.chunkY, index.chunkZ));
						if (eastObj.getType() == GLType.AIR) {
							moveTask.endIndex = eastObj.getPositionGLIndex();
							objectBesideEmpty = true;
						}
					}
					if (index.z + 1 >= 0 && !objectBesideEmpty) {
						GLObject westObj = chunk.objects.get(
								new GLIndex(index.x, index.y, index.z - 1, index.chunkX, index.chunkY, index.chunkZ));
						if (westObj.getType() == GLType.AIR) {
							moveTask.endIndex = westObj.getPositionGLIndex();
							objectBesideEmpty = true;
						}
					}
					if (objectBesideEmpty) {
						moveTask.isLead = true;
						moveTask.action = GLAction.MOVE;
						GLQueueManager.addTask(moveTask);
						visible = false;

						GLTask task = new GLTask();
						task.endIndex = index;
						task.leadIndex = moveTask.endIndex;
						task.action = GLAction.MINE;
						GLQueueManager.addTask(task);
						visible = false;
					}
				}
			}
		});
		menuItems.put(mineItem.getValue().toUpperCase(), mineItem);

		/*
		 * GLMenuItem digItem = new GLMenuItem("Dig"); digItem.click(new
		 * GLActionHandler() { public void onClick(GLMenuItem obj) { GLTask task = new
		 * GLTask(); task.endIndex = index; task.action = GLAction.DIG;
		 * GLQueueManager.addTask(task); visible = false; } });
		 * menuItems.put(digItem.getValue().toLowerCase(), digItem);
		 */

		/*
		 * GLMenuItem moveItem = new GLMenuItem("Move"); moveItem.click(new
		 * GLActionHandler() { public void onClick(GLMenuItem obj) { GLTask task = new
		 * GLTask(); task.endIndex = index; task.action = GLAction.MOVE;
		 * GLQueueManager.addTask(task); visible = false; } });
		 * menuItems.put(moveItem.getValue().toUpperCase(), moveItem);
		 */

		GLMenuItem chopItem = new GLMenuItem("Chop");
		chopItem.click(new GLActionHandler() {
			public void onClick(GLMenuItem obj) {

				GLChunk chunk = GLChunkManager.chunks
						.get(new GLIndex(0, 0, 0, index.chunkX, index.chunkY, index.chunkZ));

				if (chunk != null) {
					GLTask moveTask = new GLTask();
					boolean objectBesideEmpty = false;
					if (index.x + 1 < chunk.size.x) {
						GLObject northObj = chunk.objects.get(
								new GLIndex(index.x + 1, index.y, index.z, index.chunkX, index.chunkY, index.chunkZ));
						if (northObj.getType() == GLType.AIR) {
							moveTask.endIndex = northObj.getPositionGLIndex();
							objectBesideEmpty = true;
						}
					}

					if (index.x - 1 >= 0 && !objectBesideEmpty) {
						GLObject southObj = chunk.objects.get(
								new GLIndex(index.x - 1, index.y, index.z, index.chunkX, index.chunkY, index.chunkZ));
						if (southObj.getType() == GLType.AIR) {
							moveTask.endIndex = southObj.getPositionGLIndex();
							objectBesideEmpty = true;
						}
					}

					if (index.z - 1 < chunk.size.z && !objectBesideEmpty) {
						GLObject eastObj = chunk.objects.get(
								new GLIndex(index.x, index.y, index.z + 1, index.chunkX, index.chunkY, index.chunkZ));
						if (eastObj.getType() == GLType.AIR) {
							moveTask.endIndex = eastObj.getPositionGLIndex();
							objectBesideEmpty = true;
						}
					}
					if (index.z + 1 >= 0 && !objectBesideEmpty) {
						GLObject westObj = chunk.objects.get(
								new GLIndex(index.x, index.y, index.z - 1, index.chunkX, index.chunkY, index.chunkZ));
						if (westObj.getType() == GLType.AIR) {
							moveTask.endIndex = westObj.getPositionGLIndex();
							objectBesideEmpty = true;
						}
					}
					if (objectBesideEmpty) {
						moveTask.isLead = true;
						moveTask.action = GLAction.MOVE;
						GLQueueManager.addTask(moveTask);
						visible = false;

						GLTask task = new GLTask();
						task.endIndex = index;
						task.leadIndex = moveTask.endIndex;
						task.action = GLAction.CHOP;
						GLQueueManager.addTask(task);
						visible = false;
					}
				}
			}
		});
		menuItems.put(chopItem.getValue().toUpperCase(), chopItem);

		GLMenuItem menuItem4 = new GLMenuItem("Inspect");
		menuItem4.click(new GLActionHandler() {
			public void onClick(GLMenuItem obj) {
				if (index != null) {
					GLChunk chunk = GLChunkManager.chunks.get(new GLIndex(0,0,0,index.chunkX, index.chunkY, index.chunkZ));
					if (chunk != null) {
						System.out.println("tesT");
						GLObject object = chunk.objects.get(new GLIndex(index.x, index.y, index.z,index.chunkX, index.chunkY, index.chunkZ));
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
		menuItems.put("INSPECT", menuItem4);

		GLMenuItem menuItem3 = new GLMenuItem("Close");
		menuItem3.click(new GLActionHandler() {
			public void onClick(GLMenuItem obj) {
				visible = false;
			}
		});
		menuItems.put("CLOSE", menuItem3);
		updateBounds(100, 100);
	}

	public void clearMenu() {
		showMenu("harvest".toUpperCase(), false);
		showMenu("chop".toUpperCase(), false);
		showMenu("mine".toUpperCase(), false);
	}

	public void showMenu(String menuItemName, boolean visible) {
		GLMenuItem menuItem = menuItems.get(menuItemName);
		if (menuItem != null) {
			menuItem.setVisible(visible);
		}
	}

	public void showMenu(GLType type) {
		clearMenu();

		GLResourceData res = Data.resources.get(type.toString().toUpperCase());
		if (res != null) {
			for (String action : res.actions) {
				showMenu(action, true);
			}
		}
		this.visible = true;
	}
}
