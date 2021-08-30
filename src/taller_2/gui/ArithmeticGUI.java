package taller_2.gui;

import taller_2.Analyzer;
import taller_2.WindowEvents;

import javax.swing.*;

public class ArithmeticGUI {

    private final Analyzer analyzer;
    private final WindowEvents windowEvents;

    private JPanel mathPanel;
    private JList<String> expressionList;

    private DefaultListModel<String> listModel;

    public ArithmeticGUI(Analyzer analyzer, WindowEvents windowEvents) {
        this.analyzer = analyzer;
        this.windowEvents = windowEvents;

        expressionList.setModel(listModel = new DefaultListModel<>());

    }

    public DefaultListModel<String> getListModel() {
        return listModel;
    }

    public JPanel getMathPanel() {
        return mathPanel;
    }
}
