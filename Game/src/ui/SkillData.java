package ui;

import java.util.ArrayList;

public class SkillData {
	public SkillName name = SkillName.NONE;
	public String description;
	public boolean unlocked = false;
	public UITextureType type;

	public SkillData() {
	}

	public SkillData(SkillName newName) {
		this.name = newName;
	}

	public SkillData(SkillName newName, String newDescription) {
		this.name = newName;
		this.description = newDescription;
	}

	public SkillData(SkillName newName, String newDescription, UITextureType newType) {
		this.name = newName;
		this.description = newDescription;
		this.type = newType;
	}
}
