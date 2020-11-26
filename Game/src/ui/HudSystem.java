package ui;

import java.awt.Rectangle;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import utils.Renderer;
import utils.View;
import data.CharacterData;

public class HudSystem extends BaseSystem {

	@Override
	public void setup() {
		super.setup();

		baseBounds = new Rectangle(0, 0, 102, 64);
	}

	@Override
	public void update() {

	}

	@Override
	public void render() {
		super.render();
		Renderer.renderRectangle(baseBounds.x, baseBounds.y, baseBounds.width, baseBounds.height,
				new Color(0, 0, 0, 0.5f));

		Renderer.renderRectangle(baseBounds.x + 102, baseBounds.y, 198, 45, new Color(0, 0, 0, 0.5f));

		Renderer.renderRectangle(baseBounds.x, baseBounds.y, baseBounds.width, baseBounds.height,
				new Color(0, 0, 0, 0.5f));

		Renderer.renderText(new Vector2f(baseBounds.x + 2, baseBounds.y), CharacterData.name, 12, Color.white);

		Renderer.renderRectangle(baseBounds.x + 1, baseBounds.y + 1, 100, 62, new Color(1, 1, 1, 0.5f));

		Renderer.renderRectangle(baseBounds.x + 71, baseBounds.y + 49, 30, 14, new Color(0, 0, 0, 0.5f));

		String level = CharacterData.level + "";
		int xOffset = 0;
		if (level.length() == 2) {
			xOffset = 7;
		} else if (level.length() == 3) {
			xOffset = 14;
		}
		Renderer.renderText(new Vector2f(baseBounds.x + 92 - xOffset, baseBounds.y + 48), level, 12, Color.white);

		int healthWidth = (int) (((float) CharacterData.health / (float) CharacterData.maxHealth) * 197);
		Renderer.renderRectangle(baseBounds.x + 102, baseBounds.y + 1, healthWidth, 12, new Color(1, 0, 0, 0.5f));

		int manaWidth = (int) (((float) CharacterData.mana / (float) CharacterData.maxMana) * 197);
		Renderer.renderRectangle(baseBounds.x + 102, baseBounds.y + 14, manaWidth, 12,
				new Color(0.2f, 0.6f, 1f, 0.75f));

		int staminaWidth = (int) (((float) CharacterData.stamina / (float) CharacterData.maxStamina) * 197);
		Renderer.renderRectangle(baseBounds.x + 102, baseBounds.y + 27, staminaWidth, 12, new Color(1, 1, 0, 0.75f));

		int expWidth = (int) (((float) CharacterData.xp / (float) CharacterData.nextXp) * 197);

		Renderer.renderRectangle(baseBounds.x + 102, baseBounds.y + 40, expWidth, 4, new Color(0, 1, 0, 0.5f));

		int cartX = CharacterData.index.x * 32;
		int cartZ = CharacterData.index.y * 32;

		int isoX = cartX - cartZ;
		int isoZ = (cartX + cartZ) / 2;

		Renderer.renderText(new Vector2f(isoX + View.x - 32, isoZ + View.y - 64), CharacterData.name, 12, Color.white);

		if (BuildingSystem.totalConstruction > 0) {
			Renderer.renderText(new Vector2f(isoX + View.x - 32, isoZ + View.y - 48),
					BuildingSystem.totalConstruction + "%", 12, Color.white);
		}
		if (BuildingSystem.totalDeconstruction > 0) {
			Renderer.renderText(new Vector2f(isoX + View.x - 32, isoZ + View.y - 48),
					BuildingSystem.totalDeconstruction + "%", 12, Color.white);
		}

	}

	@Override
	public void clean() {
		super.clean();

	}
}
