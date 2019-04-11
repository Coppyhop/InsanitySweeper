package com.kjbre.insanity.renderer;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.kjbre.insanity.main.Main;
import com.kjbre.insanity.main.Tile;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class RenderEngine {
	
	
	private final List<Tile> tiles = new ArrayList<>();
	public static Texture minesweeper;
	public static Texture interfaces;
	private static Texture icons;
	public static BitmapFont font;
	private final String version = "0.1.1";
	private int width, height;
	
	public RenderEngine(String skinName, int width, int height){
		this.width = width;
		this.height = height;
		Loader loader = new Loader();
		interfaces = loader.loadTexture("data/skins/" + skinName + "_interface.png");
		minesweeper = loader.loadTexture("data/skins/" + skinName + "_minefield.png");
		icons = loader.loadTexture("data/skins/" + skinName + "_icons.png");
		font = loader.loadFont("data/skins/"+skinName+"_font.fnt");
		initOpenGL(width, height);
	}

	private void initOpenGL(int width, int height){
		GL11.glViewport(0, 0, width, height);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(-width/2, width/2, height/2, -height/2, 1, -1);
		GL11.glTranslatef(-width/2, -height/2, 0);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}

	public void prepareRender(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	//Could use some more work but not the focus currently
	public void renderTiles(float offsetx, float offsety){
		GL11.glPushMatrix();
		GL11.glTranslatef(offsetx, offsety, 0);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		setTexture(minesweeper);
		for(Tile t:tiles){
			float tid = t.getNumMines();
			if(t.isMine()) tid = 9;
			if(t.isCovered()) tid = 10;
			if(t.isFlagged()) tid = 12;
			if(t.isHovered()) tid = 11;
			float vScale = 1f/13f;
			drawRectangle(t.x*16,t.y*16,16, 16, 0, vScale*tid, 1,vScale+(vScale*tid), Main.GAME_SCALE);

		}
		GL11.glPopMatrix();
		tiles.clear();
	}

	public void setTexture(Texture texture){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
	}

	public void setTexture(int texture){
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
	}

	public void setColor(float r, float g, float b, float a){
		GL11.glColor4f(r,g,b,a);
	}

	public void drawRectangle(float x, float y, float width, float height, float scale){
		GL11.glBegin(GL_QUADS);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(x*scale, y*scale);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f((x+width)*scale, y*scale);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2f((x+width)*scale,(y+height)*scale);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2f(x*scale, (y+height)*scale);
		GL11.glEnd();
	}

	public void drawRectangle(float x, float y, float width, float height, float u1, float v1, float u2, float v2, float scale){
		GL11.glBegin(GL_QUADS);
		GL11.glTexCoord2f(u1, v1);
		GL11.glVertex2f(x*scale, y*scale);
		GL11.glTexCoord2f(u2, v1);
		GL11.glVertex2f((x+width)*scale, y*scale);
		GL11.glTexCoord2f(u2, v2);
		GL11.glVertex2f((x+width)*scale,(y+height)*scale);
		GL11.glTexCoord2f(u1, v2);
		GL11.glVertex2f(x*scale, (y+height)*scale);
		GL11.glEnd();
	}
	
	public void processTile(Tile tile){
		tiles.add(tile);
	}

}
