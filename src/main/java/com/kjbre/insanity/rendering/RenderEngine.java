package com.kjbre.insanity.rendering;

import com.kjbre.insanity.engine.Rectangle;
import com.kjbre.insanity.tools.Matrix4f;
import com.kjbre.insanity.tools.Vector3f;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class RenderEngine {

    private long window;
    private int width, height;
    private Matrix4f projectionMatrix;
    private Quad quad;

    private ArrayList<Rectangle> rectangles = new ArrayList<>();

    public RenderEngine(long window){
        this.window = window;
        glfwMakeContextCurrent(this.window);
        GL.createCapabilities();
    }

    public void init(){
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer widthBuffer = stack.mallocInt(1);
            IntBuffer heightBuffer = stack.mallocInt(1);
            glfwGetFramebufferSize(window, widthBuffer, heightBuffer);
            width = widthBuffer.get();
            height = heightBuffer.get();
        }
        projectionMatrix = Matrix4f.orthographic(0f, width, 0f, height, -1f, 1f);
        quad = new Quad();
        quad.init(0,0, 1024, 768);
        quad.setProjectionMatrix(projectionMatrix);
        quad.setColor(new Vector3f(0.2f, 0.3f, 0.7f));
    }

    public void render(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        quad.render();
        for(Rectangle i:rectangles){
            Quad quads = i.toQuad();
            quads.setProjectionMatrix(projectionMatrix);
            quads.render();
        }
        rectangles.clear();
    }

    public void processRectangle(Rectangle rectangle){
        rectangles.add(rectangle);
    }
}