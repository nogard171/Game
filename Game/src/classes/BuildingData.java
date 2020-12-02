package classes;

import java.util.LinkedList;

public class BuildingData {
	public String name = "";
	public String model = "";
	public String type = "BUILDING";
	public LinkedList<BuildingMaterial> materials = new LinkedList<BuildingMaterial>();
	public LinkedList<BuildingItem> items = new LinkedList<BuildingItem>();
}
