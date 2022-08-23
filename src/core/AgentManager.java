package core;

import java.util.Iterator;
import java.util.LinkedList;

public class AgentManager extends Thread {

	public boolean active = true;
	private static LinkedList<Task> queuedTasks = new LinkedList<Task>();
	public static LinkedList<Agent> availableAgents = new LinkedList<Agent>();
	private static LinkedList<Agent> activeAgents = new LinkedList<Agent>();

	public void setup() {

	}

	public void addAgent(String hash) {
		Agent agent = new Agent(hash);
		// agent.start();
		availableAgents.add(agent);
	}

	public static void addTask(Task newTask) {

		System.out.println("Task for:" + newTask.end);
		queuedTasks.add(newTask);
	}

	@Override
	public void run() {
		setup();
		while (active) {
			update();
		}
	}

	public void update() {
		if (Window.isClose()) {
			System.out.println("Closed:");
		}
		LinkedList<Task> assignedTasks = new LinkedList<Task>();
		if (queuedTasks.size() > 0) {
			for (Task task : queuedTasks) {
				if (availableAgents.size() > 0) {
					Agent agent = availableAgents.removeFirst();
					agent.setTask(task);
					activeAgents.add(agent);
					assignedTasks.add(task);
				}
			}
			queuedTasks.removeIf(t -> t.type != TaskType.NONE);
		}

		LinkedList<Agent> completedAgents = new LinkedList<Agent>();
		for (Agent agent : activeAgents) {
			agent.update();
			if (agent.isCompleted()) {
				agent.clear();
				completedAgents.add(agent);
			}
		}

		activeAgents.removeAll(completedAgents);
		availableAgents.addAll(completedAgents);
	}

	public static int queuedSize() {
		return  queuedTasks.size();
	}

	public static int agentSize() {
		return availableAgents.size();
	}

	public static int activeAgentSize() {
		return activeAgents.size();
	}
}
