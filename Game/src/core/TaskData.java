package core;

import java.util.LinkedList;
import java.util.List;

public class TaskData {
	public ANode endIndex;
	public LinkedList path;

	public TaskData(ANode end) {
		endIndex = end;
	}
}
