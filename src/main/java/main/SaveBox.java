package main;
import java.io.BufferedWriter;
import java.io.FileWriter;
import org.lwjgl.opengl.GL11;
import renderer.BitmapString;
import renderer.RenderEngine;

public class SaveBox extends DialogBox{

	private final Button closeButton;
	private final Button saveButton;
	private final TextBox filename;

	public SaveBox(){
		super();
		closeButton = new Button("Close",0,0){
			@Override
			public void click(DialogBox parent){
				Main.input = "";
				parent.isSelected =false;
				Main.dialog = false;
			}
		};
		closeButton.setX(74 - closeButton.getStaticWidth());
		closeButton.setY(9);
		saveButton = new Button ("Save",0,0){
			@Override
			public void click(DialogBox parent){
				if(Main.input.equals("")) Main.input = "save";
				try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/saves/"+Main.input+".mine"))){
					for(int x=0; x<TileMap.map.length;x++){
						for(int y=0;y<TileMap.map[0].length;y++){
							bw.append(TileMap.map[x][y].toSave()).append("\n");
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
				Main.input = "";
				parent.isSelected =false;
				Main.dialog = false;
			}
		};
		saveButton.setX(74 - saveButton.getStaticWidth());
		saveButton.setY(-11);
		filename = new TextBox(-73, -11, 144 - saveButton.getStaticWidth());
	}
	@Override
	public void input(int width, int height) {
		float relativeX = Main.mousex - width/2;
		float relativeY = Main.mousey - height/2;
		closeButton.input(relativeX, relativeY, this);
		saveButton.input(relativeX, relativeY, this);
		filename.input(relativeX, relativeY, this);
	}

	@Override
	public void render() {
		GL11.glColor4f(0.3f, 0.3f, 0.3f, 1.0f);
		GL11.glBegin(GL11.GL_QUADS);
		RenderEngine.dsVertex2f(-75, -29);
		RenderEngine.dsVertex2f(-75, 29);
		RenderEngine.dsVertex2f(75, 29);
		RenderEngine.dsVertex2f(75, -29);
		GL11.glColor4f(0.1f, 0.1f, 0.1f, 1.0f);
		RenderEngine.dsVertex2f(-75, -29);
		RenderEngine.dsVertex2f(-75, -13);
		RenderEngine.dsVertex2f(75, -13);
		RenderEngine.dsVertex2f(75, -29);
		GL11.glEnd();
		GL11.glColor4f(1f, 1f, 1f, 1.0f);
		BitmapString test = new BitmapString("Save Game", RenderEngine.font, 0.5f);
		test.render(-test.getWidth()/2, -27*Main.UI_SCALE);
		closeButton.render();
		saveButton.render();
		filename.render();
	}

}
