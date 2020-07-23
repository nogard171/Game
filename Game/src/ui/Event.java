package ui;

import java.awt.Point;
import java.util.List;

public class Event {
	public boolean processed = false;
	public boolean setup = false;
	public Point end;
	public String eventName;
	public List path;
	public int step = 0;
	public int stepTime = 100;
	public long startTime = 0;

	public boolean childNeedsProcessed= false;
	public Event followUpEvent;
	public boolean completedFollowUp = false;
}
