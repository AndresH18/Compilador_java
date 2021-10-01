package text_editor.gui;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * <p>This class contains the information related to the management of the editor panel.</p>
 *
 * @see text_editor.Compiler
 */
public class EditorGUI implements DocumentListener, CaretListener {
    /**
     * <p>{@link JPanel} containing the elements.</p>
     */
    private JPanel panel;
    /**
     * <p>{@link JEditorPane} for text input</p>
     */
    private JEditorPane text;

    /**
     * <P>Class constructor.</P>
     */
    public EditorGUI() {
        // set border with title
        panel.setBorder(BorderFactory.createTitledBorder("Editor"));
        // add listener to the document on the JEditorPane
        text.getDocument().addDocumentListener(this);
        // add caret listener to the JEditorPane
        text.addCaretListener(this);
        // setting font manually to avoid it changing when the panel changes
        text.setFont(new Font("Consolas", Font.PLAIN, 15));
    }

    /**
     * <p>Set the text from the file to the {@link JEditorPane}.</p>
     *
     * @param file file containing the text
     */
    public final void setTextFromFile(File file) {
        // remove the current text
        text.setText(null);
        // read file
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            // string to store the read lines
            String s;
            // stringBuilder to concatenate the strings
            StringBuilder sb = new StringBuilder();
            // read the lines and append them to the stringBuilder
            while ((s = in.readLine()) != null) {
                // append the lines with a line break at the end
                sb.append(s).append("\n");
            }
            // set the text of the JEditorPane to the concatenated Strings
            text.setText(sb.toString());
            // update the gui
            SwingUtilities.updateComponentTreeUI(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Deprecated(forRemoval = true)
    public void write(String s, boolean append) {
        if (!append) {
            text.setText("");
        }
        try {
            text.getDocument().insertString(text.getDocument().getLength(), s, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        System.out.println("inserted something" + e.getOffset());
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        System.out.println("removed something");
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        System.out.println("changed something");
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        System.out.println("Caret Position Changed");
    }

    public JEditorPane getText() {
        return text;
    }

    public JPanel getPanel() {
        return panel;
    }
}
