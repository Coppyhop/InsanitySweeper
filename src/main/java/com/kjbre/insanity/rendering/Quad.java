package com.kjbre.insanity.rendering;

import com.kjbre.insanity.tools.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Quad {

    private VertexArrayObject vao;
    private VertexBufferObject vbo;
    private VertexBufferObject ebo;
    private Shader vertexShader;
    private Shader fragmentShader;
    private ShaderProgram program;
    private int uniModel;
    private int uniColor;
    private int uniProjection;
    private Vector3f color;

    public void init(float x, float y, float width, float height){
        color = new Vector3f(1f,1f,1f);
        /* Generate Vertex Array Object */
        vao = new VertexArrayObject();
        vao.bind();

        try (MemoryStack stack = MemoryStack.stackPush()) {
            /* Vertex data */
            FloatBuffer vertices = stack.mallocFloat(4 * 4);
            vertices.put(x).put(y).put(0f).put(0f);
            vertices.put(x + width).put(y).put(1f).put(0f);
            vertices.put(x + width).put(y + height).put(1f).put(1f);
            vertices.put(x).put(y+height).put(0f).put(1f);
            vertices.flip();

            /* Generate Vertex Buffer Object */
            vbo = new VertexBufferObject();
            vbo.bind(GL15.GL_ARRAY_BUFFER);
            vbo.uploadData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

            /* Element data */
            IntBuffer elements = stack.mallocInt(2 * 3);
            elements.put(0).put(1).put(2);
            elements.put(2).put(3).put(0);
            elements.flip();

            /* Generate Element Buffer Object */
            ebo = new VertexBufferObject();
            ebo.bind(GL30.GL_ELEMENT_ARRAY_BUFFER);
            ebo.uploadData(GL30.GL_ELEMENT_ARRAY_BUFFER, elements, GL15.GL_STATIC_DRAW);
        }

        /* Load shaders */
        vertexShader = Shader.loadShader(GL20.GL_VERTEX_SHADER, "data/vertex.glsl");
        fragmentShader = Shader.loadShader(GL20.GL_FRAGMENT_SHADER, "data/fragment.glsl");

        /* Create shader program */
        program = new ShaderProgram();
        program.attachShader(vertexShader);
        program.attachShader(fragmentShader);
        program.bindFragmentDataLocation(0, "fragColor");
        program.link();
        program.use();

        specifyVertexAttributes();

        /* Set texture uniform */
        int uniTex = program.getUniformLocation("texImage");
        program.setUniform(uniTex, 0);

        /* Set model matrix to identity matrix */
        Matrix4f model = new Matrix4f();
        int uniModel = program.getUniformLocation("model");
        program.setUniform(uniModel, model);

        /* Set view matrix to identity matrix */
        Matrix4f view = new Matrix4f();
        int uniView = program.getUniformLocation("view");
        program.setUniform(uniView, view);

        uniColor = program.getUniformLocation("color");
        program.setUniform(uniColor, color);

        /* Set projection matrix to an orthographic projection */
        Matrix4f projection = Matrix4f.orthographic(0f, 1f, 0f, 1f, -1f, 1f);
        uniProjection = program.getUniformLocation("projection");
        program.setUniform(uniProjection, projection);
    }

    public void setProjectionMatrix(Matrix4f projection){
        program.setUniform(uniProjection, projection);
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public void render(){
        vao.bind();
        program.use();
        program.setUniform(uniColor, color);

        Matrix4f model = new Matrix4f();
        program.setUniform(uniModel, model);

        GL15.glDrawElements(GL15.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0);
    }

    private void specifyVertexAttributes() {
        /* Specify Vertex Pointer */
        int posAttrib = program.getAttributeLocation("position");
        program.enableVertexAttribute(posAttrib);
        program.pointVertexAttribute(posAttrib, 2, 4 * Float.BYTES, 0);

        /* Specify Texture Pointer */
        int texAttrib = program.getAttributeLocation("texcoord");
        program.enableVertexAttribute(texAttrib);
        program.pointVertexAttribute(texAttrib, 2, 4 * Float.BYTES, 2 * Float.BYTES);
    }
}
