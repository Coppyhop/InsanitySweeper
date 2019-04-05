package main;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;

import org.lwjgl.opengl.GL11;

import renderer.BitmapString;

public class SingleTextSelectionElement {
	
	BitmapString text;
	boolean selected;
	float x, y, width;
	
	public SingleTextSelectionElement(float x, float y, float width, BitmapString text){
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
	}
	
	public void input(float relativeX, float relativeY, float offset){
		float ry = y - (offset*16);
		if(glfwGetMouseButton(Main.window, GLFW_MOUSE_BUTTON_1) == 1){
			if(relativeX > x && relativeX < (x + width) && relativeY > ry && relativeY < (ry + 16)){
				for(int i=0;i<OpenBox.files.length;i++){
					OpenBox.files[i].selected = false;
				}
				selected = true;
			}
		}
	}
	
	public void render(float offset){
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y - (offset*16), 0);
		if(selected){
			GL11.glColor4f(0.4f, 0.8f, 0.4f, 1.0f);
		} else {
			GL11.glColor4f(0.8f, 0.8f, 0.8f, 1.0f);
		}
		text.render(0, 0);
		GL11.glPopMatrix();
	}

}
