package main;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;

import org.lwjgl.opengl.GL11;

import renderer.BitmapString;
import renderer.RenderEngine;

public class SettingsBox extends DialogBox{

	Button closeButton;
	
	public SettingsBox(){
		super();
		closeButton = new Button("Save",0,0);
		closeButton.setX(75 - closeButton.getWidth() -2);
		closeButton.setY(100 - 20);
		
	}
	@Override
	public void input(int width, int height) {
		float relativeX = Main.mousex - width/2;
		float relativeY = Main.mousey - height/2;
		
		closeButton.input(relativeX, relativeY, this);
		
	}

	@Override
	public void render() {
		GL11.glColor4f(0.3f, 0.3f, 0.3f, 1.0f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(-75, -100);
		GL11.glVertex2f(-75, 100);
		GL11.glVertex2f(75, 100);
		GL11.glVertex2f(75, -100);
		GL11.glColor4f(0.1f, 0.1f, 0.1f, 1.0f);
		GL11.glVertex2f(-75, -100);
		GL11.glVertex2f(-75, -84);
		GL11.glVertex2f(75, -84);
		GL11.glVertex2f(75, -100);
		GL11.glEnd();
		GL11.glColor4f(1f, 1f, 1f, 1.0f);
		BitmapString test = new BitmapString("Settings", RenderEngine.font, 0.5f);
		BitmapString na = new BitmapString("Not Added Yet.", RenderEngine.font, 0.5f);
		test.render(-test.getWidth()/2, -100);
		na.render(-na.getWidth()/2, -8);
		GL11.glColor4f(1f, 1f, 1f, 1.0f);
		closeButton.render();
		
	}

}
