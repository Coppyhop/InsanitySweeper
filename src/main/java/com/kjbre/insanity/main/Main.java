package com.kjbre.insanity.main;

import com.kjbre.insanity.rendering.RenderEngine;
import com.kjbre.insanity.rendering.WindowManager;

public class Main {

    RenderEngine renderEngine;

    public void init(){
        WindowManager.init();
        long window = WindowManager.createWindow(1024, 768, "Test", false);
        renderEngine = new RenderEngine(window);
        renderEngine.init();
        while(!WindowManager.shouldWindowClose(window)){
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
