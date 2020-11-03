package classes;

public class GroundItem extends Object {
	public int count = 0;

	public GroundItem() {
		isItem = true;
		this.setModel("TILE");
		type = ObjectType.ITEM;
	}
}
