/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myjavaproject;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 *
 * @author Merna Fadl
 */


public class MoreLine extends JPanel implements MouseListener, MouseMotionListener {
    private ArrayList<Shape> shapes = new ArrayList<>();
    private Stack<Shape> undoStack = new Stack<>();
    private Shape currentShape;
    private int startX, startY, currentX, currentY;
    private JButton rectangleButton, ovalButton, lineButton, freehandButton, blackButton, greenButton, redButton, blueButton, eraserButton, clearAllButton, openButton, saveButton;
    private int drawingMode = -1;
    private Color selectedColor = Color.BLACK;
    private Freehand currentFreehand;
    private boolean eraserMode = false;
    private JCheckBox dottedCheckbox;
    private JCheckBox filledCheckbox;

    public MoreLine() {
        addMouseMotionListener(this);
        addMouseListener(this);
        setBackground(Color.WHITE);

        rectangleButton = new JButton("Rectangle");
        rectangleButton.addActionListener(e -> drawingMode = 0);
        rectangleButton.setFocusable(true);

        ovalButton = new JButton("Oval");
        ovalButton.addActionListener(e -> drawingMode = 1);
        ovalButton.setFocusable(true);

        lineButton = new JButton("Line");
        lineButton.addActionListener(e -> drawingMode = 2);
        lineButton.setFocusable(true);

        freehandButton = new JButton("Freehand");
        freehandButton.addActionListener(e -> drawingMode = 3);
        freehandButton.setFocusable(true);

        greenButton = new JButton("Green");
        greenButton.addActionListener(e -> selectedColor = Color.GREEN);
        greenButton.setFocusable(true);
        
        blackButton = new JButton("Black");
        blackButton.addActionListener(e -> selectedColor = Color.BLACK);
        blackButton.setFocusable(true);

        redButton = new JButton("Red");
        redButton.addActionListener(e -> selectedColor = Color.RED);
        redButton.setFocusable(true);

        blueButton = new JButton("Blue");
        blueButton.addActionListener(e -> selectedColor = Color.BLUE);
        blueButton.setFocusable(true);

        eraserButton = new JButton("Eraser");
        eraserButton.addActionListener(e -> {
            eraserMode = true;
            selectedColor = getBackground();
        });
        eraserButton.setFocusable(true);

        clearAllButton = new JButton("Clear All");
        clearAllButton.addActionListener(e -> {
            for (Shape shape : shapes) {
                undoStack.push(shape);
            }
            shapes.clear();
            repaint();
        });
        clearAllButton.setFocusable(true);

        dottedCheckbox = new JCheckBox("Dotted Line");
        dottedCheckbox.addItemListener(e -> {
            if (currentShape != null) {
                currentShape.setDotted(dottedCheckbox.isSelected());
                repaint();
            }
        });

        filledCheckbox = new JCheckBox("Filled Shape");
        filledCheckbox.addItemListener(e -> {
            if (currentShape != null) {
                currentShape.setFilled(filledCheckbox.isSelected());
                repaint();
            }
        });

        openButton = new JButton("Open");
        openButton.addActionListener(e -> openFile());
        openButton.setFocusable(true);

        saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
                    paint(image.getGraphics());
                    ImageIO.write(image, "PNG", selectedFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        saveButton.setFocusable(true);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(rectangleButton);
        buttonPanel.add(ovalButton);
        buttonPanel.add(lineButton);
        buttonPanel.add(freehandButton);
        buttonPanel.add(greenButton);
        buttonPanel.add(blackButton);
        buttonPanel.add(redButton);
        buttonPanel.add(blueButton);
        buttonPanel.add(eraserButton);
        buttonPanel.add(clearAllButton);
        buttonPanel.add(dottedCheckbox);
        buttonPanel.add(filledCheckbox);
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);

        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> undo());
        undoButton.setFocusable(true);

        buttonPanel.add(undoButton);

        add(buttonPanel, BorderLayout.NORTH);
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                BufferedImage image = ImageIO.read(selectedFile);
                Graphics2D g2d = (Graphics2D) getGraphics();
                int width = image.getWidth();
                int height = image.getHeight();
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                double aspectRatio = (double) width / height;
                int newWidth, newHeight;
                if (width > panelWidth || height > panelHeight) {
                    if (aspectRatio > 1) {
                        newWidth = panelWidth / 2;
                        newHeight = (int) (newWidth / aspectRatio);
                    } else {
                        newHeight = panelHeight / 2;
                        newWidth = (int) (newHeight * aspectRatio);
                    }
                } else {
                    newWidth = width;
                    newHeight = height;
                }
                g2d.drawImage(image, (panelWidth - newWidth) / 2, (panelHeight - newHeight) / 2, newWidth, newHeight, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void undo() {
        if (!undoStack.isEmpty()) {
            shapes.add(undoStack.pop());
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Shape shape : shapes) {
            shape.draw(g);
        }
        if (currentShape != null) {
            currentShape.draw(g);
        }
        if (currentFreehand != null) {
            currentFreehand.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        startX = e.getX();
        startY = e.getY();
        if (drawingMode == 0) {
            currentShape = new Rectangle();
            currentShape.setStartPosition(startX, startY);
            currentShape.setColor(selectedColor);
            currentShape.setDotted(dottedCheckbox.isSelected());
            currentShape.setFilled(filledCheckbox.isSelected());
        } else if (drawingMode == 1) {
            currentShape = new Oval();
            currentShape.setStartPosition(startX, startY);
            currentShape.setColor(selectedColor);
            currentShape.setDotted(dottedCheckbox.isSelected());
            currentShape.setFilled(filledCheckbox.isSelected());
        } else if (drawingMode == 2) {
            currentShape = new Line();
            currentShape.setStartPosition(startX, startY);
            currentShape.setColor(selectedColor);
            currentShape.setDotted(dottedCheckbox.isSelected());
        } else if (drawingMode == 3) {
            currentFreehand = new Freehand();
            currentFreehand.setColor(selectedColor);
            currentFreehand.setStartPosition(startX, startY);
            currentFreehand.addPoint(new Point(startX, startY));
            currentFreehand.setDotted(dottedCheckbox.isSelected());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (currentShape != null) {
            currentX = e.getX();
            currentY = e.getY();
            if (drawingMode == 2) {
                ((Line) currentShape).setPositions(startX, startY, currentX, currentY);
            } else {
                currentShape.setSize(currentX - startX, currentY - startY);
            }
            shapes.add(currentShape);
            undoStack.push(currentShape);
            currentShape = null;
            repaint();
        } else if (currentFreehand != null) {
            shapes.add(currentFreehand);
            undoStack.push(currentFreehand);
            currentFreehand = null;
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        if (currentShape != null) {
            currentX = e.getX();
            currentY = e.getY();
            if (drawingMode == 2) {
                ((Line) currentShape).setPositions(startX, startY, currentX, currentY);
            } else {
                currentShape.setSize(currentX - startX, currentY - startY);
            }
            repaint();
        } else if (currentFreehand != null) {
            currentX = e.getX();
            currentY = e.getY();
            currentFreehand.addPoint(new Point(currentX, currentY));
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

}