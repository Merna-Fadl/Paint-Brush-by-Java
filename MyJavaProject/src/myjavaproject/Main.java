/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myjavaproject;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author Merna Fadl
 */
public class Main {
        public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Drawing Shapes");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new MoreLine());
            frame.setSize(1000, 400);
            frame.setVisible(true);
        });
    }
}

    

