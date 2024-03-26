/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myjavaproject;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

/**
 *
 * @author Merna Fadl
 */


public class Line extends Shape {
    private int endX, endY;

    public void setEndPosition(int x, int y) {
        endX = x;
        endY = y;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        Stroke stroke;
        if (dotted) {
            float[] dashPattern = {5, 5};
            stroke = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0);
        } else {
            stroke = new BasicStroke(1);
        }
        g2d.setStroke(stroke);
        g2d.drawLine(x, y, endX, endY);
    }

    public void setPositions(int startX, int startY, int endX, int endY) {
        this.x = startX;
        this.y = startY;
        this.endX = endX;
        this.endY = endY;
    }
}
