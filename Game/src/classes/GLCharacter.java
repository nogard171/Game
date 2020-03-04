package classes;

import java.util.ArrayList;
import java.util.HashMap;

public class GLCharacter extends GLObject {

	private ArrayList<GLItem> items = new ArrayList<GLItem>();

	public GLCharacter(GLType newType) {
		super(newType);
	}

	public void addItem(GLItem newItem) {
		items.add(newItem);
	}

	public int getItemCount() {
		return items.size();
	}
}
