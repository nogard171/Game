package ui;

import core.TaskType;
import core.TextureType;
import game.PlayerDatabase;
import utils.LogUtil;

public class SkillManager {

	public static void addExperienceByResource(TextureType type, long amount) {
		switch (type) {
		case TREE:
			addSkillExperince(SkillName.WOODCUTTING, amount);
			break;
		case ROCK:
			addSkillExperince(SkillName.MINING, amount);
			break;
		}
	}

	public static void addExperienceByTask(TaskType type, long amount) {
		switch (type) {
		case WALK:
			addSkillExperince(SkillName.DISCOVERY, amount);
			break;
		}
	}

	public static void addSkillExperince(SkillName skillname, long amount) {

		boolean hasSkill = false;
		Skill skill = null;
		for (Skill s : PlayerDatabase.skills) {
			if (s != null) {
				if (s.skill.equals(skillname)) {
					skill = s;
					hasSkill = true;
				}
			}
		}
		if (hasSkill) {
			if (skill != null) {
				long tempXP = skill.xp;
				if (tempXP < 9223372036854775807l) {
					skill.xp += amount;
				} else {
					LogUtil.addLog("Max skill xp reached for skill=" + skill.skill.toString());
				}

				while (skill.xp > skill.nextXP && skill.level < 99) {

					long nextXp = (long) ((83) * (Math.pow(((skill.level + 1) * 1.1f), 2.4f)));
					skill.xp = skill.xp - skill.nextXP;
					skill.nextXP = nextXp;
					skill.level++;
				}

			}
		}
	}

	public static long getSkillExperince(SkillName skillname) {
		boolean hasSkill = false;
		Skill skill = null;
		long xp = 0;
		for (Skill s : PlayerDatabase.skills) {
			if (s != null) {
				if (s.skill.equals(skillname)) {
					xp = s.xp;
				}
			}
		}
		return xp;
	}
}
