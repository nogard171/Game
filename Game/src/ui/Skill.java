package ui;

public class Skill {
	public SkillName skill=SkillName.NONE;
	public int level =1;
	//possible future issue, max xp value is 9,223,372,036,854,775,807
	public long xp = 0;
	public long nextXP = 83;
}
