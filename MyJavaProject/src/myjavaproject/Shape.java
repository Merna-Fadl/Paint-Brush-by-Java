/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myjavaproject;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Merna Fadl
 */


public class Shape {
    protected int x, y, width, height;
    protected Color color = Color.BLACK;
    protected boolean dotted = false;
    protected boolean filled = false;

    public void setStartPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setDotted(boolean dotted) {
        this.dotted = dotted;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public void draw(Graphics g) {
        // This method will be overridden by subclasses
    }
}
