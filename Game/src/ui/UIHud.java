package ui;

import java.awt.Rectangle;
import java.util.LinkedList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import core.Renderer;
import game.PlayerDatabase;

public class UIHud {
	protected UIMiniMap miniMap;
	private Rectangle hudBounds;
	private Rectangle healthBarBounds;
	private Rectangle xpBarBounds;

	private LinkedList<StatusEffect> statusEffects = new LinkedList<StatusEffect>();

	public void setup() {
		setupBounds();
	}

	public void setupBounds() {
		this.hudBounds = new Rectangle(0, 0, 200, 32);
		this.healthBarBounds = new Rectangle(0, 5, 200, 8);
		this.xpBarBounds = new Rectangle(0, 0, 200, 4);
	}

	int buildID = -1;

	public void build() {
		buildID = GL11.glGenLists(1);

		GL11.glNewList(buildID, GL11.GL_COMPILE_AND_EXECUTE);

		Renderer.renderQuad(hudBounds, new Color(0, 0, 0, 0.5f));
		float health = PlayerDatabase.health;
		float maxHealth = PlayerDatabase.maxHealth;
		float healthStep = health / maxHealth;
		Rectangle tempHealthBounds = new Rectangle(healthBarBounds.x, healthBarBounds.y,
				(int) ((float) healthBarBounds.width * (float) healthStep), healthBarBounds.height);
		Rectangle tempDarkHealthBounds = new Rectangle(tempHealthBounds.x + tempHealthBounds.width, healthBarBounds.y,
				(int) ((1 - (float) healthStep) * (float) healthBarBounds.width), healthBarBounds.height);

		Renderer.renderQuad(tempDarkHealthBounds, new Color(0.3f, 0, 0, 0.5f));
		Renderer.renderQuad(tempHealthBounds, new Color(1, 0, 0, 0.5f));
		PlayerDatabase.nextXP = 100;
		PlayerDatabase.xp = 63;
		float xp = PlayerDatabase.xp;
		float nextXP = PlayerDatabase.nextXP;
		float xpStep = xp / nextXP;
		Rectangle tempXpBounds = new Rectangle(xpBarBounds.x, xpBarBounds.y,
				(int) ((float) xpBarBounds.width * (float) xpStep), xpBarBounds.height);
		Rectangle tempDarkXpBounds = new Rectangle(tempXpBounds.x + tempXpBounds.width, xpBarBounds.y,
				(int) ((1 - (float) xpStep) * (float) xpBarBounds.width), xpBarBounds.height);

		Renderer.renderQuad(tempDarkXpBounds, new Color(0.2f, 0.5f, 0.2f, 0.5f));
		Renderer.renderQuad(tempXpBounds, new Color(0.5f, 1, 0.5f, 0.5f));
		int w = Renderer.getTextWidth("Level:" + PlayerDatabase.level, 12);

		Renderer.renderText(new Vector2f(hudBounds.x + hudBounds.width - w, 16), "Level:" + PlayerDatabase.level, 12,
				Color.white);

		Renderer.renderText(new Vector2f(0, 16), PlayerDatabase.name, 12, Color.white);

		GL11.glEndList();
		System.out.println("Building");
	}

	public void update() {
		setupBounds();
		handleHudChanges();
	}

	int tempMaxHealth = 0;
	int tempHealth = 0;
	int tempLevel = 0;
	long tempXP = 0;
	long tempNextXP = 0;
	boolean updateID = false;

	public void handleHudChanges() {
		updateID = (tempMaxHealth != PlayerDatabase.maxHealth ? true : false);
		tempMaxHealth = (tempMaxHealth != PlayerDatabase.maxHealth ? PlayerDatabase.maxHealth : tempMaxHealth);
		updateID = (updateID ? updateID : (tempHealth != PlayerDatabase.health ? true : false));
		tempHealth = (tempHealth != PlayerDatabase.health ? PlayerDatabase.health : tempHealth);
		updateID = (updateID ? updateID : (tempLevel != PlayerDatabase.level ? true : false));
		tempLevel = (tempLevel != PlayerDatabase.level ? PlayerDatabase.level : tempLevel);
		updateID = (updateID ? updateID : (tempXP != PlayerDatabase.xp ? true : false));
		tempXP = (tempXP != PlayerDatabase.xp ? PlayerDatabase.xp : tempXP);
		updateID = (updateID ? updateID : (tempNextXP != PlayerDatabase.nextXP ? true : false));
		tempNextXP = (tempNextXP != PlayerDatabase.nextXP ? PlayerDatabase.nextXP : tempNextXP);
	}

	public void render() {
		PlayerDatabase.health = 50;
		PlayerDatabase.level = 51;

		if (buildID == -1 || updateID) {
			build();
		} else {
			GL11.glCallList(buildID);
		}
	}
}
