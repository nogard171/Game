package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.soap.Text;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.SkillData;
import data.CharacterData;
import data.UIData;
import data.WorldData;
import utils.Renderer;
import utils.Window;

public class SkillSystem extends BaseSystem {
	public ArrayList<Rectangle> bounds = new ArrayList<Rectangle>();
	private int start = 0;
	private int hover = -1;

	@Override
	public void setup() {
		super.setup();
		baseBounds = new Rectangle(0, 0, 308, 333);
		baseBounds.y = (Window.height - 32) - baseBounds.height;

		for (int i = 0; i < 10; i++) {
			Rectangle bound = new Rectangle(baseBounds.x, baseBounds.y + (i * 33), 148, 33);

			bounds.add(bound);
		}
	}

	@Override
	public void update() {
		super.update();
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
						if (CharacterData.obtainedSkills.size() > i + start) {
							hover = i + start;
						}
					}
				}

				if (hover > -1 && Window.isMainAction() && mouseDownCount == 0) {
					System.out.println("Hover: " + hover);
					mouseDownCount++;
				}

				if (!Window.isMainAction() && mouseDownCount > 0) {
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

			Renderer.renderRectangle(baseBounds.x, baseBounds.y, baseBounds.width, baseBounds.height,
					new Color(0, 0, 0, 0.5f));

			for (int x = 0; x < 9; x++) {
				for (int y = 0; y < 10; y++) {
					int i = x + (y * 9);

					Renderer.renderRectangle(baseBounds.x + (x * 33) + 2, baseBounds.y + (y * 33) + 2, 32, 32,
							new Color(1, 1, 1, 0.5f));
					if (CharacterData.obtainedSkills.size()>i) {
						String skillName = CharacterData.obtainedSkills.get(i);
						if (WorldData.skillData.containsKey(skillName)) {
							SkillData data = WorldData.skillData.get(skillName);

							if (data != null) {
								GL11.glBegin(GL11.GL_TRIANGLES);
								Renderer.renderModel(baseBounds.x + (x * 33) + 2, baseBounds.y + (y * 33) + 2, "SQUARE",
										data.material, new Color(1, 1, 1, 1f));
								GL11.glEnd();
							}
						}
					}
					Renderer.renderText(new Vector2f(baseBounds.x + (x * 33) + 2, baseBounds.y + (y * 33) + 2), i + "",
							12, Color.white);
				}
			}

		}
	}

	@Override
	public void clean() {

	}
}
