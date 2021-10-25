package ui;

import java.util.ArrayList;

public class SkillData {
	public String name = "";
	public String description;
	public boolean unlocked = false;
	public UITextureType type;

	public SkillData() {
	}

	public SkillData(String newName) {
		this.name = newName;
	}

	public SkillData(String newName, String newDescription) {
		this.name = newName;
		this.description = newDescription;
	}

	public SkillData(String newName, String newDescription, UITextureType newType) {
		this.name = newName;
		this.description = newDescription;
		this.type = newType;
	}
}
