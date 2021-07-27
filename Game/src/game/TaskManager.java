package game;

import java.awt.Point;
import java.util.LinkedList;

import core.ANode;
import core.ChunkManager;
import core.Task;
import core.TextureType;
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
					endIndex = path.getLast().toPoint();
				}
			}
		} else {
			endIndex = ChunkManager.getIndexByType(TextureType.CHARACTER);
		}
		return endIndex;
	}
	
	public int getTaskCount()
	{
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
				startedTasks.removeFirst();
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
				ANode node = path.removeFirst();
				Point playerIndex = ChunkManager.getIndexByType(TextureType.CHARACTER);
				ChunkManager.setObjectAtIndex(playerIndex, TextureType.AIR);
				ChunkManager.move(playerIndex, node.toPoint());
			}
			if (path.size() <= 0) {

				System.out.println("Ticks to Complete: " + task.getTicks());
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
