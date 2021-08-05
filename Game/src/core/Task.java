package core;

import java.util.LinkedList;

public class Task {
	private TaskType type = TaskType.NONE;
	private ANode currentNode;
	private LinkedList<ANode> path;
	private int ticks;
	private boolean setup = false;
	private LinkedList<Task> followUpTasks = new LinkedList<Task>();

	public Task(TaskType newType, ANode current, LinkedList<ANode> newPath) {
		this(newType, current, newPath, 1000);
	}

	public Task(TaskType newType, ANode current, LinkedList<ANode> newPath, int newTicks) {
		type = newType;
		currentNode = current;
		path = newPath;
		ticks = newTicks;
	}

	public void addFollowUp(Task followUpTask) {
		followUpTasks.add(followUpTask);
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

	public Task getFirstFollowUpTask() {
		return (followUpTasks != null ? (followUpTasks.size() > 0 ? followUpTasks.getFirst() : null) : null);
	}
}
