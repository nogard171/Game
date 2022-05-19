package ui;

public enum SkillName {
	NONE, WOODCUTTING, MINING, FARMING, FISHING, AGILITY, ATTACK, DEFENSE, MAGIC, CRAFTING, BUILDING;

	public String toUserString() {
		String temp = this.toString();

		return temp.substring(0, 1) + temp.substring(1, temp.length() ).toLowerCase();
	}
}
