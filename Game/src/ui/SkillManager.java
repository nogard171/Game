package ui;

import core.TaskType;
import core.TextureType;
import game.PlayerDatabase;
import utils.LogUtil;

public class SkillManager {

	public static void addExperienceByResource(TextureType type, long amount) {
		addSkillExperince(getSkillByType(type), amount);
	}

	public static void addExperienceByTask(TaskType type, long amount) {
		switch (type) {
		case WALK:
			addSkillExperince(SkillName.AGILITY, amount);
			break;
		}
	}

	public static SkillName getSkillByType(TextureType type) {
		SkillName skill = SkillName.NONE;
		switch (type) {
		case TREE:
			skill = SkillName.WOODCUTTING;
			break;
		case ROCK:
		case COPPER_ORE:
		case TIN_ORE:
			skill = SkillName.MINING;
			break;
		case FISHING_SPOT:
			skill = SkillName.FISHING;
			break;
		default:
			skill = SkillName.AGILITY;
		}
		return skill;
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
					UIChat.addMessage("System:You gained " + amount + " xp in "
							+ skill.skill.toUserString() + ".");
				} else {
					LogUtil.addLog("Max skill xp reached for skill=" + skill.skill.toString());

					UIChat.addMessage("System:You have reached max level for "
							+ skill.skill.toUserString() + ".");
				}

				while (skill.xp > skill.nextXP && skill.level < 99) {

					long nextXp = (long) ((83) * (Math.pow(((skill.level + 1) * 1.1f), 2.4f)));
					skill.xp = skill.xp - skill.nextXP;
					skill.nextXP = nextXp;
					skill.level++;
				
				}
				UISkillWindow.updateID = true;
			}
		}
	}

	public static long getSkillExperince(SkillName skillname) {
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

	public static Skill getSkillByName(SkillName name) {
		Skill skill = null;
		for (Skill s : PlayerDatabase.skills) {
			if (s.skill == name) {
				skill = s;
				break;
			}
		}
		return skill;
	}
}