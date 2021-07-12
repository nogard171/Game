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
	long tickSize = 500;
	static boolean ticked = false;

	public static LinkedList<Task> tasks = new LinkedList<Task>();
	public static LinkedList<TaskThread> taskThreads = new LinkedList<TaskThread>();
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
			tickTime = time + tickSize;
			tickCount++;
			ticked = true;
		}
	}

	public ArrayList<Task> startedTasks = new ArrayList<Task>();
	public ArrayList<TaskThread> startedTaskThreads = new ArrayList<TaskThread>();

	public void update() {
		assignTask();

		ArrayList<TaskThread> tempCompletedTaskThreads = new ArrayList<TaskThread>();
		ArrayList<Task> tempCompletedTasks = new ArrayList<Task>();

		for (TaskThread thread : startedTaskThreads) {
			if (thread.task != null) {
				if (thread.task.isComplete) {
					thread.terminate();
					tempCompletedTasks.add(thread.task);
					tempCompletedTaskThreads.add(thread);
				}
			}
		}
		if (tempCompletedTaskThreads.size() > 0) {
			startedTaskThreads.removeAll(tempCompletedTaskThreads);
			tempCompletedTaskThreads.clear();
		}

		if (tempCompletedTasks.size() > 0) {
			startedTasks.removeAll(tempCompletedTasks);
			tempCompletedTasks.clear();
		}
		/*
		 * ArrayList<Task> tempCompletedTasks = new ArrayList<Task>(); for (Task task :
		 * startedTasks) { System.out.println("Move Process: "); if (!task.isSetup) {
		 * setupTask(task); } else if (!task.isComplete) { processTask(task); } if
		 * (task.isComplete) { tempCompletedTasks.add(task); completedTasks.add(task); }
		 * } if (tempCompletedTasks.size() > 0) {
		 * startedTasks.removeAll(tempCompletedTasks); tempCompletedTasks.clear(); }
		 */
	}

	public void assignTask() {
		if (tasks.size() > 0&&ChunkManager.avaliableCharacters.size()>0) {
			Task task = tasks.removeFirst();

			startedTasks.add(task);
			TaskThread thread = new TaskThread(task);
			thread.start();
			taskThreads.add(thread);
			startedTaskThreads.add(thread);
		}
	}

	public static void setupTask(Task task) {

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
					System.out.println("test2:" + task.characteruuid);
					task.isSetup = true;
				}
				break;
			default:
			}
		}
	}

	public static void processTask(Task task) {

		switch (task.taskName) {
		case "MOVE":
			if (task.characterIndex != null) {
				Object obj = ChunkManager.getObjectAt(task.characterIndex.x, task.characterIndex.y,
						task.characterIndex.z);
				if (obj != null) {

					if (task.data.path == null) {
						LinkedList test = APathFinder.find(task.characterIndex, task.data.endIndex);
						task.data.path = test;
						System.out.println("Move Process: " + task.characterIndex + "," + task.data.endIndex);
					} else {
						if (task.data.path.size() > 0) {
							ANode node = (ANode) task.data.path.removeFirst();
							ChunkManager.moveObject(task.characterIndex, node);
							task.characterIndex = node;
						} else {
							task.isComplete = true;
						}
					}
				}
				if (task.isComplete) {
					ChunkManager.avaliableCharacters.add(task.characterIndex);
				}
			}
			break;
		}

	}

	public void destroy() {
		isRunning = false;
		for (TaskThread thread : startedTaskThreads) {
			thread.terminate();
		}
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

	public static boolean hasTicked() {
		// TODO Auto-generated method stub
		return ticked;
	}
}
