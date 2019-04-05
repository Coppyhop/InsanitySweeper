package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import org.lwjgl.opengl.GL11;
import renderer.BitmapString;
import renderer.RenderEngine;

public class OpenBox extends DialogBox{

	Button closeButton, openButton;
	ScrollButton upButton, downButton;
	File[] saves;
	public static SingleTextSelectionElement[] files;
	int offset = 0;
	
	public OpenBox(){
		super();
		closeButton = new Button("Close",0,0);
		closeButton.setX(-73);
		closeButton.setY(80);
		openButton = new Button("Open",0,0){
			@Override
			public void click(DialogBox parent){
				boolean onesel = false;
				File saveGame = null;
				for(int i=0;i<files.length;i++){
					if(files[i].selected){
						onesel = true;
						saveGame = saves[i];
					}
				}
				if(onesel){
					Tile[][] map = new Tile[3000][1600];
					try (final BufferedReader br =
								 new BufferedReader(new InputStreamReader(
								 		new FileInputStream("data/saves/" + saveGame.getName())))) {
						String line;
						while ((line = br.readLine()) != null) {
						 	if (line.isEmpty()) { continue; }
						 		String[] tile = line.split(" ");
						 		int x = Integer.valueOf(tile[0]);
						 		int y = Integer.valueOf(tile[1]);
						 		int mid = Integer.valueOf(tile[2]);
						 		map[x][y] = new Tile(x, y, mid);
						}
						br.close();
						TileMap.setMap(map);
					} catch(Exception e){
						e.printStackTrace();
					}
					parent.isSelected =false;
					Main.dialog = false;
				}
			}
		};
		openButton.setX(73 - openButton.getStaticWidth());
		openButton.setY(80);
		upButton = new ScrollButton(57,-82,-1);
		downButton = new ScrollButton(57,(78-16),1);
	}
	
	public void update(){
		offset = 0;
		File folder = new File("data/saves/");
		saves = folder.listFiles();
		files = new SingleTextSelectionElement[saves.length];
		for (int i = 0; i < saves.length; i++) {
			if (saves[i].isFile()) {
		        files[i] = new SingleTextSelectionElement(-73*Main.UI_SCALE,
						-82*Main.UI_SCALE + (16*i*Main.UI_SCALE),128*Main.UI_SCALE,
						new BitmapString(saves[i].getName(), RenderEngine.font, 0.5f));
			}
		}
	}
	
	@Override
	public void input(int width, int height) {
		float relativeX = Main.mousex - width/2;
		float relativeY = Main.mousey - height/2;
		closeButton.input(relativeX, relativeY, this);
		openButton.input(relativeX, relativeY, this);
		upButton.input(relativeX, relativeY,this);
		downButton.input(relativeX, relativeY,this);
		for(int i=offset;i<files.length;i++){
			if(i < offset + 10){
				files[i].input(relativeX, relativeY,offset);
			}
		}
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
		GL11.glColor4f(0.2f, 0.2f, 0.2f, 1.0f);
		RenderEngine.dsVertex2f(-73, -82);
		RenderEngine.dsVertex2f(-73, 78);
		RenderEngine.dsVertex2f(55, 78);
		RenderEngine.dsVertex2f(55, -82);
		GL11.glColor4f(0.2f, 0.2f, 0.2f, 1.0f);
		RenderEngine.dsVertex2f(57, -82);
		RenderEngine.dsVertex2f(57, 78);
		RenderEngine.dsVertex2f(57+16, 78);
		RenderEngine.dsVertex2f(57+16, -82);
		GL11.glEnd();
		GL11.glColor4f(1f, 1f, 1f, 1.0f);
		for(int i=offset;i<files.length;i++){
			if(i < offset + 10){
				files[i].render(offset);
			}
		}
		GL11.glColor4f(1f, 1f, 1f, 1.0f);
		BitmapString test = new BitmapString("Open Game", RenderEngine.font, 0.5f);
		test.render(-test.getWidth()/2, -100*Main.UI_SCALE);
		GL11.glColor4f(1f, 1f, 1f, 1.0f);
		closeButton.render();
		upButton.render();
		downButton.render();
		openButton.render();
	}

}
