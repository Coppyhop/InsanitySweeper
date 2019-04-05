package renderer;

import java.util.ArrayList;

public class BitmapFont {

	private final float textureWidth;
    private final float textureHeight;
    private final float glyphSize;
	private final Texture fontTexture;
	private final ArrayList<BitmapGlyph> glyphs;
	
	public BitmapFont(float textureWidth, float textureHeight, float glyphSize, Texture fontTexture,
					  ArrayList<BitmapGlyph> glyphs) {
		super();
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		this.fontTexture = fontTexture;
		this.glyphSize = glyphSize;
		this.glyphs = glyphs;
	}

	public float getTextureWidth() {
		return textureWidth;
	}

	public float getTextureHeight() {
		return textureHeight;
	}

	public Texture getFontTexture() {
		return fontTexture;
	}

	public ArrayList<BitmapGlyph> getGlyphs() {
		return glyphs;
	}
	
	public float getGlyphSize(){
		return glyphSize;
	}
	
}
