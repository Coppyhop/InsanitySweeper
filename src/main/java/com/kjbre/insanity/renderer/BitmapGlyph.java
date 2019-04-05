package com.kjbre.insanity.renderer;

class BitmapGlyph {
	private final int charId;
	private final char charRep;
	private final float x;
    private final float y;
	private final float width;
    private final float height;
	private final int xoffset;
    private final int yoffset;
	private final int xadvance;

	public BitmapGlyph(int charId, int x, int y, int width, int height, int xoffset, int yoffset, int xadvance) {
		super();
		this.charId = charId;
		this.charRep =(char) charId;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.xoffset = xoffset;
		this.yoffset = yoffset;
		this.xadvance = xadvance;
	}

	public int getCharId() {
		return charId;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public int getXoffset() {
		return xoffset;
	}

	public int getYoffset() {
		return yoffset;
	}

	public char getCharRep() {
		return charRep;
	}

	public int getXadvance() {
		return xadvance;
	}

	public String toString(){
		return charRep + ": " +x+", " + y + " W: " + width + " H:" + height;
	}

}
