package main;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;

import org.lwjgl.opengl.GL11;

import renderer.BitmapString;

public class SingleTextSelectionElement {
	
	private final BitmapString text;
	boolean selected, hovered;
	private final float x;
    private final float y;
    private final float width;
	
	public SingleTextSelectionElement(float x, float y, float width, BitmapString text){
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
	}
	
	public void input(float relativeX, float relativeY, float offset){
		float ry = y - (offset*16*Main.UI_SCALE);
		if(relativeX > x && relativeX < (x + width) && relativeY > ry && relativeY < (ry + 16*Main.UI_SCALE)){
			hovered = true;
			if(glfwGetMouseButton(Main.window, GLFW_MOUSE_BUTTON_1) == 1){
				for(int i=0;i<OpenBox.files.length;i++){
					OpenBox.files[i].selected = false;
				}
				selected = true;
			}
		} else {
			hovered = false;
		}
	}
	
	public void render(float offset){
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y - (offset*16*Main.UI_SCALE), 0);
		if(selected){
			GL11.glColor4f(0.4f,0.8f,0.4f,1.0f);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(0,0);
			GL11.glVertex2f(width,0);
			GL11.glVertex2f(width,16*Main.UI_SCALE);
			GL11.glVertex2f(0,16*Main.UI_SCALE);
			GL11.glEnd();
			GL11.glColor4f(0.2f, 0.2f, 0.2f, 1.0f);
		} else if(hovered) {
			GL11.glColor4f(0.4f, 0.8f, 0.4f, 1.0f);
		} else {
			GL11.glColor4f(0.8f, 0.8f, 0.8f, 1.0f);
		}
		text.render(0, 0,width);
		GL11.glPopMatrix();
	}

}
