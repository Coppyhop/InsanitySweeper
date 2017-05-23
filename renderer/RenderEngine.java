package renderer;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import main.DialogBox;
import main.Main;
import main.Tile;

public class RenderEngine {
	
	
	List<Tile> tiles = new ArrayList<Tile>();
	public static Texture minesweeper, interfaces, icons;
	public static BitmapFont font;
	String version = "0.0.4";
	DialogBox dialog = null;
	public RenderEngine(String skinName){
		
		Loader loader = new Loader();
		
		interfaces = loader.loadTexture("data/skins/" + skinName + "_interface.png");
		minesweeper = loader.loadTexture("data/skins/" + skinName + "_minefield.png");
		icons = loader.loadTexture("data/skins/" + skinName + "_icons.png");
		font = loader.loadFont("data/skins/"+skinName+"_font.fnt");

		
	}
	
	public void render(float width, float height, float offsetx, float offsety, float mapwidth, float mapheight, int fps, int selicon){
			
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
	    GL11.glVertex2f(t.x*16, t.y*16);
	    GL11.glTexCoord2f(1, (0.076923076923f * tid));
	    GL11.glVertex2f(t.x*16+16, t.y*16);
	    GL11.glTexCoord2f(1, 0.076923076923f+ (0.076923076923f * tid));
	    GL11.glVertex2f(t.x*16+16,t.y*16+16);
	    GL11.glTexCoord2f(0, 0.076923076923f + (0.076923076923f * tid));
	    GL11.glVertex2f(t.x*16, t.y*16+16);

	    
		}
		
	    GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
	    GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.8f);
	    GL11.glBegin(GL11.GL_QUADS);
	    GL11.glVertex2f(0, height -48);
	    GL11.glVertex2f(width, height -48);
	    GL11.glVertex2f(width, height);
	    GL11.glVertex2f(0, height);
	    
	    GL11.glVertex2f(width-16, 0);
	    GL11.glVertex2f(width, 0);
	    GL11.glVertex2f(width, height-48);
	    GL11.glVertex2f(width-16, height-48);
	    
	    GL11.glEnd();
	    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	    
	    GL11.glBegin(GL11.GL_LINES);
	    GL11.glVertex2f(0, height -16);
	    GL11.glVertex2f(width-16, height -16);
	    
	    GL11.glVertex2f(width-16, height -48);
	    GL11.glVertex2f(width-16, height);
	    
	    GL11.glEnd();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, interfaces.getID());

		GL11.glBegin(GL11.GL_QUADS);
	    float totalpixelswide = (mapwidth * 16 + 32) - (width-16);
	    float x = (-offsetx/totalpixelswide)*(width-14-16);
	    float texInt = 0.16666666f;
	    float texIndex = 4;
	    if(!Main.hgrabbed){
	    	texIndex = 4;
	    } else {
	    	texIndex = 5;
	    }
	    GL11.glTexCoord2f(0, (texInt * texIndex));
	    GL11.glVertex2f(x, height -16);
	    GL11.glTexCoord2f(0, (texInt * (texIndex +1)));
	    GL11.glVertex2f(x+16, height -16);
	    GL11.glTexCoord2f(1, (texInt * (texIndex +1)));
	    GL11.glVertex2f(x+16, height);
	    GL11.glTexCoord2f(1, (texInt * texIndex));
	    GL11.glVertex2f(x, height);


	    if(!Main.vgrabbed){
	    	texIndex = 4;
	    } else {
	    	texIndex = 5;
	    }
	    float totalpixelshigh = (mapheight * 16 + 32) - height;
	    float y = (-offsety/totalpixelshigh)*(height-15);
	    GL11.glTexCoord2f(0, (texInt * texIndex));
	    GL11.glVertex2f(width-16, y);
	    GL11.glTexCoord2f(0, (texInt * (texIndex+1)));
	    GL11.glVertex2f(width, y);
	    GL11.glTexCoord2f(1, (texInt *(texIndex+1)));
	    GL11.glVertex2f(width, y+16);
	    GL11.glTexCoord2f(1, (texInt * texIndex));
	    GL11.glVertex2f(width-16, y+16);
	    GL11.glEnd();
	    
	    
	    BitmapString test = new BitmapString("FPS: " + fps, font,0.5f);
	    test.render(width - 16 - test.getWidth(), height-48);
	    
	    BitmapString version = new BitmapString("Insanity Sweeper v" + this.version, font, 0.5f);
	    version.render(width - 16 - version.getWidth(), height-32);

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, icons.getID());
		GL11.glBegin(GL11.GL_QUADS);
		
	    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		if(selicon == 0){	    GL11.glColor4f(0.5f, 1.0f, 0.5f, 1.0f);}
		GL11.glTexCoord2f(0, 0.5f);
		GL11.glVertex2f(0, height - 48);
		GL11.glTexCoord2f(0, 0.75f);
		GL11.glVertex2f(0, height-16);
		GL11.glTexCoord2f(1, 0.75f);
		GL11.glVertex2f(32, height-16);
		GL11.glTexCoord2f(1, 0.5f);
		GL11.glVertex2f(32, height -48);
		
	    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		if(selicon == 1){	    GL11.glColor4f(0.5f, 1.0f, 0.5f, 1.0f);}
		GL11.glTexCoord2f(0, 0.75f);
		GL11.glVertex2f(32, height - 48);
		GL11.glTexCoord2f(0, 1f);
		GL11.glVertex2f(32, height-16);
		GL11.glTexCoord2f(1, 1f);
		GL11.glVertex2f(64, height-16);
		GL11.glTexCoord2f(1, 0.75f);
		GL11.glVertex2f(64, height -48);
		
	    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		if(selicon == 2){	    GL11.glColor4f(0.5f, 1.0f, 0.5f, 1.0f);}
		GL11.glTexCoord2f(0, 0.25f);
		GL11.glVertex2f(64, height - 48);
		GL11.glTexCoord2f(0, 0.5f);
		GL11.glVertex2f(64, height-16);
		GL11.glTexCoord2f(1, 0.5f);
		GL11.glVertex2f(96, height-16);
		GL11.glTexCoord2f(1, 0.25f);
		GL11.glVertex2f(96, height -48);
		
	    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		if(selicon == 3){	    GL11.glColor4f(0.5f, 1.0f, 0.5f, 1.0f);}
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(96, height - 48);
		GL11.glTexCoord2f(0, 0.25f);
		GL11.glVertex2f(96, height-16);
		GL11.glTexCoord2f(1, 0.25f);
		GL11.glVertex2f(128, height-16);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2f(128, height -48);
		GL11.glEnd();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		

		
		
		if(dialog != null){
		    GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.65f);
		    GL11.glBegin(GL11.GL_QUADS);
		    GL11.glVertex2f(0, 0);
		    GL11.glVertex2f(0, height);
		    GL11.glVertex2f(width, height);
		    GL11.glVertex2f(width, 0);
		    GL11.glEnd();
		    
		    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			GL11.glPushMatrix();
			GL11.glTranslatef(width/2, height/2, 0);
			dialog.render();
			GL11.glPopMatrix();
		}
		

		dialog = null;
		tiles.clear();
		
	
	}
	
	public void setDialog(DialogBox dialog){
		this.dialog = dialog;
	}
	
	public void processTile(Tile tile){
		tiles.add(tile);
	}

}
