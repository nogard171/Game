package game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import core.ANode;
import core.ChunkManager;
import core.GameDatabase;
import core.GroundItem;
import core.ItemType;
import core.Resource;
import core.ResourceData;
import core.ResourceItemDrop;
import core.Task;
import core.TaskType;
import core.TextureType;
import core.Tile;
import ui.Inventory;
import utils.Ticker;

public class TaskManager {
	private static LinkedList<Task> tasks = new LinkedList<Task>();
	private static LinkedList<Task> startedTasks = new LinkedList<Task>();
	Ticker tickerUtil;

	public static void addTask(Task newTask) {
		tasks.add(newTask);
	}

	public static Point getCurrentTaskEnd() {
		Point endIndex = null;
		if (startedTasks.size() > 0) {
			Task task = startedTasks.getLast();
			if (task != null) {
				LinkedList<ANode> path = task.getPath();
				if (path != null) {
					if (path.size() > 0) {
						ANode node = path.getLast();
						if (node != null) {
							endIndex = node.toPoint();
						}
					}
				}
			}
		} else {
			endIndex = ChunkManager.getIndexByType(TextureType.CHARACTER);
		}
		return endIndex;
	}

	public int getTaskCount() {
		return startedTasks.size();
	}

	public void setup() {
		tickerUtil = new Ticker();
	}

	public void tickedUpdate() {
		if (tasks.size() > 0) {
			Task task = tasks.removeFirst();

			setupTask(task);
		}
		if (startedTasks.size() > 0) {
			Task task = startedTasks.getFirst();

			boolean done = processTask(task);
			if (done) {
				Task followUpTask = task.getFirstFollowUpTask();
				if (followUpTask != null) {
					boolean followUpDone = processTask(followUpTask);

					if (followUpDone) {
						startedTasks.removeFirst();
					}

				} else {
					startedTasks.removeFirst();
				}
			}
		}
	}

	private boolean setupTask(Task task) {
		boolean isSetup = false;
		if (!task.isSetup()) {
			startedTasks.add(task);
			task.setup();
		}
		return isSetup;
	}

	Random r = new Random();

	private boolean processTask(Task task) {
		boolean complete = false;
		LinkedList<ANode> path = task.getPath();
		if (path != null) {

			if (path.size() > 0) {
				if (task.getType() == TaskType.WALK) {
					ANode node = path.removeFirst();
					Point playerIndex = ChunkManager.getIndexByType(TextureType.CHARACTER);
					ChunkManager.setObjectAtIndex(playerIndex, TextureType.AIR);
					ChunkManager.move(playerIndex, node.toPoint());
				}
				if (task.getType() == TaskType.TILL) {
					ANode node = path.removeFirst();
					if (path.size() == 0) {
						System.out.println("tilling land");
						Tile tile = ChunkManager.getTile(node.toPoint());
						if(tile!=null)
						{
							ChunkManager.setTileAtIndex(node.toPoint(), TextureType.TILLED_DIRT);
						}
						/*
						Resource res = ChunkManager.getResource();
						if (res != null) {
							ResourceData dat = GameDatabase.resources.get(res.getBaseType());
							if (dat != null) {
								int genR = r.nextInt(dat.rarity - 1 + 1) + 1;
								System.out.println("genR: " + genR);
								if (genR == 1) {
									for (ResourceItemDrop drop : dat.itemDrops) {
										ArrayList<ItemType> types = drop.getDroppedItems();
										for (ItemType type : types) {
											Inventory.addItem(type);
										}
									}
									if (!dat.isRenewable) {
										ChunkManager.setObjectAtIndex(node.toPoint(), TextureType.AIR);
									}
								} else {
									for (int t = 0; t < 10; t++) {
										path.add(node);
									}
								}
							}
						}*/

					}
				} else if (task.getType() == TaskType.RESOURCE) {
					ANode node = path.removeFirst();
					if (path.size() == 0) {
						Resource res = ChunkManager.getResource(node.toPoint());
						if (res != null) {
							ResourceData dat = GameDatabase.resources.get(res.getBaseType());
							System.out.println("test");
							if (dat != null) {
								int genR = r.nextInt(dat.rarity - 1 + 1) + 1;
								System.out.println("genR: " + genR);
								if (genR == 1) {
									for (ResourceItemDrop drop : dat.itemDrops) {
										ArrayList<ItemType> types = drop.getDroppedItems();
										for (ItemType type : types) {
											Inventory.addItem(type);
										}
									}
									if (!dat.isRenewable) {
										ChunkManager.setObjectAtIndex(node.toPoint(), TextureType.AIR);
									}
									else
									{
										//generate new renewable resources in near by location,
										//fishing spots should generate on the end of the water
										//so it can be reached.
										
										//rocks can generate outside of view or add generating
										// infinite cave on rock/ore break
										// or generate on game load.
									}
								} else {
									for (int t = 0; t < 10; t++) {
										path.add(node);
									}
								}
							}
						}

					}
				} else if (task.getType() == TaskType.ITEM) {
					ANode node = path.removeFirst();
					if (path.size() == 0) {

						GroundItem tile = ChunkManager.getItem(node.toPoint());
						if (tile != null) {
							if (tile instanceof GroundItem) {
								boolean pickedUp = Inventory.addItem(tile.item);
								if (pickedUp) {
									ChunkManager.setItemAtIndex(node.toPoint(), TextureType.AIR);
								}
							}
						}

					}

				}
			}
			if (path.size() <= 0) {
				complete = true;
			}
		}
		return complete;
	}

	public void update() {
		tickerUtil.poll(100);
		if (tickerUtil.ticked()) {
			tickedUpdate();
		}
	}

	public void destroy() {

	}
}
