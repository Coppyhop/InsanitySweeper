package com.kjbre.insanity.main;

import org.lwjgl.opengl.GL11;
import com.kjbre.insanity.renderer.BitmapString;
import com.kjbre.insanity.renderer.RenderEngine;

public class NewBox extends DialogBox{

	private final Button yesButton;
	private final Button noButton;
	
	public NewBox(){
		super();
		yesButton = new Button("Yes",0,18*Main.UI_SCALE){
			@Override
			public void click(DialogBox parent){
				Main.map.generateMap();
				parent.isSelected =false;
				Main.dialog = false;
			}
		};
		noButton = new Button("No",-73*Main.UI_SCALE,18*Main.UI_SCALE);
		noButton.setX(-113);
		noButton.setY(16);
		yesButton.setY(16);
		yesButton.setX(115 - yesButton.getStaticWidth() -2);
	}
	
	@Override
	public void input(int width, int height) {
		float relativeX = Main.mousex - width/2;
		float relativeY = Main.mousey - height/2;
		yesButton.input(relativeX, relativeY, this);
		noButton.input(relativeX, relativeY, this);
	}

	@Override
	public void render() {
		GL11.glColor4f(0.3f, 0.3f, 0.3f, 1.0f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(-115*Main.UI_SCALE, -36*Main.UI_SCALE);
		GL11.glVertex2f(-115*Main.UI_SCALE, 36*Main.UI_SCALE);
		GL11.glVertex2f(115*Main.UI_SCALE, 36*Main.UI_SCALE);
		GL11.glVertex2f(115*Main.UI_SCALE, -36*Main.UI_SCALE);
		GL11.glColor4f(0.1f, 0.1f, 0.1f, 1.0f);
		GL11.glVertex2f(-115*Main.UI_SCALE, -36*Main.UI_SCALE);
		GL11.glVertex2f(-115*Main.UI_SCALE, -20*Main.UI_SCALE);
		GL11.glVertex2f(115*Main.UI_SCALE, -20*Main.UI_SCALE);
		GL11.glVertex2f(115*Main.UI_SCALE, -36*Main.UI_SCALE);
		GL11.glEnd();
		BitmapString test = new BitmapString("New Game", RenderEngine.font, 0.5f);
		BitmapString message = new BitmapString("This will clear any unsaved progress.", RenderEngine.font,
				0.5f);
		BitmapString message2 = new BitmapString("Do you wish to continue?", RenderEngine.font, 0.5f);
		GL11.glColor4f(1f, 1f, 1f, 1.0f);
		test.render(-test.getWidth()/2, -36*Main.UI_SCALE);
		message.render(-113*Main.UI_SCALE, -20*Main.UI_SCALE);
		message2.render(-113*Main.UI_SCALE, -4*Main.UI_SCALE);
		GL11.glColor4f(1f, 1f, 1f, 1.0f);
		yesButton.render();
		noButton.render();
	}

}
