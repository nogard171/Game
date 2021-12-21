package core;

import java.util.LinkedList;

public class Agent {// extends Thread {
	private Task task;
	private String characterHash;
	private LinkedList<Index> path = new LinkedList<Index>();
	private boolean complete = false;

	public Agent(String newHash) {
		characterHash = newHash;
	}

	public boolean isCompleted() {
		return this.complete;
	}

	// @Override
	public void update() {// run() {
		while (!complete) {
			if (task != null) {
				// check if task is near or not
				// check if task is to find nearest resource
				// check if path is needed
				if (path.size() == 0) {
					Cell charc = World.getCharacterByHash(characterHash);
					if (charc != null) {
						ANode anode = charc.getIndex().toANode();

						LinkedList<ANode> tempPath = APathFinder.find(anode, task.end.toANode());

						System.out.println("Path Found=>" + tempPath);
					}
					// while (path.size() > 0) {

					// }
					complete = true;
				}
			}
		}
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public void clear() {
		task = null;
		characterHash = "";
		path = new LinkedList<Index>();
		complete = false;
	}
}
