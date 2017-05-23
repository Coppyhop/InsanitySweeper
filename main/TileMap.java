package main;

import java.util.Random;

public class TileMap {

	static Tile[][] map;
	private static Tile blankTile;
	int defaultMines;
	Random random = new Random();
	
	public TileMap(int width, int height, int mines){
		map = new Tile[width][height];
		defaultMines = mines;
		generateMap();
	}
	
	public void generateMap(){
		blankTile = new Tile();
		blankTile.setCovered(false);
		int numMines = defaultMines;
		
		for(int x=0; x<map.length;x++){
			for(int y=0; y<map[0].length;y++){
				map[x][y] = new Tile();
				map[x][y].x = x;
				map[x][y].y = y;

			}
		}
		
		while(numMines > 0){

					int x = random.nextInt(map.length);
					int y = random.nextInt(map[0].length);
					if(numMines > 0){
					if(random.nextInt(6) == 1){
						if(!map[x][y].isMine()){
					map[x][y].setMine(true);
					numMines--;
						}
					}
					}


		}
		
		for(int x=0; x<map.length;x++){
			for(int y=0; y<map[0].length;y++){
				
				setMines(x,y);
				
			}
		}
		
	}
	
	
	public static void setMap(Tile[][] newmap){
		map = newmap;
		
		for(int x=0; x<map.length;x++){
			for(int y=0; y<map[0].length;y++){
				
				setMines(x,y);
				
			}
		}
	}
	
	public void setHovered(int x, int y){
		
		for(int xz=x-9; xz<x+9;xz++){
			for(int yz=y-9; yz<y+9;yz++){
				getTile(xz, yz).setHovered(false);
			}
			}
		
		if(x >= 0 && x < map.length && y >= 0 && y < map[0].length){
		map[x][y].setHovered(true);
		}
	}
	
	public static void setMines(int x, int y){
		int mines = 0;
		
		if(x!= 0){
		if(map[x-1][y].isMine()){
			mines++;
		}
		}
		
		if(x!=0 && y!=0){
		if(map[x-1][y-1].isMine()){
			mines++;
		}
		}
		
		if(y!=0){
		if(map[x][y-1].isMine()){
			mines++;
		}
		}
		
		if(x!=(map.length-1) && y!=0){
		if(map[x+1][y-1].isMine()){
			mines++;
		}
		}
		
		if(x!=(map.length-1)){
		if(map[x+1][y].isMine()){
			mines++;
		}
		}
		
		if(x!=(map.length-1) && y!=map[0].length-1){
		if(map[x+1][y+1].isMine()){
			mines++;
		}
		}
		if(y!=map[0].length-1){
		if(map[x][y+1].isMine()){
			mines++;
		}
		}
		if(x!=0 && y!=map[0].length-1){
		if(map[x-1][y+1].isMine()){
			mines++;
		}
		}
		
		map[x][y].setNumMines(mines);
	}
	
	public void click(int x, int y){
		
		if(x>=0 && y>=0 && x<=map.length && y<=map[0].length){
			map[x][y].setHoverable();
		}		
		
	}
	
	public void chord(int x, int y){
		map[x][y].chord();
	}
	
	public void flag(int x, int y){
		map[x][y].toggleFlag();
	}
	
	public static Tile getTile(int x, int y){
		if(x<=-1 || y<=-1 || x>=map.length || y>=map[0].length){
			return blankTile;
		} else {
			return map[x][y];
		}
	}
	
	public static int checkFlags(int x, int y){
		int flags = 0;

		if(getTile(x-1,y).isFlagged()) flags++;
		if(getTile(x-1,y-1).isFlagged()) flags++;
		if(getTile(x,y-1).isFlagged()) flags++;
		if(getTile(x+1,y-1).isFlagged()) flags++;
		if(getTile(x+1,y).isFlagged()) flags++;
		if(getTile(x+1,y+1).isFlagged()) flags++;
		if(getTile(x,y+1).isFlagged()) flags++;
		if(getTile(x-1,y+1).isFlagged()) flags++;
		
		if(getTile(x-1,y).isMine() && getTile(x-1,y).isCovered() == false) flags++;
		if(getTile(x-1,y-1).isMine() && getTile(x-1,y-1).isCovered() == false) flags++;
		if(getTile(x,y-1).isMine() && getTile(x,y-1).isCovered() == false) flags++;
		if(getTile(x+1,y-1).isMine() && getTile(x+1,y-1).isCovered() == false) flags++;
		if(getTile(x+1,y).isMine() && getTile(x+1,y).isCovered() == false) flags++;
		if(getTile(x+1,y+1).isMine() && getTile(x+1,y+1).isCovered() == false) flags++;
		if(getTile(x,y+1).isMine() && getTile(x,y+1).isCovered() == false) flags++;
		if(getTile(x-1,y+1).isMine() && getTile(x-1,y+1).isCovered() == false) flags++;
		
		return flags;
	}
	
	public static void murrclick(int x, int y){

				getTile(x-1,y).setHoverable();
				getTile(x-1,y-1).setHoverable();
				getTile(x,y-1).setHoverable();
				getTile(x+1,y-1).setHoverable();
				getTile(x+1,y).setHoverable();
				getTile(x+1,y+1).setHoverable();
				getTile(x,y+1).setHoverable();
				getTile(x-1,y+1).setHoverable();
		
}
	
	
	public Tile[][] getMap(){
		return map;
	}
	
}
