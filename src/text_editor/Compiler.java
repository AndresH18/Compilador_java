package text_editor;

import text_editor.gui.ArithmeticGUI;
import text_editor.gui.EditorGUI;
import text_editor.gui.TableGUI;
import text_editor.old.SystemGUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.ExecutionException;


/**
 * FIXME: delete comment
 * This class is the equivalent to {@link SystemGUI}
 *
 * @see text_editor.FileEvents
 * @see WindowEvents
 */

public class Compiler implements FileEvents {

    /**
     * <p>Language name.</p>
     */
    public static final String LANGUAGE_NAME = "AlejiNdres";

    /**
     * <p>Language extension.</p>
     * <p><i>extension = aa</i></p>
     */
    public static final String LANGUAGE_EXTENSION = "txt";

    /**
     * <p>Project path.</p>
     */
    public static final String PROJECT_DIRECTORY = System.getProperty("user.dir") + File.separatorChar;

    /**
     * <p>Data directory path.</p>
     */
    public static final String DATA_DIRECTORY = PROJECT_DIRECTORY + "data" + File.separatorChar;

    /**
     * <p>Output directory path.</p>
     */
    public static final String OUTPUT_DIRECTORY = PROJECT_DIRECTORY + "output" + File.separatorChar;

    /**
     * <p>Input directory path.</p>
     */
    public static final String INPUT_DIRECTORY = PROJECT_DIRECTORY + "input" + File.separatorChar;

    /**
     * <p>Log file path.</p>
     */
    @Deprecated
    private static final String LOG_FILE = OUTPUT_DIRECTORY + "log.txt";

    /**
     * <p>Symbols text file name.</p>
     */
    public static final String SYMBOLS_TXT_FILE = "symbols.txt";

    /**
     * <p>Symbols' comment identifier.</p>
     */
    public static final String SYMBOL_COMMENT = "##";

    /**
     * <p>Subset of {@link ColumnType} containing the allowed keys. </p>
     */
    private static final EnumSet<ColumnType> SYMBOLS_KEYS = EnumSet.range(ColumnType.TOKEN, ColumnType.TYPE2);

    /**
     * <p>{@code Map<String, Map<ColumnType, String>> } to containing the symbols.</p>
     */
    public static final Map<String, Map<ColumnType, String>> SYMBOLS;

    // static initializer to load symbols
    static {
        SYMBOLS = loadSymbols();
    }

    private File file;


    private final JFrame frame;
    private final EditorGUI editor = new EditorGUI();
    private final TableGUI table = new TableGUI();
    private final ArithmeticGUI math = new ArithmeticGUI();
/*
    private final MainGUI mainGUI = new MainGUI(this);

     TODO: Decide how to use
    private JFrame mathFrame;
    private JFrame tableFrame;
    private final TableGUI tableGUI = new TableGUI(new Analyzer(), this);
    private ArithmeticGUI arithmeticGUI = new ArithmeticGUI(null, this);
*/

