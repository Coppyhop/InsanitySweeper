package com.kjbre.insanity.rendering;

import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.GLFW.*;

public class RenderEngine {

    private long window;

    public RenderEngine(long window){
        this.window = window;
        glfwMakeContextCurrent(this.window);
        GL.createCapabilities();
    }

    public void init(){

    }

    public void render(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

}