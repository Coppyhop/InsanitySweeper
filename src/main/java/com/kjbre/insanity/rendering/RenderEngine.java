package com.kjbre.insanity.rendering;

import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;

public class RenderEngine {

    private long window;
    private Quad quad;
    public RenderEngine(long window){
        this.window = window;
        glfwMakeContextCurrent(this.window);
        GL.createCapabilities();
    }

    public void init(){
        quad = new Quad();
        quad.init();
    }

    public void render(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        quad.render();
    }

}