package com.kjbre.insanity;

import com.kjbre.insanity.main.Main;
import com.kjbre.insanity.renderer.BitmapFont;
import com.kjbre.insanity.renderer.BitmapString;
import com.kjbre.insanity.renderer.RenderEngine;
import com.kjbre.insanity.renderer.Texture;
import com.kjbre.insanity.renderer.Loader;

public class GuiRenderer {
    BitmapFont mainFont;

    public GuiRenderer(Loader loader){
        mainFont = loader.loadFont("data/skins/default_font.fnt");
    }

    public void renderGui(RenderEngine renderer, String fps, Texture testTexture){
        BitmapString fpsString = new BitmapString("FPS: " + fps, mainFont);
        renderer.setTexture(0);
        renderer.setColor(0,0,0,0.5f);
        renderer.drawRectangle(0,0, fpsString.getWidth()+4, mainFont.getGlyphSize()+4, Main.UI_SCALE);
        renderer.setColor(1,1,1,1);
        fpsString.render(2,2, renderer);
    }
}
