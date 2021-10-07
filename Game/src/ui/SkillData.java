package ui;

import java.util.ArrayList;

public class SkillData {
	public String name = "";
	public ArrayList<String> parentSkills;
	public String description;
	public int unlockPoints = 0;

	public SkillData() {
	}
	public SkillData(String newName) {
		this.name = newName;
	}
	public SkillData(String newName, String newDescription) {
		this.name = newName;
		this.description = newDescription;
	}
	public SkillData(String newName, String newDescription,String newParentSkill) {
		this.name = newName;
		this.description = newDescription;
		this.parentSkills.add(newParentSkill);
	}
	public SkillData(String newName, String newDescription,String newParentSkill, int newUnlockPoints) {
		this.name = newName;
		this.description = newDescription;
		this.parentSkills.add(newParentSkill);
		this.unlockPoints = newUnlockPoints;
	}
}
