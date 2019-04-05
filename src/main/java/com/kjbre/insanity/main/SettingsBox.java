package com.kjbre.insanity.main;

import org.lwjgl.opengl.GL11;

import com.kjbre.insanity.renderer.BitmapString;
import com.kjbre.insanity.renderer.RenderEngine;

public class SettingsBox extends DialogBox{

	private final Button closeButton;
	private final Button uiBigger;
	private final Button uiSmaller;
	private final Button minesBigger;
	private final Button minesSmaller;
	
	public SettingsBox(){
		super();
		closeButton = new Button("Save",0,0);
		closeButton.setX(75 - closeButton.getStaticWidth() -2*Main.UI_SCALE);
		closeButton.setY(80);
		uiSmaller = new Button("Smaller",0,0){
			@Override
			public void click(DialogBox parent){
				if(Main.UI_SCALE > 0.25f) {
					Main.UI_SCALE -= 0.25f;
				} else {
					Main.UI_SCALE = 0.25f;
				}
			}
		};
		uiSmaller.setX(-73);
		uiSmaller.setY(-68);
		uiBigger = new Button("Larger",0,0){
			@Override
			public void click(DialogBox parent){
				Main.UI_SCALE += 0.25f;
			}
		};
		uiBigger.setX(-71+ (uiSmaller.getStaticWidth()));
		uiBigger.setY(-68);
		minesSmaller = new Button("Smaller",0,0){
			@Override
			public void click(DialogBox parent){
				if(Main.GAME_SCALE > 1f) {
					Main.GAME_SCALE -= 0.25f;
				} else {
					Main.GAME_SCALE = 1f;
				}
			}
		};
		minesSmaller.setX(-73);
		minesSmaller.setY(-24);
		minesBigger = new Button("Larger",0,0){
			@Override
			public void click(DialogBox parent){
				Main.GAME_SCALE += 0.25f;
			}
		};
		minesBigger.setX(-71+ (minesSmaller.getStaticWidth()));
		minesBigger.setY(-24);
	}

	@Override
	public void input(int width, int height) {
		float relativeX = Main.mousex - width/2;
		float relativeY = Main.mousey - height/2;
		closeButton.input(relativeX, relativeY, this);
		uiBigger.input(relativeX, relativeY, this);
		uiSmaller.input(relativeX, relativeY, this);
		minesSmaller.input(relativeX, relativeY, this);
		minesBigger.input(relativeX, relativeY, this);
	}

	@Override
	public void render() {
		GL11.glColor4f(0.3f, 0.3f, 0.3f, 1.0f);
		GL11.glBegin(GL11.GL_QUADS);
		RenderEngine.dsVertex2f(-75, -100);
		RenderEngine.dsVertex2f(-75, 100);
		RenderEngine.dsVertex2f(75, 100);
		RenderEngine.dsVertex2f(75, -100);
		GL11.glColor4f(0.1f, 0.1f, 0.1f, 1.0f);
		RenderEngine.dsVertex2f(-75, -100);
		RenderEngine.dsVertex2f(-75, -84);
		RenderEngine.dsVertex2f(75, -84);
		RenderEngine.dsVertex2f(75, -100);
		GL11.glEnd();
		GL11.glColor4f(1f, 1f, 1f, 1.0f);
		BitmapString test = new BitmapString("Settings", RenderEngine.font, 0.5f);
		BitmapString na = new BitmapString("UI Scale: " + Main.UI_SCALE, RenderEngine.font, 0.5f);
		BitmapString gs = new BitmapString("Game Scale: " + Main.GAME_SCALE, RenderEngine.font, 0.5f);
		test.render(-test.getWidth()/2, -100*Main.UI_SCALE);
		na.render(-73 * Main.UI_SCALE, -84*Main.UI_SCALE);
		gs.render(-73 * Main.UI_SCALE, -40*Main.UI_SCALE);
		GL11.glColor4f(1f, 1f, 1f, 1.0f);
		closeButton.render();
		uiBigger.render();
		uiSmaller.render();
		minesSmaller.render();
		minesBigger.render();
	}

}
