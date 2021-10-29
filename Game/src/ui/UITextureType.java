package ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum UITextureType {
	BLANK(0, 0, 0, 0), COINS_ITEM(7, 0, 1, 1), ITEM_BACK(3, 0, 1, 1),ITEM_BACK_SIDE(4, 0, 0.5f, 1), CURSOR(3, 1, 1, 1), ROCK_ITEM(0, 3, 1, 1),
	LOG_ITEM(1, 3, 1, 1), COPPER_ORE_ITEM(0, 5, 1, 1), TIN_ORE_ITEM(1, 5, 1, 1), FISH_ITEM(3, 4, 1, 1), HOE_ITEM(4, 4, 1, 1),

	MOUTH_ICON(5, 2, 1, 1),
	DROP_ICON(5, 3, 1, 1), 
	INSPECT_ICON(6, 3, 1, 1),
	

	INVENTORY_ICON(7, 2, 1, 1),
	SKILL_ICON(6, 1, 1, 1),
	QUESTS_ICON(6, 0, 1, 1),
	CHARACTER_ICON(7, 2, 1, 1),
	SPELLS_ICON(6, 2, 1, 1),
	MAP_ICON(5, 2, 1, 1),
	OPTIONS_ICON(5, 0, 1, 1),

	PANEL_TL(0, 0, 1, 1), PANEL_TC(1, 0, 1, 1), PANEL_TR(2, 0, 1, 1), PANEL_ML(0, 1, 1, 1), PANEL_MC(1, 1, 1, 1),
	PANEL_MR(2, 1, 1, 1), PANEL_BL(0, 2, 1, 1), PANEL_BC(1, 2, 1, 1), PANEL_BR(2, 2, 1, 1);

	public float x;
	public float y;
	public float w;
	public float h;

	public float xOffset;
	public float yOffset;

	public float pW;
	public float pH;

	UITextureType(float newX, float newY) {
		this(newX, newY, 1, 1);
	}

	UITextureType(float newX, float newY, float newW, float newH) {
		this(newX, newY, newW, newH, 0, 0);
	}

	UITextureType(float newX, float newY, float newW, float newH, float newXOffset, float newYOffset) {
		x = newX;
		y = newY;

		w = newW;
		h = newH;

		xOffset = newXOffset;
		yOffset = newYOffset;

		pW = newW;
		pH = newH;
	}

	UITextureType(float newX, float newY, float newW, float newH, float newXOffset, float newYOffset, float newPW,
			float newPH) {
		x = newX;
		y = newY;

		w = newW;
		h = newH;

		xOffset = newXOffset;
		yOffset = newYOffset;

		pW = newPW;
		pH = newPH;
	}

	private String getFriendlyName() {
		String name = "";
		switch (this) {
		case LOG_ITEM:
			name = "Log";
			break;
		case ROCK_ITEM:
			name = "Rock";
			break;
		}
		return name;
	}

	@Override
	public String toString() {
		String data = this.name().replace("_ITEM", "");
		String first = data.substring(0, 1);
		String rest = data.substring(1, data.length());

		return first.toUpperCase() + rest.toLowerCase();
	}
}
