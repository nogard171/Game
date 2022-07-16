package ui;

import java.util.ArrayList;
import java.util.HashMap;

import core.TextureType;

public class SkillData {
	public SkillName name = SkillName.NONE;
	public String description;
	public boolean unlocked = false;
	public UITextureType type;
	public HashMap<TextureType, Integer> resourceLevels = new HashMap<TextureType, Integer>();
	public String requiredItem;

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

	public SkillData(SkillName newName, String newDescription, UITextureType newType, String newRequiredItem) {
		this.name = newName;
		this.description = newDescription;
		this.type = newType;
		this.requiredItem = newRequiredItem;
	}
}
