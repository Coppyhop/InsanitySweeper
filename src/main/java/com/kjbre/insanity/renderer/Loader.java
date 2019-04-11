package com.kjbre.insanity.renderer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Loader {

	public Texture loadTexture(String filename){
		ByteBuffer buf = null;
		int tWidth = 0;
		int tHeight = 0;
		try {
		    InputStream in = new FileInputStream(filename);
		    PNGDecoder decoder = new PNGDecoder(in);
		    tWidth = decoder.getWidth();
		    tHeight = decoder.getHeight();
		    buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
		    decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
		    buf.flip();
		    in.close();
		} catch (IOException e) {
		    e.printStackTrace();
		    System.exit(-1);
		}
		int texId = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, tWidth, tHeight, 0, GL11.GL_RGBA,
				GL11.GL_UNSIGNED_BYTE, buf);
		GL11.glBindTexture(0, GL11.GL_TEXTURE_2D);
		return new Texture(texId);
	}
	
	public BitmapFont loadFont(String filename){
		ArrayList<BitmapGlyph> glyphs = new ArrayList<>();
		Texture bitmap = null;
		float glyphSize = 0;
		try (final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.isEmpty()) { continue; }
				if(line.startsWith("info ")){
					String[] test = line.split(" ");
					for(String s: test){
						if(s.startsWith("size=")){
							glyphSize = Integer.valueOf(s.substring(5));
						}
					}
				}
				if(line.startsWith("page ")){
					String[] test = line.split(" ");
					for(String s: test){
						if(s.startsWith("file=")){
							String fName = s.substring(6, s.length() -1);
							bitmap = loadTexture("data/skins/" + fName);
						}
					}
				}
				if(line.startsWith("char ")){
					String[] test = line.split(" ");
					int charId = 0;
					int x=0,y=0,width=0,height=0, xoffset=0, yoffset=0, xadvance=0;
					for(String s:test){
						if(s.startsWith("id=")){
							charId = Integer.valueOf(s.substring(3));
						}
						if(s.startsWith("x=")){
							x = Integer.valueOf(s.substring(2));
						}
						if(s.startsWith("y=")){
							y = Integer.valueOf(s.substring(2));
						}
						if(s.startsWith("width=")){
							width = Integer.valueOf(s.substring(6));
						}
						if(s.startsWith("height=")){
							height = Integer.valueOf(s.substring(7));
						}
						if(s.startsWith("xoffset=")){
							xoffset = Integer.valueOf(s.substring(8));
						}
						if(s.startsWith("yoffset=")){
							yoffset = Integer.valueOf(s.substring(8));
						}
						if(s.startsWith("xadvance=")){
							xadvance = Integer.valueOf(s.substring(9));
						}
					}
					glyphs.add(new BitmapGlyph(charId, x, y, width, height, xoffset, yoffset, xadvance));
				}
			}
			br.close();
			return new BitmapFont(256,256, glyphSize, bitmap, glyphs);
		 } catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
		 return null;
	}
	
}