    /**
     * <p>Program entry point</p>
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(Compiler::new);
    }

    public Compiler() {
        frame = new JFrame(Compiler.LANGUAGE_NAME + " Compiler");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        System.out.println("i\tframe's layout class: " + frame.getLayout().getClass().toString());

        if (frame.getLayout() instanceof BorderLayout b) {
            System.out.println("i\tframe border layout: Horizontal gap: " + b.getHgap());
            System.out.println("i\tframe border layout: Vertical gap: " + b.getVgap());
            b.setHgap(b.getHgap());
            b.setVgap(b.getVgap());
        }
        frame.setJMenuBar(createMenuBarItems());

        frame.add(editor.getPanel(), BorderLayout.CENTER);
//        frame.pack();
        // FIXME: MAXIMIZED_BOTH
        frame.setExtendedState(JFrame.NORMAL);
        frame.setMinimumSize(new Dimension(700, 600));
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /**
     * <p>Creates the {@code JMenuBar} and adds {@code JMenu} with {@code JMenuItem}.</p>
     *
     * @return the created {@code JMenuBar}
     */
    private JMenuBar createMenuBarItems() {
        final JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;

        menuBar = new JMenuBar();

        /* FILE */
        menu = new JMenu("File");
//        menu.setMnemonic(KeyEvent.VK_A);
        // NEW
        menuItem = new JMenuItem("New");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
/*
                here
                normalView();
                editor.getText().setText("");
                editor.setEdited(false);
                file = null;
*/
            }
        });
        menu.add(menuItem);
        // OPEN
        menuItem = new JMenuItem("Open");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openDialog();
            }
        });
        menu.add(menuItem);
        // SAVE
        menuItem = new JMenuItem("Save");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
                /**
                 * TODO: You need to change every method that saves the file.
                 *  Save As -> will always ask how to save the file (display dialog)
                 *  Save -> checks somehow if the file exists. if it exists, it saves; otherwise to the same as "Save As."
                 *  AutoSave -> when you need to save. For now: lexicalAnalysis, findMathExpressions, new.
                 *  new -> checks if there is any need to save, then resets all posible data, maybe close the program and run it again?
                 *
                 */
            }
        });
        menu.add(menuItem);
        // SAVE AS
        menuItem = new JMenuItem("Save As...");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAs();
            }
        });
        menu.add(menuItem);
        // add menu to menuBar
        menuBar.add(menu);

        /* VIEW */
        menu = new JMenu("View");
        // NORMAL VIEW
        menuItem = new JMenuItem("Normal View");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notYet();
                // TODO:
            }
        });
        menu.add(menuItem);
        // separator
        menu.addSeparator();
        // DISPLAY CODE
        menuItem = new JMenuItem("Display Code");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: implement display code
                notYet();
            }
        });
        menu.add(menuItem);
        // SYMBOLS TABLE
        menuItem = new JMenuItem("Symbols Table");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                symbolsView();
            }
        });
        menu.add(menuItem);
        // ARITHMETIC EXPRESSIONS
        menuItem = new JMenuItem("Arithmetic Expressions");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mathView();
            }
        });
        menu.add(menuItem);
        // add menu to menuBar
        menuBar.add(menu);

        /* CODE */
        menu = new JMenu("Code");
        // COMPILE
        menuItem = new JMenuItem("Compile");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notYet();
            }
        });
        menu.add(menuItem);
        // COMPILE AND RUN
        menuItem = new JMenuItem("Compile and Run");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notYet();
            }
        });
        menu.add(menuItem);
        // FORMAT
        menuItem = new JMenuItem("Format");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notYet();
            }
        });
        menu.add(menuItem);
        // separator
        menu.addSeparator();
        // LEXICAL ANALYSIS
        menuItem = new JMenuItem("Lexical Analysis");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (save()) {
                    lexicalAnalysis();
                }
            }
        });
        menu.add(menuItem);
        // Find Arithmetic Expressions
        menuItem = new JMenuItem("Arithmetic");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (save()) {
                    arithmeticAnalysis();
                }
            }
        });
        menu.add(menuItem);
        // add menu to menuBar
        menuBar.add(menu);

        return menuBar;
    }

    private void lexicalAnalysis() {
        symbolsView();
        // TODO: implementing, add debug comments
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        frame.add(progressBar, BorderLayout.PAGE_END);
        frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        editor.getText().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        var w = new Background<List<String[]>, Void>(file) {
            @Override
            protected List<String[]> doInBackground() throws Exception {
                System.out.println(mFile);
                Thread.sleep(500);
                return Analyzer.generateTable(mFile);
            }

            @Override
            protected void done() {
                try {
                    var d = get();
                    // get tableModel
                    var m = table.getTableModel();
                    // remove items
                    m.setRowCount(0);
                    // add all items of the analyzed file
                    for (String[] strings : Objects.requireNonNull(d)) {
                        m.addRow(strings);
                    }
                    // notify tableModel data has changed
                    m.fireTableDataChanged();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } finally {
                    frame.remove(progressBar);
                    frame.setCursor(Cursor.getDefaultCursor());
                    editor.getText().setCursor(Cursor.getDefaultCursor());
                    frame.pack();
                    SwingUtilities.updateComponentTreeUI(frame);
                }
            }
        };
        w.execute();
    }

    private void symbolsView() {
        frame.getContentPane().removeAll();
        frame.add(editor.getPanel(), BorderLayout.LINE_START);
        frame.add(table.getPanel(), BorderLayout.LINE_END);
        /* TODO: Determine if window is going to be maximized all the time or not */
        frame.pack();
        frame.setLocationRelativeTo(null);
        /* */
        SwingUtilities.updateComponentTreeUI(frame);
    }

    private void arithmeticAnalysis() {
        mathView();
        saveAs();
        // TODO: implementing, add debug comments
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        frame.add(progressBar, BorderLayout.PAGE_END);
        frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        math.getList().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        editor.getText().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        var w = new Background<String[], Void>(file) {
            @Override
            protected String[] doInBackground() throws Exception {
                Thread.sleep(500);
                return Analyzer.findMathExpressions(mFile);
            }

            @Override
            protected void done() {
                try {
                    var m = math.getListModel();
                    m.removeAllElements();
                    for (String s : get()) {
                        m.addElement(s);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } finally {
                    frame.remove(progressBar);
                    frame.setCursor(Cursor.getDefaultCursor());
                    math.getList().setCursor(Cursor.getDefaultCursor());
                    editor.getText().setCursor(Cursor.getDefaultCursor());
                    frame.pack();
                    SwingUtilities.updateComponentTreeUI(frame);
                }
            }
        };
        w.execute();
    }

    private void mathView() {
        frame.getContentPane().removeAll();
        frame.add(editor.getPanel(), BorderLayout.LINE_START);
        frame.add(math.getPanel(), BorderLayout.LINE_END);
        /* TODO: Determine if window is going to be maximized all the time or not */
        frame.pack();
        frame.setLocationRelativeTo(null);
        /* */
        SwingUtilities.updateComponentTreeUI(frame);
    }

    private void normalView() {

        frame.getContentPane().removeAll();
        frame.add(editor.getPanel(), BorderLayout.CENTER);
        /* TODO: Determine if window is going to be maximized all the time or not */
        frame.pack();
        frame.setLocationRelativeTo(null);
        /* */
        SwingUtilities.updateComponentTreeUI(frame);

    }


    /**
     * <p>Displays the open file dialog.</p>
     */
    private void openDialog() {
        // check for directory
        String path = file == null ?
                Compiler.INPUT_DIRECTORY :
                file.getParentFile().getAbsolutePath();
        // initialize file chooser
        JFileChooser fileChooser = new JFileChooser(path);
        // configure file chooser
        // set accepting type
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // set file filter
        fileChooser.setFileFilter(new FileNameExtensionFilter(Compiler.LANGUAGE_NAME + " file", Compiler.LANGUAGE_EXTENSION));
        // show Open dialog and check for approved option
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            // set the file to the selected
            file = fileChooser.getSelectedFile();

            System.out.println("d\tFile to open: " + file.getAbsolutePath());

            // open file
            openFile(file);
        }
    }

    /**
     * <p>Opens the file and adds the text to the text editor.</p>
     *
     * @param file the file to open.
     */
    @Override
    public void openFile(File file) {
        // checks if there is content on the editor
        if (!editor.getText().getText().isBlank()) {
            // save content
            save();
        }
        // check if the file is null or if it doesn't exist
        if (file == null || !file.exists()) {
            throw new RuntimeException("File should exist and not be null");
        }
        // check that the editor is not null, set the file content to the editor.
        Objects.requireNonNull(editor).setText(file);
        /*

        SwingWorker<String[], Void> work = new SwingWorker<String[], Void>() {
            @Override
            protected String[] doInBackground() throws Exception {
                try (BufferedReader in = new BufferedReader(new FileReader(file))) {
                    List<String> l = new LinkedList<>();
                    String s;
                    while ((s = in.readLine()) != null) {
                        l.add(s);
                    }

                    return l.toArray(String[]::new);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.err.println("returning null");
                return null;
            }

            @Override
            protected void done() {
                try {
                    String[] t = get();
                    if (t != null) {

                        for (String s : t) {
                            editor.write(s, true);
                        }
                    } else {
                        System.err.println("NULL STRING");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        work.execute();
*/
    }

    /**
     * <p>Saves the file.</p>
     * <p>If no file has been used, this will show a Save Dialog; Otherwise it will save the file.</p>
     */
    private boolean save() {
        // check if file exists, sort of
        if (file != null) {
            return saveSourceFile(file);
        } else {

            if (saveDialog(null)) {
                // inform the user that the file was saved
                JOptionPane.showMessageDialog(frame, "Save Successful!");
                return true;
            } else {
                // inform the user that there was an error saving the file
                JOptionPane.showMessageDialog(frame, "Something went wrong.", "Error", JOptionPane.ERROR_MESSAGE);
                Toolkit.getDefaultToolkit().beep();
                return false;
            }
        }
    }

    /**
     * <p>Save file as.</p>
     * <p>This method will ask the user where to save the file.</p>
     */
    private void saveAs() {
        if (file != null) {
            saveDialog(file);
        } else {
            saveDialog(null);
        }
    }

    /**
     * <p>Opens a save dialog and saves the text on the editor into the selected file.</p>
     */
    private boolean saveDialog(Object data) {
        String path = file == null ?
                Compiler.INPUT_DIRECTORY :
                file.getParentFile().getAbsolutePath();
        // initialize the FileChooser to the default location or the location of the last file
        JFileChooser fileChooser = new JFileChooser(path);
        // configure the FileChooser to filter only compiler language files
        fileChooser.setFileFilter(new FileNameExtensionFilter(Compiler.LANGUAGE_NAME + " files", Compiler.LANGUAGE_EXTENSION));
        // show save dialog
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            System.out.println("D\tfileChooser selected file to Save: " + fileChooser.getSelectedFile().getAbsolutePath());

            // check if the selected file ends with the language extension, add if needed
            path = fileChooser.getSelectedFile().getAbsolutePath().endsWith("." + LANGUAGE_EXTENSION) ?
                    fileChooser.getSelectedFile().getAbsolutePath() :
                    fileChooser.getSelectedFile().getAbsolutePath() + "." + LANGUAGE_EXTENSION;

            // save the file and assign it to the global variable
            if (saveSourceFile(file = new File(path))) {
                System.out.println("D\tThe file was saved successfully.");
                return true;
            } else {
                System.out.println("E\tthe file could not be opened.");

            }
        }
        return false;
    }


    /**
     * <p>Saves the file.</p>
     *
     * @param file the file to Save.
     * @return true if the file was saved.
     */
    @Override
    public boolean saveSourceFile(File file) {
        // require file to not be null
        Objects.requireNonNull(file);

        System.out.println("D\tSaving file: " + file.getAbsolutePath());
        // save the file
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            // print to the file the contents of the editor
            out.println(editor.gettext());

            System.out.println("D\tFile was saved.");

            // tell editor that the file has been saved
            editor.setEdited(false);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * <p><b>This method is used as a place holder.</b></p>
     */
    @Deprecated
    private void notYet() {
        JOptionPane.showMessageDialog(frame, "This feature has not been implemented yet or is missing.\nTry again in the future.", "FEATURE NOT FOUND", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * <p>Map the header with a map of characteristic.<br>
     * It's similar to a .json data map</p>
     * <p><b>The Map is inmodificable.</b></p>
     *
     * @return a map containing the symbols
     */
    private static Map<String, Map<ColumnType, String>> loadSymbols() {
        File file = new File(DATA_DIRECTORY + SYMBOLS_TXT_FILE);
        // long to count the lines in the file to address syntax errors
        long lineCounter = 0x01;
        // strings to store the header, key and value
        String header, key, value;
        // Map to contain the headers and characteristic maps
        Map<String, Map<ColumnType, String>> head = new HashMap<>();
        // Map to contain the characteristics of the header
        Map<ColumnType, String> body;

        // read the file
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {

            // string to store the read lines, starting from the first line
            String line = in.readLine();

            // loop to navigate the file lines
            while (line != null) {
                // loop to skip blank lines
                while (line != null && line.isBlank()) {
                    // read next line
                    line = in.readLine();
                    lineCounter++;
                }
                // check if it is not the end of the file
                if (line != null) {
                    // if line start with the comment symbol, skip
                    if (!line.startsWith(SYMBOL_COMMENT)) {
                        // stores the header
                        header = line.replaceAll(" ", "");
                        // read next line
                        line = in.readLine();
                        lineCounter++;
                        // initialize the characteristics map
                        body = new HashMap<>();
                        // loop to map the key value pairs
                        // checks if is not the end of the file and that the line is not blank
                        while (line != null && !line.isBlank()) {
                            // !(line == null || line.isBlank())
                            // remove empty spaces form line
                            line = line.replaceAll(" ", "");
                            // check that line contains ":" in order to separate
                            if (line.contains(":")) {
                                // stores the part before the ":"
                                key = line.split(":")[0];
                                // stores the part after the ":"
                                value = line.split(":")[1];
                                // checks if the "key" belongs to the set of valid keys,
                                // while also checking if that "key" is also an element of ColumnType
                                if (!SYMBOLS_KEYS.contains(ColumnType.valueOf(key))) {
                                    // the key is element of ColumnType but is not part of allowed keys
                                    throw new IllegalArgumentException();
                                }
                                // maps the key value pair
                                body.put(ColumnType.valueOf(key), value);
                                key = value = "";
                            }
                            // read next line
                            line = in.readLine();
                            lineCounter++;
                        }
                        // maps the header as the key and the characteristics map as the value
                        head.put(header, Collections.unmodifiableMap(body));
                    } else {
                        // read next line, to skip comment
                        line = in.readLine();
                        lineCounter++;
                    }
                }
            }
            // return the map, unmodifiable
            return Collections.unmodifiableMap(head);

        } catch (IOException e) {
            // prints the Exception to the standard error stream
            e.printStackTrace(System.err);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("ILLEGAL KEY ON LINE " + lineCounter);
        }
        throw new RuntimeException("Symbols file not found");
    }
}

abstract class Background<T, V> extends SwingWorker<T, V> {
    protected final File mFile;

    public Background(File file) {
        this.mFile = file;
    }
}
