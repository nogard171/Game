package classes;

import java.util.LinkedList;

public class BuildingData {
	public String name = "";
	public String model = "";
	public LinkedList<BuildingMaterial> materials = new LinkedList<BuildingMaterial>();
	public LinkedList<BuildingItem> items = new LinkedList<BuildingItem>();
}
