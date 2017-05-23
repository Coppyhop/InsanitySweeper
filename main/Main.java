package main;

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

import renderer.Loader;
import renderer.RenderEngine;
import renderer.Texture;

public class Main {

	float viewx = 0, viewy = 0, smoothx = 0, smoothy =0;
	static int width=1024, height=768, mapheight = 1600, mapwidth = 3000, nummines = 999999;
	float delta, lastFrame, lastFPS, FPS;
	int fps = 0, selicon = -1;
	static float mousex =0, mousey =0;
	public static boolean dialog = false;
	RenderEngine renderer;
	OpenBox open;
	SaveBox save;
	NewBox newbox;
	SettingsBox settings;
	public static String input="";
	public static long window;
	static TileMap map;
	public static boolean refreshed = false, pressed = false, rpressed = false, opressed = false, hgrabbed = false, vgrabbed = false;
	
	//Options
	boolean smoothScroll = true, useModern = false, useVsync = false, safeMode = false; //Use Modern means "use VBOs", not implemented yet
	float smoothFriction = 0.8f; //Friction of the smooth scrolling
	
	public Main(){

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
			if(time - lastFPS > 1){
				lastFPS += 1;
				fps = (int) FPS;
				FPS = 0;
			}
			FPS++;
			

			if(!dialog){
			input();
			} else {
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
				viewx += smoothx;
				viewy += smoothy;
				
				smoothx*=smoothFriction;
				smoothy*=smoothFriction;
			}
			
			if(viewx >= 16){
				viewx = 16;
			}
			
			if(viewx <= -(map.getMap().length*16) + width -16){
				viewx = -(map.getMap().length*16) + width -16;
			}
			
			
			if(viewy <= -(map.getMap()[0].length*16) + height -16){
				viewy = -(map.getMap()[0].length*16) + height -16;
			}
			
			if(viewy >= 16){
				viewy =16;
			}


			Tile[][] tiles = map.getMap();
			
			float squaresize = 16;
			

			
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
	
	public void input(){
		
		if(glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == 0){
			if(hgrabbed){
				hgrabbed = false;
			}
			
			if(vgrabbed){
				vgrabbed = false;
			}
		}
		
		if(glfwGetKey(window, GLFW_KEY_A) == 1){
			
			if(!smoothScroll){
			viewx += 480 * delta;
			} else {
				smoothx = 480 * delta;
			}
		}
		
		if(glfwGetKey(window, GLFW_KEY_D) == 1){
			if(!smoothScroll){
			viewx -= 480 * delta;
			} else {
				smoothx = -480 * delta;
			}

		}
		
		if(glfwGetKey(window, GLFW_KEY_W) == 1){
			if(!smoothScroll){
			viewy += 480 * delta;
			} else {
				smoothy = 480 * delta;
			}

		}
		
		if(glfwGetKey(window, GLFW_KEY_S) == 1){
			if(!smoothScroll){
			viewy -= 480 * delta;
			} else {
				smoothy = -480 * delta;
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
		
		if(mousex < width - 16 && mousey < height -48){
			int tilex = (int) Math.floor((-viewx + mousex))/16;
			int tiley = (int) Math.floor((-viewy + mousey))/16;
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
		} else { rpressed = false;}
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
			
			if(mousey < height -16){
				
				if(!opressed){
				if(mousex > 0 && mousex < 32){
					selicon = 0;
				} else if(mousex > 32 && mousex < 64){
					selicon = 1;
				}  else if(mousex > 64 && mousex < 96){
					selicon = 2;
				} else if(mousex > 96 && mousex < 128){
					selicon = 3;
				} else {
					selicon = -1;
				}
				}
			} else {
				selicon = -1;
			}
			
			if(mousex > width -16){
				
				if(mapheight * 16 > height){
				if(glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == 1){
					if(!hgrabbed && !opressed){
					vgrabbed = true;
				    opressed = true;
					}
				}
				}
			}
			

			if(mapwidth * 16 > width){
			if(mousey > height -16){
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
		    float totalpixelshigh = (mapwidth * 16 + 32) - width;
		    totalpixelshigh = totalpixelshigh/(width-16);
		    float y = (mousex)*(totalpixelshigh);
		    viewx = -y;
		}
		
		if(vgrabbed){
		    float totalpixelshigh = (mapheight * 16 + 32) - height;
		    totalpixelshigh = totalpixelshigh/height;
		    float y = (mousey)*(totalpixelshigh);
		    viewy = -y;
		}
		
	}
	
	public static void main(String[] args){
		Main main = new Main();
	}
	
}
