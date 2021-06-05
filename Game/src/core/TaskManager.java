package core;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class TaskManager extends Thread {
	boolean isRunning = true;
	int tickCount = 0;
	long time = 0;
	long tickTime = 0;
	boolean ticked = false;

	public static LinkedList<Task> tasks = new LinkedList<Task>();
	public static ArrayList<Task> completedTasks = new ArrayList<Task>();

	public static void addTask(Task task) {
		tasks.add(task);
	}

	@Override
	public void run() {
		while (isRunning) {
			tickCheck();
			if (ticked) {
				update();
			}
		}
	}

	public void tickCheck() {
		ticked = false;
		time = System.currentTimeMillis();
		if (time > tickTime) {
			tickTime = time + 1000;
			tickCount++;
			ticked = true;
		}
	}

	public ArrayList<Task> startedTasks = new ArrayList<Task>();

	public void update() {
		assignTask();
		ArrayList<Task> tempCompletedTasks = new ArrayList<Task>();
		for (Task task : startedTasks) {
			if (!task.isSetup) {
				setupTask(task);
			} else if (!task.isComplete) {
				processTask(task);
			}
			if (task.isComplete) {
				tempCompletedTasks.add(task);
				completedTasks.add(task);
			}
		}
		if (tempCompletedTasks.size() > 0) {
			startedTasks.removeAll(tempCompletedTasks);
			tempCompletedTasks.clear();
		}
	}

	public void assignTask() {
		if (tasks.size() > 0) {
			Task task = tasks.removeFirst();

			startedTasks.add(task);
		}
	}

	public void setupTask(Task task) {
		ANode characterIndex = null;
		if (ChunkManager.avaliableCharacters.size() > 0) {
			characterIndex = ChunkManager.avaliableCharacters.removeFirst();
		}
		if (characterIndex != null) {
			switch (task.taskName) {
			case "MOVE":
				Object obj = ChunkManager.getObjectAt(characterIndex.x, characterIndex.y, characterIndex.z);
				if (obj != null) {
					task.characterIndex = characterIndex;
					task.characteruuid = obj.uuid;
					task.isSetup = true;
				}
				break;
			default:
			}
		}
	}

	public void processTask(Task task) {
		switch (task.taskName) {
		case "MOVE":
			if (task.characterIndex != null) {
				Object obj = ChunkManager.getObjectByUUID(task.characteruuid);
				if (obj != null) {
					System.out.println("Move Process: " + obj.getSprite());

					List test = APathFinder.find(task.characterIndex, task.data.endIndex);
					task.data.path = test;

				}
				ChunkManager.avaliableCharacters.add(task.characterIndex);
				task.isComplete = true;
			}
			break;
		}

	}

	public void destroy() {
		isRunning = false;
	}

	public static Task removeTaskByUUID(UUID test) {
		Task task = null;
		for (Task tempTask : completedTasks) {
			if (tempTask.uuid.equals(test)) {
				task = tempTask;
				break;
			}
		}
		completedTasks.remove(task);
		return task;
	}

	public static Task removeCompletedTask() {
		Task task = null;
		for (Task tempTask : completedTasks) {
			if (tempTask.isComplete) {
				task = tempTask;
				break;
			}
		}
		completedTasks.remove(task);
		return task;
	}
}
