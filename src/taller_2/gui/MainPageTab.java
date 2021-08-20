package taller_2.gui;

import taller_1.Sistema;
import taller_2.WindowEvents;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPageTab {

    private final WindowEvents windowEvents;
    private final JFileChooser fileChooser;

    private JTabbedPane tabbedPane;
    private JPanel firstPanel;
    private JPanel secondPanel;
    private JLabel file_lbl;
    private JTextField fileName_txf;
    private JButton search_btn;
    private JButton analyze_btn;


    private java.io.File file;

    {
        this.fileChooser = new JFileChooser(Sistema.PROJECT_DIRECTORY);
        this.fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.fileChooser.setFileFilter(new FileNameExtensionFilter(Sistema.LANGUAGE_NAME + " file", Sistema.LANGUAGE_EXTENSION));
    }

    {
        search_btn.setIcon(UIManager.getIcon("Tree.openIcon"));
    }

    public MainPageTab() {
        this(null);
    }

    public MainPageTab(WindowEvents windowEvents) {
        this.windowEvents = windowEvents;


        search_btn.addActionListener(e -> {

            if (fileChooser.showOpenDialog(this.firstPanel) == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                fileName_txf.setText(file.getName());
            }
            resizeWindow();

        });

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // TODO
            }
        });

        analyze_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void resizeWindow() {
        if (windowEvents != null) {
            windowEvents.resize();
        }
    }


    public JPanel getFirstPanel() {
        return firstPanel;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public JPanel getSecondPanel() {
        return secondPanel;
    }
}
