package com.kjbre.insanity.engine;

import com.kjbre.insanity.rendering.Quad;
import com.kjbre.insanity.tools.Vector3f;

public class Rectangle {

    float x, y, width, height;
    Vector3f color = new Vector3f(1,1,1);
    public Rectangle(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setColor(Vector3f color){
        this.color = color;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Quad toQuad(){
        Quad quad = new Quad();
        quad.init(x,y,width,height);
        quad.setColor(color);
        return quad;
    }

}
