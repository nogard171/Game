package core;

import java.util.ArrayList;

public class ResourceData {
	public ArrayList<ResourceItemDrop> itemDrops = new ArrayList<ResourceItemDrop>();

	public void addDrop(ResourceItemDrop resourceItemDrop) {
		itemDrops.add(resourceItemDrop);
	}
}
