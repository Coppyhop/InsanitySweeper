package main;

public abstract class DialogBox {

	public boolean isSelected = false;

	DialogBox(){ }
	
	public abstract void input(int width, int height);
	
	public abstract void render();
	
	public void destroy(){
		Main.dialog = false;
	}
	
}
