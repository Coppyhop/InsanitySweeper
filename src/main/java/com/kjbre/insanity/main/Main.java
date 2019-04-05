package com.kjbre.insanity.main;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

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
	public static boolean hgrabbed = false;
	public static boolean vgrabbed = false;
	private static boolean justExited = false;
	
	//Options
	private final boolean smoothScroll = true;
	boolean useModern = false;
	private final boolean useVsync = false;
	private final boolean safeMode = false;
	//Use Modern means "use VBOs", not implemented yet
	private final float smoothFriction = 0.75f; //Friction of the smooth scrolling
	
	private Main(){
		GLFWErrorCallback.createPrint(System.err).set();
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); 
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		window = glfwCreateWindow(width, height, "Insanity Sweeper", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);
			glfwGetWindowSize(window, pWidth, pHeight);
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		}
		glfwMakeContextCurrent(window);
		if(useVsync){
			glfwSwapInterval(1);
		}
		glfwShowWindow(window);	
		GL.createCapabilities();
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
		renderer = new RenderEngine("default");
		map = new TileMap(mapwidth,mapheight,nummines);
		open = new OpenBox();
		save = new SaveBox();
		settings = new SettingsBox();
		newbox = new NewBox();
		GL11.glViewport(0, 0, width, height);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		if(!safeMode){
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(-width/2, width/2, height/2, -height/2, 1, -1);
		GL11.glTranslatef(-width/2, -height/2, 0);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		while ( !glfwWindowShouldClose(window) ) {
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
			if(!dialog){
			    if(justExited){
			        mousex = 0;
			        mousey = 0;
			        justExited = false;
                }
				input();
			} else {
			    selicon = -1;
			    justExited = true;
				if(save.isSelected){
					renderer.setDialog(save);
					save.input(width, height);
				}
				if(open.isSelected){
					renderer.setDialog(open);
					open.input(width, height);
				}
				if(settings.isSelected){
					renderer.setDialog(settings);
					settings.input(width, height);
				}
				if(newbox.isSelected){
					renderer.setDialog(newbox);
					newbox.input(width, height);
				}
			}
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
			renderer.render(width, height, viewx, viewy, mapwidth, mapheight, fps, selicon);
			glfwSwapBuffers(window);
			glfwPollEvents();
		}
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	private void input(){
		if(glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == 0){
			if(hgrabbed){
				hgrabbed = false;
				justExited = true;
			}
			if(vgrabbed){
				vgrabbed = false;
				justExited = true;
			}
		}
		
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
		
		if(mousex < width - 16*UI_SCALE && mousey < height -48*UI_SCALE){
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
			selicon = -1;
		} else {
			if(glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == 1){
				if(selicon == 0){
					newbox.isSelected = true;
					dialog = true;
				}
				if(selicon == 1){
					open.isSelected = true;
					open.update();
					dialog = true;
				}
				if(selicon == 2){
					save.isSelected = true;
					dialog = true;
				}
				if(selicon == 3){
					settings.isSelected = true;
					dialog = true;
				}
			}
			if(mousey < height -16*UI_SCALE){
				if(!opressed){
					if(mousex > 0 && mousex < 32*UI_SCALE){
						selicon = 0;
					} else if(mousex > 32*UI_SCALE && mousex < 64*UI_SCALE){
						selicon = 1;
					}  else if(mousex > 64*UI_SCALE && mousex < 96*UI_SCALE){
						selicon = 2;
					} else if(mousex > 96*UI_SCALE && mousex < 128*UI_SCALE){
						selicon = 3;
					} else {
						selicon = -1;
					}
				}
			} else {
				selicon = -1;
			}
			if(mousex > width -16*UI_SCALE){
				if(mapheight * 16*GAME_SCALE > height){
					if(glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == 1){
						if(!hgrabbed && !opressed){
						vgrabbed = true;
						opressed = true;
						}
					}
				}
			}
			if(mapwidth * 16*GAME_SCALE > width){
				if(mousey > height -16*UI_SCALE){
					if(glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == 1){
						if(!vgrabbed && !opressed){
							hgrabbed = true;
							opressed = true;
						}
					}
				}
			}
		}
		if(hgrabbed){
		    float totalpixelshigh = (mapwidth * 16 *GAME_SCALE + 32*GAME_SCALE) - width;
		    totalpixelshigh = totalpixelshigh/(width-16*GAME_SCALE);
		    float y = (mousex)*(totalpixelshigh);
		    viewx = -y;
		}
		if(vgrabbed){
		    float totalpixelshigh = (mapheight * 16*GAME_SCALE + 32*GAME_SCALE) - height;
		    totalpixelshigh = totalpixelshigh/height;
		    float y = (mousey)*(totalpixelshigh);
		    viewy = -y;
		}
	}
	
	public static void main(String[] args){
		Main main = new Main();
	}
	
}
