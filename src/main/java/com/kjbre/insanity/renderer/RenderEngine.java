package com.kjbre.insanity.renderer;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.kjbre.insanity.main.DialogBox;
import com.kjbre.insanity.main.Main;
import com.kjbre.insanity.main.Tile;

import static org.lwjgl.opengl.GL11.glClearColor;

public class RenderEngine {
	
	
	private final List<Tile> tiles = new ArrayList<>();
	private static Texture minesweeper;
	public static Texture interfaces;
	private static Texture icons;
	public static BitmapFont font;
	private final String version = "0.1.1";
	private DialogBox dialog = null;
	
	public RenderEngine(String skinName, int width, int height){
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
	
	public void renderTiles(float offsetx, float offsety){
		GL11.glPushMatrix();
		GL11.glTranslatef(offsetx, offsety, 0);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, minesweeper.getID());
		GL11.glBegin(GL11.GL_QUADS);
		for(Tile t:tiles){
			float tid = t.getNumMines();
			if(t.isMine()) tid = 9;
			if(t.isCovered()) tid = 10;
			if(t.isFlagged()) tid = 12;
			if(t.isHovered()) tid = 11;
			GL11.glTexCoord2f(0, (0.076923076923f * tid));
			GL11.glVertex2f(t.x*16*Main.GAME_SCALE, t.y*16*Main.GAME_SCALE);
			GL11.glTexCoord2f(1, (0.076923076923f * tid));
			GL11.glVertex2f(t.x*16*Main.GAME_SCALE+16*Main.GAME_SCALE, t.y*16*Main.GAME_SCALE);
			GL11.glTexCoord2f(1, 0.076923076923f+ (0.076923076923f * tid));
			GL11.glVertex2f(t.x*16*Main.GAME_SCALE+16*Main.GAME_SCALE,t.y*16*Main.GAME_SCALE+16*Main.GAME_SCALE);
			GL11.glTexCoord2f(0, 0.076923076923f + (0.076923076923f * tid));
			GL11.glVertex2f(t.x*16*Main.GAME_SCALE, t.y*16*Main.GAME_SCALE+16*Main.GAME_SCALE);
		}
		GL11.glEnd();
		GL11.glPopMatrix();
		tiles.clear();
	}

	public static void dsVertex2f(float x, float y){
		GL11.glVertex2f(x * Main.UI_SCALE, y * Main.UI_SCALE);
	}

	
	public void setDialog(DialogBox dialog){
		this.dialog = dialog;
	}
	
	public void processTile(Tile tile){
		tiles.add(tile);
	}

}
