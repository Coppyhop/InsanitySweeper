package com.kjbre.insanity.main;

public class Tile {
	
	private boolean covered, flagged;
	private boolean mine, hovered, hoverable;
	private int numMines;
	public int x, y;
	
	public Tile(){
		covered = true;
		hoverable = true;
		mine = false;
		numMines = 0;
	}
	
	public Tile(int x, int y, char mid){
		this.x = x;
		this.y = y;
		if(mid =='0'){
			covered = true;
			mine = false;
		}
		if(mid == '1'){
			covered = true;
			mine = true;
		}
		if(mid == '2'){
			covered = true;
			flagged = true;
			mine = true;
		}
		if(mid == '3'){
			covered = true;
			flagged = true;
			mine = true;
		}
		if(mid == '4'){
			covered = false;
			mine = true;
		}
		if(mid == '5'){
			covered = false;
			mine = false;
		}
		if(!covered){
			hovered = false;
			hoverable = false;
		} else {
			hovered = false;
			hoverable = true;
		}
	}

	public boolean isCovered() {
		return covered;
	}

	public void setCovered(boolean covered) {
		this.covered = covered;
	}

	public boolean isMine() {
		return mine;
	}

	public boolean isHovered() {
		return hovered;
	}

	public void setHovered(boolean hovered) {
		if(hoverable && !flagged){
			this.hovered = hovered;
		}
	}
	
	public void setHoverable(){
		if(hoverable && !flagged){
			this.hoverable = false;
			this.hovered = false;
			this.covered = false;
			if(numMines == 0 && !mine){
				TileMap.needUpdates.add(this);
			}
		}
	}
	
	public void chord(){
		if(TileMap.checkFlags(x, y) == numMines){
			TileMap.murrclick(x, y);
		}
	}

	public void setMine(boolean mine) {
		this.mine = mine;
	}

	public boolean isFlagged(){
		return flagged;
	}
	
	public void toggleFlag(){
		if(covered){
			flagged = !flagged;
		}
	}
	
	public int getNumMines() {
		return numMines;
	}

	public void setNumMines(int numMines) {
		this.numMines = numMines;
	}
	
	public int boolToInt(boolean bool){
		if(bool){
			return 1;
		} else {
			return 0;
		}
	}
	
	private char mid(){
		if(covered && flagged &&!mine){
			return '2';
		}
		if(covered && flagged && mine){
			return '3';
		}
		if(covered && !mine){
			return '0';
		}
		if(covered && mine){
			return '1';
		}
		if(!covered && mine){
			return '4';
		}
		if(!covered && !mine){
			return '5';
		}
		return '9';
	}
	
	public char toSave(){
		return mid();
	}
	
}
