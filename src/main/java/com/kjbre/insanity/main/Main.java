package com.kjbre.insanity.main;

import com.kjbre.insanity.engine.Rectangle;
import com.kjbre.insanity.rendering.RenderEngine;
import com.kjbre.insanity.rendering.WindowManager;
import com.kjbre.insanity.tools.Vector3f;

public class Main {

    RenderEngine renderEngine;

    public void init(){
        WindowManager.init();
        long window = WindowManager.createWindow(1024, 768, "Test", false);
        renderEngine = new RenderEngine(window);
        renderEngine.init();
        Rectangle rectangle = new Rectangle(200,200, 200,200);
        rectangle.setColor(new Vector3f(1,0,0));
        while(!WindowManager.shouldWindowClose(window)){
            renderEngine.processRectangle(rectangle);
            renderEngine.render();
            WindowManager.update(window);
        }
        WindowManager.destroyWindow(window);
    }

    public static void main(String[] args){
        Main main = new Main();
        main.init();
    }

}
