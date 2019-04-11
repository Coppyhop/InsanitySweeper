package com.kjbre.insanity.renderer;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import com.kjbre.insanity.main.Main;

public class BitmapString {
	
	private final ArrayList<BitmapGlyph> string = new ArrayList<>();
	private final BitmapFont font;

	public BitmapString(String string, BitmapFont font){
		char[] chars = string.toCharArray();
		this.font = font;
		for(char c: chars){
			for(BitmapGlyph g: font.getGlyphs() ){
				if(g.getCharRep() == c){
					this.string.add(g);
				}
			}
			
		}
	}
	
	public ArrayList<BitmapGlyph> getString(){
		return string;
	}
	
	public void render(float x, float y, RenderEngine renderer){
		int curx = 0;
		GL11.glPushMatrix();
		GL11.glTranslatef(x*Main.UI_SCALE, y*Main.UI_SCALE, 0);
		renderer.setTexture(font.getFontTexture());
		for(BitmapGlyph g: string){
			curx += g.getXoffset();
			float u = g.getX() / font.getTextureWidth();
			float v = g.getY() / font.getTextureHeight();
			float u2 = (g.getX() + g.getWidth()) / font.getTextureWidth();
			float v2 = (g.getY() + g.getHeight()) / font.getTextureHeight();
		    renderer.drawRectangle(curx, g.getYoffset(), g.getWidth(), g.getHeight(), u, v, u2, v2, Main.UI_SCALE);
		    curx+= g.getXadvance();
		}
		GL11.glPopMatrix();
	}

	public float getWidth(){
		int x = 0;
		for(BitmapGlyph s: string){
			x+=s.getXoffset();
			x+=s.getXadvance();
		}
		return x;
	}


	public float getHeight(){
		return font.getGlyphSize();
	}

}
