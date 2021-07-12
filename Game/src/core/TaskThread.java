package core;

public class TaskThread extends Thread {
	public Task task;

	int tickCount = 0;
	long time = 0;
	long tickTime = 0;
	long tickSize = 500;
	static boolean ticked = false;
	private boolean isRunning = true;

	public TaskThread(Task newTask) {
		task = newTask;
	}

	public boolean taskComplete() {
		return task.isComplete;
	}

	public void terminate() {
		isRunning = false;
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

	@Override
	public void run() {
		TaskManager.setupTask(task);
		while (isRunning) {
			tickCheck();
			if (ticked) {
				System.out.println("test");
				TaskManager.processTask(task);
			}
		}
	}
}
