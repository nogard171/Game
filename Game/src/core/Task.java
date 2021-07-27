package core;

import java.util.LinkedList;

public class Task {
	private TaskType type = TaskType.NONE;
	private ANode currentNode;
	private LinkedList<ANode> path;
	private int ticks;
	private boolean setup = false;

	public Task(TaskType newType, ANode current, LinkedList<ANode> newPath, int newTicks) {
		type = newType;
		currentNode = current;
		path = newPath;
		ticks = newTicks;
	}

	public TaskType getType() {
		return type;
	}

	public ANode getCurrentNode() {
		return currentNode;
	}

	public int getTicks() {
		return ticks;
	}

	public LinkedList<ANode> getPath() {
		return path;
	}

	public boolean isSetup() {
		return setup;
	}

	public void setup() {
		setup = true;
	}
}
