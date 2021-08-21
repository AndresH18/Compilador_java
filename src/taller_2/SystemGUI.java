package taller_2;

import taller_2.gui.MainPage;

import javax.swing.*;
import java.awt.*;

public class SystemGUI implements WindowEvents {

    private final JFrame window;

    public SystemGUI() {
        window = new JFrame("APP");

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.add(new MainPage(this).getTabbedPane());

        this.resize();

        window.setLocationRelativeTo(null);


        window.setResizable(false);

        window.setAlwaysOnTop(true);

        window.setVisible(true);

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SystemGUI();
            }
        });
    }

    @Override
    public void resize() {
        window.pack();

    }
}

