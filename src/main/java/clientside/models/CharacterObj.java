package clientside.models;

import java.util.List;


public class CharacterObj{
	
	public int xVel;
	public int yVel; 
	
	public long id;
	
	public List<Bullet> newBullets;
	
	public CharacterObj(){}
	
	public CharacterObj(int xVel, int yVel, long id) {
		
		this.xVel = xVel;
		this.yVel = yVel;
		this.id = id;
	}

}
