package com.kjbre.insanity.gui;

import com.kjbre.insanity.main.Main;
import com.kjbre.insanity.renderer.BitmapFont;
import com.kjbre.insanity.renderer.BitmapString;
import com.kjbre.insanity.renderer.RenderEngine;
import com.kjbre.insanity.renderer.Texture;
import com.kjbre.insanity.renderer.Loader;

public class GuiRenderer {
    public BitmapFont mainFont;
    Texture guiTexture, guiButtons;

    public GuiRenderer(Loader loader){
        mainFont = loader.loadFont("data/skins/default_font.fnt");
        guiTexture = loader.loadTexture("data/skins/guiBorder.png");
        guiButtons = loader.loadTexture("data/skins/default_interface.png");
    }

    public void renderGui(RenderEngine renderer, String fps, Texture testTexture){
        BitmapString fpsString = new BitmapString("FPS: " + fps, mainFont);
        BitmapString titleString = new BitmapString("Insanity Sweeper", mainFont);
        BitmapString guiPOCString = new BitmapString("Game  Edit  Help", mainFont);
        renderBorderedRectangle(0,0, renderer.getWidth(), mainFont.getGlyphSize()+8, 0, 0, 1, renderer);
        renderBorderedRectangle(0,mainFont.getGlyphSize()+8, renderer.getWidth(), mainFont.getGlyphSize()+8, 0.8f, 0.8f, 0.8f, renderer);
        renderEmptyBorder(0,(mainFont.getGlyphSize()+8)*2, renderer.getWidth()-(24), renderer.getHeight()-(mainFont.getGlyphSize()+8)*2-24, renderer);
        renderBorderedRectangle(0,renderer.getHeight()-24, renderer.getWidth()-24, 24, 0.8f, 0.8f, 0.8f, renderer);
        renderBorderedRectangle(renderer.getWidth()-24,(mainFont.getGlyphSize()+8)*2, 24, renderer.getHeight()-(mainFont.getGlyphSize()+8)*2, 0.8f, 0.8f, 0.8f, renderer);
        renderer.setColor(1,1,1,1);
        titleString.render(6,3, renderer);
        fpsString.render(renderer.getWidth()-(fpsString.getWidth()+6)-20, 3, renderer);
        renderer.setColor(0,0,0, 1);
        guiPOCString.render(6, 3 + mainFont.getGlyphSize()+8, renderer);

        float buttonPiece = 1f/12f;
        renderer.setColor(1,1,1,1);
        renderer.setTexture(guiButtons);

        //Horizontal Scrollbar
        renderer.drawRectangle(4,renderer.getHeight()-20,16,16, 0, 6*buttonPiece, 1, 7*buttonPiece, Main.UI_SCALE);
        renderer.drawRectangle(renderer.getWidth()-44,renderer.getHeight()-20,16,16, 0, 4*buttonPiece, 1, 5*buttonPiece, Main.UI_SCALE);
        renderer.drawRectangle(20,renderer.getHeight()-20,16,16,  0, 8*buttonPiece, 1, 9*buttonPiece, Main.UI_SCALE);

        //Vertical Scrollbar
        renderer.drawRectangle(renderer.getWidth()-20,(mainFont.getGlyphSize()+8)*2+4,16,16, 0, 0, 1, buttonPiece, Main.UI_SCALE);
        renderer.drawRectangle(renderer.getWidth()-20,renderer.getHeight()-20,16,16, 0, 2*buttonPiece, 1, 3*buttonPiece, Main.UI_SCALE);
        renderer.drawRectangle(renderer.getWidth()-20,(mainFont.getGlyphSize()+8)*2+20,16,16, 0, 8*buttonPiece, 1, 9*buttonPiece,Main.UI_SCALE);

        renderer.drawRectangle(renderer.getWidth()-20, 4, 16, 16, 0, 10*buttonPiece, 1, 11*buttonPiece, Main.UI_SCALE);
    }

    public void renderBorderedRectangle(float x, float y, float width, float height, float r, float g, float b, RenderEngine renderer){
        float borderSize = 4f;
        renderer.setColor(1,1,1,1f);
        renderer.setTexture(guiTexture);
        renderer.drawRectangle(x+borderSize, y, width-borderSize*2, borderSize, 1f/3f, 0, 2f/3f, 1f/3f, Main.UI_SCALE);
        renderer.drawRectangle(x+borderSize, y+height-borderSize, width-borderSize*2, borderSize, 1f/3f, 2f/3f, 2f/3f, 1, Main.UI_SCALE);
        renderer.drawRectangle(x,y+borderSize, borderSize, height-borderSize*2, 0, 1f/3f, 1f/3f, 2f/3f, Main.UI_SCALE);
        renderer.drawRectangle(x+width-borderSize,y+borderSize, borderSize, height-borderSize*2, 2f/3f, 1f/3f, 1, 2f/3f, Main.UI_SCALE);

        renderer.drawRectangle(x,y,borderSize,borderSize,0,0,1f/3f,1f/3f,Main.UI_SCALE);
        renderer.drawRectangle(x+width-borderSize,y,borderSize,borderSize,2f/3f, 0, 1, 1f/3f, Main.UI_SCALE);
        renderer.drawRectangle(x+width-borderSize,y+height-borderSize,borderSize,borderSize, 2f/3f, 2f/3f, 1, 1, Main.UI_SCALE);
        renderer.drawRectangle(x,y+height-borderSize,borderSize,borderSize,0, 2f/3f, 1f/3f, 1, Main.UI_SCALE);

        renderer.setColor(r,g,b,1);
        renderer.drawRectangle(x+borderSize, y+borderSize, width-borderSize*2, height-borderSize*2, 1f/3f, 1f/3f, 2f/3f, 2f/3f, Main.UI_SCALE);
    }

    public void renderEmptyBorder(float x, float y, float width, float height, RenderEngine renderer){
        float borderSize = 4f;
        renderer.setColor(1,1,1,1f);
        renderer.setTexture(guiTexture);
        renderer.drawRectangle(x+borderSize, y, width-borderSize*2, borderSize, 1f/3f, 0, 2f/3f, 1f/3f, Main.UI_SCALE);
        renderer.drawRectangle(x+borderSize, y+height-borderSize, width-borderSize*2, borderSize, 1f/3f, 2f/3f, 2f/3f, 1, Main.UI_SCALE);
        renderer.drawRectangle(x,y+borderSize, borderSize, height-borderSize*2, 0, 1f/3f, 1f/3f, 2f/3f, Main.UI_SCALE);
        renderer.drawRectangle(x+width-borderSize,y+borderSize, borderSize, height-borderSize*2, 2f/3f, 1f/3f, 1, 2f/3f, Main.UI_SCALE);

        renderer.drawRectangle(x,y,borderSize,borderSize,0,0,1f/3f,1f/3f,Main.UI_SCALE);
        renderer.drawRectangle(x+width-borderSize,y,borderSize,borderSize,2f/3f, 0, 1, 1f/3f, Main.UI_SCALE);
        renderer.drawRectangle(x+width-borderSize,y+height-borderSize,borderSize,borderSize, 2f/3f, 2f/3f, 1, 1, Main.UI_SCALE);
        renderer.drawRectangle(x,y+height-borderSize,borderSize,borderSize,0, 2f/3f, 1f/3f, 1, Main.UI_SCALE);
    }
}
