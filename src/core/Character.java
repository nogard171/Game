package core;

import java.util.UUID;

public class Character extends Cell {
	// inventory items

	public Character() {
	}

	public Character(Index newIndex, String newTexture) {
		this.setIndex(newIndex);
		this.setTexture(newTexture);
	}

}
