package main;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;

import org.lwjgl.opengl.GL11;

import renderer.RenderEngine;

public class ScrollButton {
	int x, y, direction;
	float textIndex = 5;
	boolean clicked, pressed;
	float textSpace = 0.16666666666f;
	
	public ScrollButton(int x, int y, int direction){
		this.x = x;
		this.y = y;
		this.direction = direction;
		
		if(direction == 1){
			textIndex = 2;
		} else {
			textIndex = 0;
		}
	}
	
	public void render(){
		GL11.glColor4f(1, 1, 1, 1);
		
		if(clicked){
			if(direction == 1){
				textIndex = 3;
			} else {
				textIndex = 1;
			}
		} else {
			if(direction == 1){
				textIndex = 2;
			} else {
				textIndex = 0;
			}
		}
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, RenderEngine.interfaces.getID());
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2d(0, textIndex * textSpace);
		GL11.glVertex2f(x, y);
		GL11.glTexCoord2d(0, (textIndex +1) * textSpace);
		GL11.glVertex2f(x, y+16);
		GL11.glTexCoord2d(1, (textIndex+1)*textSpace);
		GL11.glVertex2f(x+16, y+16);
		GL11.glTexCoord2d(1, textIndex * textSpace);
		GL11.glVertex2f(x+16, y);
		GL11.glEnd();
	}
	
	public void input(float rx, float ry, OpenBox parent){

		
if(glfwGetMouseButton(Main.window, GLFW_MOUSE_BUTTON_1) == 1){
		
	if(rx > x && rx < (x + 16) && ry > y && ry < (y + 16)){
		clicked = true;
		if(!pressed){
			if(direction == -1){

				if(parent.offset > 0){
			parent.offset-=1;	
				}
			} else {
	
				if(parent.offset < parent.files.length - 10){
					parent.offset+=1;
				}

			}
			pressed = true;
		}
	} else {
		clicked = false;

	}
	
} else {
	clicked = false;
	pressed = false;
}
	}
	
}
