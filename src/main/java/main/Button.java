package main;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;
import org.lwjgl.opengl.GL11;
import renderer.BitmapString;
import renderer.RenderEngine;

public class Button {

	BitmapString text;
	boolean hovered = true;
	float x, y;
	float width, height = 18;
	
	public void input(float rx, float ry, DialogBox parent){
		if(rx > x && rx < (x + width) && ry > y && ry < (y + height)){
			hovered = true;
		} else {
			hovered = false;
		}
        if(glfwGetMouseButton(Main.window, GLFW_MOUSE_BUTTON_1) == 1){
            if(hovered){
                Main.opressed = true;
		        click(parent);
            }
        }
	}
	
	public void click(DialogBox parent){
			parent.isSelected =false;
			Main.dialog = false;
	}
	
	public Button(String text, int x, int y){
		this.text = new BitmapString(text, RenderEngine.font, 0.5f);
		this.width = this.text.getWidth() + 2;
	}
	
	public void render(){
		if(!hovered){
		    GL11.glColor4f(0.8f, 0.8f, 0.8f, 1f);
		} else {
		    GL11.glColor4f(0.4f, 0.8f, 0.4f, 1f);
		}
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x, y+height);
		GL11.glVertex2f(x+width, y+height);
		GL11.glVertex2f(x+width, y);
		GL11.glEnd();
		GL11.glColor4f(0, 0, 0, 1f);
		text.render(x+1, y+1);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

}
