package classes;

import java.util.List;

public class GLTask {
	public GLIndex startIndex;
	public GLIndex currentIndex;
	public GLIndex endIndex;
	public GLIndex leadIndex;
	public GLAction action;
	public GLCharacter character;
	
	public List path;
	public int step;
	public boolean complete = false;
	public boolean started = false;
	public boolean isLead= false;
}
