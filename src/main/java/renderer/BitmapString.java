package renderer;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

public class BitmapString {
	
	ArrayList<BitmapGlyph> string = new ArrayList<BitmapGlyph>();
	BitmapFont font;
	float scale;
	
	public BitmapString(String string, BitmapFont font, float scale){
		this.scale = scale * ((float) 32/font.getGlyphSize());
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
	
	public void render(float x, float y){
		
		int curx = 0;
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, font.getFontTexture().getID());
		GL11.glBegin(GL11.GL_QUADS);
		for(BitmapGlyph g: string){
			curx += g.getXoffset();
			
			float u = g.getX() / font.getTextureWidth();
			float v = g.getY() / font.getTextureHeight();
			float u2 = (g.getX() + g.getWidth()) / font.getTextureWidth();
			float v2 = (g.getY() + g.getHeight()) / font.getTextureHeight();
			
			GL11.glTexCoord2f(u, v);
		    GL11.glVertex2f(curx * scale, g.getYoffset() * scale);
		    GL11.glTexCoord2f(u2, v);
		    GL11.glVertex2f((curx + g.getWidth())*scale, g.getYoffset()*scale);
		    GL11.glTexCoord2f(u2, v2);
		    GL11.glVertex2f((curx + g.getWidth())*scale,(g.getYoffset() + g.getHeight())*scale);
		    GL11.glTexCoord2f(u,v2);
		    GL11.glVertex2f(curx*scale, (g.getYoffset() + g.getHeight())*scale);
		    curx+= g.getXadvance();
		}
		
		GL11.glEnd();
		GL11.glPopMatrix();
		
	}
	
	public float getWidth(){
		int x = 0;
		
		for(BitmapGlyph s: string){
			x+=s.getXoffset();
			x+=s.getXadvance();
			
			
		}
		
		return x * scale;
	}

}
