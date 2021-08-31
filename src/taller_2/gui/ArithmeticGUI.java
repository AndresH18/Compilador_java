package taller_2.gui;

import taller_2.Analyzer;
import taller_2.WindowEvents;

import javax.swing.*;

/**
 * <p><b>Graphic Interface class.</b></p>
 * <p>Manages the Arithmetic Expression window interface.</p>
 * <p>This class makes use of an intellij form to connect the GUI objects to the code</p>
 */
public class ArithmeticGUI {

    private final Analyzer analyzer;
    private final WindowEvents windowEvents;

    private JPanel mathPanel;
    private JList<String> expressionList;

    private DefaultListModel<String> listModel;

    /**
     * <p>Create an instance of the Arithmetic Expression GUI.</p>
     *
     * @param analyzer     the analyzer to use
     * @param windowEvents interface to interact with the window
     */
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
