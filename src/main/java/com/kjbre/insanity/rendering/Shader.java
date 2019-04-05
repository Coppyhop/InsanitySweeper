package com.kjbre.insanity.rendering;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;

/**
 * This class represents a shader.
 *
 * @author Heiko Brumme
 */
public class Shader {

    /**
     * Stores the handle of the shader.
     */
    private final int id;

    /**
     * Creates a shader with specified type. The type in the tutorial should be
     * either <code>GL_VERTEX_SHADER</code> or <code>GL_FRAGMENT_SHADER</code>.
     *
     * @param type Type of the shader
     */
    public Shader(int type) {
        id = glCreateShader(type);
    }

    /**
     * Sets the source code of this shader.
     *
     * @param source GLSL Source Code for the shader
     */
    public void source(CharSequence source) {
        glShaderSource(id, source);
    }

    /** Compiles the shader and checks it's status afertwards. */
    public void compile() {
        glCompileShader(id);

        checkStatus();
    }

    /**
     * Checks if the shader was compiled successfully.
     */
    private void checkStatus() {
        int status = glGetShaderi(id, GL_COMPILE_STATUS);
        if (status != GL_TRUE) {
            throw new RuntimeException(glGetShaderInfoLog(id));
        }
    }

    /**
     * Deletes the shader.
     */
    public void delete() {
        glDeleteShader(id);
    }

    /**
     * Getter for the shader ID.
     *
     * @return Handle of this shader
     */
    public int getID() {
        return id;
    }

    /**
     * Creates a shader with specified type and source and compiles it. The type
     * in the tutorial should be either <code>GL_VERTEX_SHADER</code> or
     * <code>GL_FRAGMENT_SHADER</code>.
     *
     * @param type   Type of the shader
     * @param source Source of the shader
     *
     * @return Compiled Shader from the specified source
     */
    public static Shader createShader(int type, CharSequence source) {
        Shader shader = new Shader(type);
        shader.source(source);
        shader.compile();

        return shader;
    }

    /**
     * Loads a shader from a file.
     *
     * @param type Type of the shader
     * @param path File path of the shader
     *
     * @return Compiled Shader from specified file
     */
    public static Shader loadShader(int type, String path) {
        StringBuilder builder = new StringBuilder();

        try (InputStream in = new FileInputStream(path);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load a shader file!"
                    + System.lineSeparator() + ex.getMessage());
        }
        CharSequence source = builder.toString();

        return createShader(type, source);
    }

}