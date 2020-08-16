package data;

public class CharacterData {
	public static String name = "";
	public static int damage = 10;
	public static int defense = 10;
	public static int agility = 10;
	public static int intellect = 10;
	public static int regen = 10;
	public static int vitality = 10;

	public static int gold = 0;
	public static int silver = 0;
	public static int copper = 0;

	public static void addAttribute(String name, int amount) {
		switch (name) {
		case "damage":
			damage += amount;
			break;
		case "defense":
			defense += amount;
			break;
		case "agility":
			agility += amount;
			break;
		case "intellect":
			intellect += amount;
			break;
		case "regen":
			regen += amount;
			break;
		case "vitality":
			vitality += amount;
			break;
		case "gold":
			gold += amount;
			break;
		case "silver":
			silver += amount;
			break;
		case "copper":
			copper += amount;
			break;
		}
	}
}