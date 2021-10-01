package text_editor.gui;

import text_editor.Analyzer;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutionException;

/**
 * <p><b>Graphic Interface class.</b></p>
 * <p>Manages the Arithmetic Expression window interface.</p>
 * <p>This class makes use of an intellij form to connect the GUI objects to the code</p>
 */
public class ArithmeticGUI {
    /**
     * <p>Default list model for the list.</p>
     */
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    /**
     * <p>Panel containing the elements of this gui.</p>
     */
    private JPanel panel;
    /**
     * <p>{@link JList} of type {@link String}[] containing the elements.</p>
     */
    private JList<String> list;
    /**
     * <p>{@link String}[] containing the expressions for fast acces</p>
     */
    private String[] expressions;

    /**
     * <p>Create an instance of the Arithmetic Expression GUI.</p>
     */
    public ArithmeticGUI() {
        // set the model for the list
        list.setModel(listModel);
        // add a mouse listener to the list
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // check if there are any expressions
                if (expressions != null) {
                    // count 2 or more clicks
                    if (e.getClickCount() >= 2) {
                        /*
                         * initiate a swingWorker to check if the selected expression has been
                         * typed correctly, on a thread different to the EDT (Event Dispatch Thread)
                         */
                        (new SwingWorker<String, Void>() {
                            @Override
                            protected String doInBackground() throws Exception {
                                // return the results performed in the background
                                return Analyzer.checkExpression(list.getSelectedValue());
                            }

                            @Override
                            protected void done() {
                                try {
                                    // display the result to the user, this is done on the EDT
                                    JOptionPane.showMessageDialog(panel, get());
                                } catch (InterruptedException | ExecutionException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }).execute(); // execute the SwingWorker
                    }
                }
            }
        });
    }

    /**
     * <p>Add the {@link String}[] containing the found posible expressions to the list.</p>
     *
     * @param strings the expressions to display on the list
     */
    public void addExpressions(String[] strings) {
        // set the global variable to the strings
        expressions = strings;
        // remove all elements
        listModel.removeAllElements();
        // add elements to the list
        for (String s : strings) {
            listModel.addElement(s);
        }
        // update the gui
        SwingUtilities.updateComponentTreeUI(list);
    }

    public JPanel getPanel() {
        return panel;
    }
}
