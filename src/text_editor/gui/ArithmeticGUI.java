package text_editor.gui;

import javax.swing.*;

/**
 * <p><b>Graphic Interface class.</b></p>
 * <p>Manages the Arithmetic Expression window interface.</p>
 * <p>This class makes use of an intellij form to connect the GUI objects to the code</p>
 */
public class ArithmeticGUI {

    private JPanel panel;
    private JList<String> list;

    private final DefaultListModel<String> listModel = new DefaultListModel<>();

    /**
     * <p>Create an instance of the Arithmetic Expression GUI.</p>
     */
    public ArithmeticGUI() {

        list.setModel(listModel);

    }

    public DefaultListModel<String> getListModel() {
        return listModel;
    }

    public JPanel getPanel() {
        return panel;
    }

    public JList<String> getList() {
        return list;
    }
}
