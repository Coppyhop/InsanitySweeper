package com.kjbre.insanity.main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import com.kjbre.insanity.gui.GuiRenderer;
import com.kjbre.insanity.renderer.Loader;
import com.kjbre.insanity.renderer.WindowManager;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL11;

import com.kjbre.insanity.renderer.RenderEngine;

public class Main {

	private float viewx = 0;
	private float viewy = 0;
	private float smoothx = 0;
	private float smoothy =0;
	private static int width=1600;
	private static int height=900;
	private static final int mapheight = 30;
	private static final int mapwidth = 60;
	private static final int nummines = 10;
	private float delta;
	private float lastFrame;
	private float lastFPS;
	private float FPS;
	public static float UI_SCALE = 2f, GAME_SCALE = 2f;
	private int fps = 0;
	private int selicon = -1;
	static float mousex =0, mousey =0;
	public static boolean dialog = false;
	private RenderEngine renderer;
	public static String input="";
	public static long window;
	static TileMap map;
	private static boolean refreshed = false;
	private static boolean pressed = false;
	private static boolean rpressed = false;
	public static boolean opressed = false;
	
	//Options
	private final boolean smoothScroll = true;
	private final float smoothFriction = 0.75f; //Friction of the smooth scrolling
	
	private Main(){
		WindowManager.init();
		window = WindowManager.createWindow(width,height, "Insanity Sweeper", false);

		//TODO: Make this into an input Class
		GLFWCursorPosCallback posCallback;
		glfwSetCursorPosCallback(window, posCallback = new GLFWCursorPosCallback() {
		    @Override
		    public void invoke(long window, double xpos, double ypos) {
		    	Main.mousex = (float) xpos;
		    	Main.mousey = (float) ypos;
		    }
		});
		GLFWCharCallback charCallback;
		glfwSetCharCallback(window, charCallback = new GLFWCharCallback(){
			@Override
			public void invoke(long window, int codepoint){
				if(dialog){
					input = input + String.valueOf(Character.toChars(codepoint));
				} else {
					input = "";
				}
			}
		});
		//END INPUT CLASS STUFF

		//TODO Rework RenderEngine into the lowest level rendering.
		renderer = new RenderEngine("default", width, height);

		//Highest Level Of Game
		map = new TileMap(mapwidth,mapheight,nummines);

		//TODO Rework the GUI System into one with greater flexibility

		GuiRenderer guiRenderer = new GuiRenderer(new Loader());
		while (!WindowManager.shouldWindowClose(window)) {
			//TODO All GL Calls should only Occur in the RenderEngine.
			float time = (float) GLFW.glfwGetTime();
			delta = time - lastFrame;
			lastFrame = time;
			if(time - lastFPS > 1f){
				lastFPS += 1;
				fps = (int) FPS;
				FPS = 0;
			}
			FPS++;

			//TODO GUIHandlerInputCode here
			if(mousex > 4*UI_SCALE && mousex < width-(28*UI_SCALE ) && mousey > ((guiRenderer.mainFont.getGlyphSize()+8)*2+4)*UI_SCALE && mousey < height - (28 * UI_SCALE)) {
				input();
			}
			//END

			//The Render Engine should handle all GAME_SCALE references in the Future
			if(smoothScroll){
				viewx += smoothx*GAME_SCALE;
				viewy += smoothy*GAME_SCALE;
				smoothx*=smoothFriction;
				smoothy*=smoothFriction;
			}
			if(viewx >= 32*GAME_SCALE){
				viewx = 32*GAME_SCALE;
			}
			if(viewx <= -(map.getMap().length*16*GAME_SCALE) + width -64*GAME_SCALE){
				viewx = -(map.getMap().length*16*GAME_SCALE) + width -64*GAME_SCALE;
			}
			if(viewy <= -(map.getMap()[0].length*16*GAME_SCALE) + height -64*GAME_SCALE){
				viewy = -(map.getMap()[0].length*16*GAME_SCALE) + height -64*GAME_SCALE;
			}
			if(viewy >= 96*GAME_SCALE){
				viewy =96*GAME_SCALE;
			}

			Tile[][] tiles = map.getMap();

			if(tiles.length*16*GAME_SCALE < width-(32*UI_SCALE)){
				viewx = 4*UI_SCALE - (tiles.length/2f)*16*GAME_SCALE + (renderer.getWidth()/2-32)*UI_SCALE;
			}

			if(tiles[0].length*16*GAME_SCALE < height-(((guiRenderer.mainFont.getGlyphSize()+8)*2 + 32)*UI_SCALE)){
				viewy = (guiRenderer.mainFont.getGlyphSize()+8)*2*UI_SCALE + ((renderer.getHeight()-(guiRenderer.mainFont.getGlyphSize()+8)*2-32)*UI_SCALE)/2 - (tiles[0].length/2f)*16*GAME_SCALE;
			}

			float squaresize = 16*GAME_SCALE;
			int minx = (int) -Math.ceil(viewx/squaresize)-1;
			int miny = (int) -Math.ceil(((viewy-((guiRenderer.mainFont.getGlyphSize()+8)*2+4)*UI_SCALE)/squaresize))-1;
			int maxx = (int) Math.ceil((-viewx/squaresize)+ ((width-28*UI_SCALE)/squaresize));
			int maxy = (int) Math.ceil((-viewy/squaresize)+ ((height-28*UI_SCALE)/squaresize));

			if(minx < 0){
				minx = 0;
			}

			if(maxx > tiles.length){
				maxx = tiles.length;
			}

			if(miny < 0){
				miny = 0;
			}

			if(maxy > tiles[0].length){
				maxy = tiles[0].length;
			}

			for(int x=minx; x<maxx;x++){
				for(int y=miny; y<maxy;y++){
					renderer.processTile(tiles[x][y]);
				}
			}
            renderer.prepareRender();
			//TODO: Make GUI.render call after this
			guiRenderer.preRender(renderer);
			renderer.renderTiles(viewx, viewy);
			guiRenderer.renderGui(renderer, String.valueOf(fps));
			WindowManager.update(window);
		}
		WindowManager.destroyWindow(window);
	}

