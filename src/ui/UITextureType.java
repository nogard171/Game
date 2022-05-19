package ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum UITextureType {
	BLANK(0, 0, 0, 0), 
	COINS_ITEM(224, 0,32,32), 
	ITEM_BACK(96, 0,32,32),
	ITEM_BACK_SIDE(128, 0, 16,32), 
	CURSOR(96,32,32,32), 
	ROCK_ITEM(0,96,32,32),
	LOG_ITEM(32,96,32,32), 
	COPPER_ORE_ITEM(0, 160,32,32), 
	TIN_ORE_ITEM(32, 160,32,32), 
	FISH_ITEM(96,128,32,32), 
	HOE_ITEM(128,128,32,32),
	STICK_ITEM(96,96,32,32),

	MOUTH_ICON(160,64,32,32),
	DROP_ICON(160,96,32,32), 
	INSPECT_ICON(192,96,32,32),
	CURSOR_INVALID(224,128,32,32), 
	CURSOR_MINING(128,64,32,32),
	CURSOR_WOODCUTTING(96,64,32,32),
	CURSOR_TILLING(128,32,32,32),
	

	INVENTORY_ICON(224,64,32,32),
	SKILL_ICON(192,32,32,32),
	QUESTS_ICON(192, 0,32,32),
	CHARACTER_ICON(224,64,32,32),
	SPELLS_ICON(192,64,32,32),
	MAP_ICON(160,64,32,32),
	OPTIONS_ICON(160, 0,32,32),
	CHAT_ICON(224,96,32,32),
	

	MINING_SKILL_ICON(192,128,32,32),
	WOODCUTTING_SKILL_ICON(160,128,32,32),

	PANEL_TL(0, 0,32,32), 
	PANEL_TC(32, 0,32,32), 
	PANEL_TR(64, 0,32,32), 
	PANEL_ML(0,32,32,32), 
	PANEL_MC(32,32,32,32),
	PANEL_MR(64,32,32,32), 
	PANEL_BL(0,64,32,32), 
	PANEL_BC(32,64,32,32), 
	PANEL_BR(64,64,32,32);

	public float x;
	public float y;
	public float w;
	public float h;

	public float xOffset;
	public float yOffset;

	public float pW;
	public float pH;

	UITextureType(float newX, float newY) {
		this(newX, newY, 1,1);
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
