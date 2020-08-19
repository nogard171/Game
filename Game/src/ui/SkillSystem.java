package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.RecipeItem;
import data.CharacterData;
import data.UIData;
import utils.Renderer;
import utils.Window;

public class SkillSystem extends BaseSystem {
	public ArrayList<Rectangle> bounds = new ArrayList<Rectangle>();
	private int start = 0;
	private int hover = -1;

	@Override
	public void setup() {
		super.setup();
		baseBounds = new Rectangle(0, 0, 300, 330);
		baseBounds.y = (Window.height - 32) - baseBounds.height;

		for (int i = 0; i < 10; i++) {
			Rectangle bound = new Rectangle(baseBounds.x, baseBounds.y + (i * 33), 148, 33);

			bounds.add(bound);
		}
	}

	@Override
	public void update() {
		super.update();
		if (Window.wasResized()) {
			baseBounds.y = (Window.height - 32) - baseBounds.height;
		}
		if (this.showSystem) {

			if (baseHovered) {
				UserInterface.craftingHovered = true;
				int wheel = Mouse.getDWheel();
				if (wheel != 0) {
					int y = wheel / 120;
					y *= -1;
					if (start + y >= 0 && (start + 10 < CharacterData.obtainedSkills.size() || y < 0)) {
						start += y;
					}
				}
				hover = -1;
				for (int i = 0; i < 10; i++) {
					Rectangle bound = bounds.get(i);
					if (bound.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
						hover = i + start;
					}
				}

				if (hover > -1 && Mouse.isButtonDown(0) && mouseDownCount == 0) {
					System.out.println("Hover: " + hover);
					mouseDownCount++;
				}

				if (!Mouse.isButtonDown(0) && mouseDownCount > 0) {
					mouseDownCount = 0;
				}

			} else {

				UserInterface.craftingHovered = false;
			}
		} else {
			UserInterface.craftingHovered = false;
		}
	}

	int mouseDownCount = 0;

	@Override
	public void render() {
		if (showSystem) {

			Renderer.renderRectangle(baseBounds.x + baseBounds.width - 16, baseBounds.y, 16, 16,
					new Color(1, 0, 0, 0.75f));

			Renderer.renderRectangle(baseBounds.x, baseBounds.y, baseBounds.width, baseBounds.height,
					new Color(0, 0, 0, 0.5f));

			Renderer.renderRectangle(baseBounds.x + 150, baseBounds.y + 1, 8, baseBounds.height - 2,
					new Color(1, 1, 1, 0.5f));
			Renderer.renderRectangle(baseBounds.x + 151,
					baseBounds.y + 2 + ((start * 33) * (((float) 10 / (float) CharacterData.obtainedSkills.size()))), 6,
					(baseBounds.height - 5) * ((float) 10 / (float) CharacterData.obtainedSkills.size()),
					new Color(0.25f, 0.25f, 0.25f, 0.75f));
			if (hover > -1) {
				Renderer.renderRectangle((baseBounds.x + 1), baseBounds.y + 1 + ((hover - start) * 33), 148, 32,
						new Color(1, 0, 0, 0.5f));
			}
			for (int i = start; i < start + 10; i++) {
				if (CharacterData.obtainedSkills.size() > i) {
					String recipeName = CharacterData.obtainedSkills.get(i);

					Renderer.renderText(new Vector2f((baseBounds.x + 33), baseBounds.y + ((i - start) * 33)),
							recipeName, 12, Color.white);
					Renderer.renderRectangle((baseBounds.x + 1), baseBounds.y + 1 + ((i - start) * 33), 148, 32,
							new Color(1, 1, 1, 0.5f));
				}
			}

		}
	}

	@Override
	public void clean() {

	}
}
