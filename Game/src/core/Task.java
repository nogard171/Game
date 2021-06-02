package core;

import java.util.UUID;

public class Task {
	public UUID uuid;
	public UUID characteruuid;
	public ANode characterIndex;
	public String taskName = "";
	public int taskTicks = 10;
	public boolean isSetup = false;
	public boolean isComplete = false;
	public TaskData data;

	public Task(String name, TaskData newData) {
		uuid = UUID.randomUUID();
		taskName = name;
		data = newData;
	}
}
