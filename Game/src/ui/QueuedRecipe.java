package ui;

public class QueuedRecipe {
	public String hash = "";
	public String recipe = "";
	public int time = 0;

	public QueuedRecipe(String newHash, String newRecipe) {
		hash = newHash;
		recipe = newRecipe;
	}
}
