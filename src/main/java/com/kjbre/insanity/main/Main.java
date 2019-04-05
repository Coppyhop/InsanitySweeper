package com.kjbre.insanity.main;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import com.kjbre.insanity.renderer.WindowManager;
import com.sun.javaws.WinOperaSupport;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import com.kjbre.insanity.renderer.RenderEngine;

public class Main {

	private float viewx = 0;
	private float viewy = 0;
	private float smoothx = 0;
	private float smoothy =0;
	private static int width=1024;
	private static int height=768;
	private static final int mapheight = 1600;
	private static final int mapwidth = 3000;
	private static final int nummines = 99999;
	private float delta;
	private float lastFrame;
	private float lastFPS;
	private float FPS;
	public static float UI_SCALE = 2f, GAME_SCALE = 1.5f;
	private int fps = 0;
	private int selicon = -1;
	static float mousex =0, mousey =0;
	public static boolean dialog = false;
	private RenderEngine renderer;
	private OpenBox open;
	private SaveBox save;
	private NewBox newbox;
	private SettingsBox settings;
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
		window = WindowManager.createWindow(1024,768, "Insanity Sweeper", false);

		//TODO: Make this into an input Class
		GLFWWindowSizeCallback windowSizeCallback;
		glfwSetWindowSizeCallback(window, windowSizeCallback = new GLFWWindowSizeCallback(){
            @Override
            public void invoke(long window, int width, int height){
            	int ow = Main.width, oh = Main.height;
                Main.width = width;
                Main.height = height;
                int cw = Main.width - ow;
                int ch = Main.height - oh;
                cw/=2;
                ch/=2;
                glViewport(0, 0, width, height);
        		GL11.glMatrixMode(GL11.GL_PROJECTION);
        		GL11.glLoadIdentity();
        		GL11.glOrtho(0, width, height, 0, 1, -1);
        		viewx=viewx + cw;
        		viewy=viewy + ch;
            }
        });
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
		open = new OpenBox();
		save = new SaveBox();
		settings = new SettingsBox();
		newbox = new NewBox();

		while (!WindowManager.shouldWindowClose(window)) {
			//TODO All GL Calls should only Occur in the RenderEngine.
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
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
			input();
			//END

			//The Render Engine should handle all GAME_SCALE references in the Future
			if(smoothScroll){
				viewx += smoothx*GAME_SCALE;
				viewy += smoothy*GAME_SCALE;
				smoothx*=smoothFriction;
				smoothy*=smoothFriction;
			}
			if(viewx >= 64*GAME_SCALE){
				viewx = 64*GAME_SCALE;
			}
			if(viewx <= -(map.getMap().length*16*GAME_SCALE) + width -64*GAME_SCALE){
				viewx = -(map.getMap().length*16*GAME_SCALE) + width -64*GAME_SCALE;
			}
			if(viewy <= -(map.getMap()[0].length*16*GAME_SCALE) + height -64*GAME_SCALE){
				viewy = -(map.getMap()[0].length*16*GAME_SCALE) + height -64*GAME_SCALE;
			}
			if(viewy >= 64*GAME_SCALE){
				viewy =64*GAME_SCALE;
			}

			Tile[][] tiles = map.getMap();
			float squaresize = 16*GAME_SCALE;
			float minx = (float) -Math.ceil(viewx/squaresize)-1;
			float miny = (float) -Math.ceil(viewy/squaresize)-1;
			float maxx = (float) Math.ceil((-viewx/squaresize)+ (width/squaresize));
			float maxy = (float) Math.ceil((-viewy/squaresize)+ (height/squaresize));
			for(int x=0; x<tiles.length;x++){
				for(int y=0; y<tiles[0].length;y++){
					if(x > minx && y > miny && y < maxy && x < maxx){
					renderer.processTile(tiles[x][y]);
					}
				}
			}

			//TODO: Make GUI.render call after this
			renderer.renderTiles(viewx, viewy);
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