	//TODO InputManager class maybe?
	private void input(){
		if(glfwGetKey(window, GLFW_KEY_A) == 1){
			if(!smoothScroll){
				viewx += (480 / GAME_SCALE) * delta;
			} else {
				smoothx = (480 / GAME_SCALE) * delta;
			}
		}
		
		if(glfwGetKey(window, GLFW_KEY_D) == 1){
			if(!smoothScroll){
				viewx -= (480 / GAME_SCALE) * delta;
			} else {
				smoothx = -(480 / GAME_SCALE) * delta;
			}
		}
		
		if(glfwGetKey(window, GLFW_KEY_W) == 1){
			if(!smoothScroll){
				viewy += (480 / GAME_SCALE) * delta;
			} else {
				smoothy = (480 / GAME_SCALE) * delta;
			}
		}
		
		if(glfwGetKey(window, GLFW_KEY_S) == 1){
			if(!smoothScroll){
				viewy -= (480 / GAME_SCALE) * delta;
			} else {
				smoothy = -(480 / GAME_SCALE) * delta;
			}
		}

		if(glfwGetKey(window, GLFW_KEY_F2) == 1){
			if(!refreshed){
				map.generateMap();
				refreshed = true;
			}
		} else {
			refreshed = false;
		}
		
		if(mousex < width && mousey < height){
			int tilex = (int) (Math.floor((-viewx + mousex))/(16*GAME_SCALE));
			int tiley = (int) (Math.floor((-viewy + mousey))/(16*GAME_SCALE));
			if(!opressed){
				if(glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == 1){
					map.setHovered(tilex, tiley);
					if(!pressed){
						pressed = true;
					}
				}
			} else {
				if(glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == 0){
					opressed= false;
				}
			}
			if(pressed){
				if(glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == 0){
					map.click(tilex, tiley);
					pressed = false;
				}
			}
			if(glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_2) == 1){
				if(glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == 1){
					map.chord(tilex, tiley);
				}
				if(!rpressed){
					rpressed = true;
					map.flag(tilex, tiley);
				}
			} else {
				rpressed = false;
			}
		}
	}
	
	public static void main(String[] args){
		Main main = new Main();
	}
	
}
