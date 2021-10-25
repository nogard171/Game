package core;

import java.util.ArrayList;

public class ResourceData {
	public ArrayList<ResourceItemDrop> itemDrops = new ArrayList<ResourceItemDrop>();
	public TextureType[] animationTypes;
	public int rarity = 1;// 1 in 3 chance
	public boolean isRenewable = false;

	public void addDrop(ResourceItemDrop resourceItemDrop) {
		itemDrops.add(resourceItemDrop);
	}
}
