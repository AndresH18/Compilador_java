package text_editor.gui;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class EditorGUI implements DocumentListener, CaretListener {

    private JPanel panel;
    private JEditorPane text;

    private boolean edited = false;


    public EditorGUI() {

        text.getDocument().addDocumentListener(this);
        text.addCaretListener(this);

    }

    public String gettext() {
        return text.getText();
    }

    public final void setText(File file) {
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            text.getDocument().removeDocumentListener(this);
            text.setPage(file.toURI().toURL());
            text.getDocument().addDocumentListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
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

    public JPanel getPanel() {
        return panel;
    }


    @Override
    public void insertUpdate(DocumentEvent e) {
        System.out.println("inserted something" + e.getOffset());
        edited = true;
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        System.out.println("removed something");
        edited = true;
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        System.out.println("changed something");
        edited = true;
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        System.out.println("Caret Position Changed");
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }
}
