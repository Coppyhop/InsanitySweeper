package com.kjbre.insanity.main;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;
import org.lwjgl.opengl.GL11;
import com.kjbre.insanity.renderer.BitmapString;
import com.kjbre.insanity.renderer.RenderEngine;

class Button {

	private final BitmapString text;
	private boolean hovered = true;
    private boolean clicked = false;
	private float x;
    private float y;
	private float width;
    private final float height = 18;
	
	public void input(float rx, float ry, DialogBox parent){
        hovered = rx > x * Main.UI_SCALE && rx < (x * Main.UI_SCALE + width) && ry > y * Main.UI_SCALE && ry < (y * Main.UI_SCALE + height * Main.UI_SCALE);
        if(glfwGetMouseButton(Main.window, GLFW_MOUSE_BUTTON_1) == 1){
            if(hovered){
                Main.opressed = true;
                if(!clicked) {
					click(parent);
					clicked = true;
				}
            }
        } else {
        	clicked = false;
		}
	}
	
	void click(DialogBox parent){
			parent.isSelected =false;
			Main.dialog = false;
	}
	
	public Button(String text, float x, float y){
		this.text = new BitmapString(text, RenderEngine.font, 0.5f);
		this.width = this.text.getWidth() + 2;
	}
	
	public void render(){
		this.width = this.text.getWidth() + 2;
		if(!hovered){
		    GL11.glColor4f(0.8f, 0.8f, 0.8f, 1f);
		} else {
		    GL11.glColor4f(0.4f, 0.8f, 0.4f, 1f);
		}
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(x*Main.UI_SCALE, y*Main.UI_SCALE);
		GL11.glVertex2f(x*Main.UI_SCALE, y*Main.UI_SCALE+height*Main.UI_SCALE);
		GL11.glVertex2f(x*Main.UI_SCALE+width, y*Main.UI_SCALE+height*Main.UI_SCALE);
		GL11.glVertex2f(x*Main.UI_SCALE+width, y*Main.UI_SCALE);
		GL11.glEnd();
		GL11.glColor4f(0, 0, 0, 1f);
		text.render(x*Main.UI_SCALE+Main.UI_SCALE, y*Main.UI_SCALE+Main.UI_SCALE);
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

	public float getStaticWidth() {
		return (this.text.getWidth()/Main.UI_SCALE) + 2;
	}

}
