package main;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import renderer.BitmapString;
import renderer.RenderEngine;

public class TextBox {

	String heldText = "";
	float width, height;
	float x, y;
	boolean selected = false, pressed = false;
	
	public TextBox(float x, float y, float width){
		height= 18;
		this.width = width;
		this.x = x;
		this.y = y;
	}
	
	public void input(float rx, float ry, DialogBox parent){
		if(!selected){
			Main.input = heldText;
		} else {
			heldText = Main.input;
			if(glfwGetKey(Main.window, GLFW.GLFW_KEY_BACKSPACE) == 1){
				if(!pressed){
					pressed = true;
					if(!Main.input.equals("")){
						Main.input = Main.input.substring(0, Main.input.length() -1);
					}
				}
			} else {
				pressed = false;
			}
		}
		if(glfwGetMouseButton(Main.window, GLFW_MOUSE_BUTTON_1) == 1){
			if(rx > x*Main.UI_SCALE && rx < (x*Main.UI_SCALE + width * Main.UI_SCALE) && ry > y*Main.UI_SCALE && ry < (y*Main.UI_SCALE + (height * Main.UI_SCALE))){
				selected = true;
			}  else {
				selected = false;
			}
		}
	}
	
	public void render(){
		GL11.glColor4f(0.2f, 0.2f, 0.2f, 1f);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glBegin(GL11.GL_QUADS);
		RenderEngine.dsVertex2f(x, y);
		RenderEngine.dsVertex2f(x, y+height);
		RenderEngine.dsVertex2f(x+width, y+height);
		RenderEngine.dsVertex2f(x+width, y);
		GL11.glEnd();
		GL11.glColor4f(1f, 1f, 1f, 1f);
		BitmapString userInput = new BitmapString(Main.input, RenderEngine.font, 0.5f);
		if(selected){
			GL11.glBegin(GL11.GL_LINES);
			RenderEngine.dsVertex2f(x, y);
			RenderEngine.dsVertex2f(x, y+height);
			RenderEngine.dsVertex2f(x+width, y+height);
			RenderEngine.dsVertex2f(x+width, y);
			RenderEngine.dsVertex2f(x, y);
			RenderEngine.dsVertex2f(x+width, y);
			RenderEngine.dsVertex2f(x, y+height);
			RenderEngine.dsVertex2f(x+width, y+height);
			if(GLFW.glfwGetTime() %2 > 1 && userInput.getStaticWidth() < width){
				RenderEngine.dsVertex2f(x+userInput.getStaticWidth()+2, y+2);
				RenderEngine.dsVertex2f(x+userInput.getStaticWidth()+2, y+height-2);
			}
			GL11.glEnd();
		}
		userInput.render((x+2)*Main.UI_SCALE, (y+2)*Main.UI_SCALE);
	}
	
}
