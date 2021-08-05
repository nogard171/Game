package game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import core.ANode;
import core.ChunkManager;
import core.GameDatabase;
import core.ItemType;
import core.Resource;
import core.ResourceData;
import core.ResourceItemDrop;
import core.Task;
import core.TaskType;
import core.TextureType;
import ui.Inventory;
import utils.Ticker;

public class TaskManager {
	private static LinkedList<Task> tasks = new LinkedList<Task>();
	private static LinkedList<Task> startedTasks = new LinkedList<Task>();

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
				if (task.getType() == TaskType.RESOURCE) {
					ANode node = path.removeFirst();
					if (path.size() == 0) {

						Resource res = ChunkManager.getResource(node.toPoint());
						if (res != null) {
							ResourceData dat = GameDatabase.resources.get(res.getType());
							if (dat != null) {
								for (ResourceItemDrop drop : dat.itemDrops) {
									ArrayList<ItemType> types = drop.getDroppedItems();
									for (ItemType type : types) {
										Inventory.addItem(type);
									}
								}
							}
						}

						ChunkManager.setObjectAtIndex(node.toPoint(), TextureType.AIR);
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
		if (Ticker.ticked()) {
			tickedUpdate();
		}
	}

	public void destroy() {

	}
}
