package text_editor;

import text_editor.gui.EditorGUI;
import text_editor.old.ColumnType;
import text_editor.old.SystemGUI;
import text_editor.old.WindowEvents;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;


/**
 * FIXME: delete comment
 * This class is the equivalent to {@link SystemGUI}
 */

public class Compiler implements FileEvents, WindowEvents {

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
        menuBar.add(menu);

        menuItem = new JMenuItem("New");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notYet();
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Open");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                open();
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Save");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        menu.add(menuItem);

        /* VIEW */
        menu = new JMenu("View");
        menuItem = new JMenuItem("Display Code");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: implement display code
                notYet();
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Symbols Table");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTable();
                notYet();
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem("Arithmetic Expressions");
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMath();
                notYet();
            }
        });
        menu.add(menuItem);

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
                if (editor.isEdited()) {
                    saveSourceFile(file);
                }
                lexicalAnalysis();
            }
        });
        menu.add(menuItem);

        menuBar.add(menu);


        return menuBar;
    }

    private void lexicalAnalysis() {
        //TODO IMPLEMENTING
    }


    private void open() {
        // initialize a file choose
        JFileChooser fileChooser = new JFileChooser(file == null ?
                Compiler.PROJECT_DIRECTORY :
                file.getParentFile().getAbsolutePath());
        // Configures the file chooser
        // set accepting type
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // set accepting files
        fileChooser.setFileFilter(new FileNameExtensionFilter(Compiler.LANGUAGE_NAME + " file", Compiler.LANGUAGE_EXTENSION));
        // show openDialog and check for approved option
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            // sets the file to the selected file
            file = fileChooser.getSelectedFile();

            System.out.println("D\tFile to open: " + file.getAbsolutePath());
            // open file to editor
            openFile(file);
        }
    }


    /**
     * <p>Opens a save dialog and saves the text on the editor into the selected file.</p>
     */
    private void save() {
        // initialize the FileChooser to the default location or the location of the last file
        JFileChooser fileChooser = new JFileChooser(file != null ? file.getParentFile().getAbsolutePath() : Compiler.INPUT_DIRECTORY);
        // configure the FileChooser to filter only compiler language files
        fileChooser.setFileFilter(new FileNameExtensionFilter(LANGUAGE_NAME + " files", LANGUAGE_EXTENSION));
        // TODO
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            System.out.println("D\tfileChooser selected file to Save: " + fileChooser.getSelectedFile().getAbsolutePath());


            String path = fileChooser.getSelectedFile().getAbsolutePath().endsWith("." + LANGUAGE_EXTENSION) ?
                    fileChooser.getSelectedFile().getAbsolutePath() :
                    fileChooser.getSelectedFile().getAbsolutePath() + "." + LANGUAGE_EXTENSION;

            if (saveSourceFile(file = new File(path))) {
                // tell editor that the file has been saved
                editor.setEdited(false);
                JOptionPane.showMessageDialog(frame, "Save Successful!");
            } else {
                JOptionPane.showMessageDialog(frame, "Something went wrong.", "Error", JOptionPane.ERROR_MESSAGE);
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    @Override
    public boolean saveSourceFile(File file) {
        Objects.requireNonNull(file);

        System.out.println("D\tSaving file: " + file.getAbsolutePath());
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {

            out.println(editor.gettext());

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public void openFile(File file) {
        if (file == null || !file.exists()) {
            throw new RuntimeException("File to open should exist.");
        }
        editor.setText(file);
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

    /*
     * TODO: IMPLEMENT WINDOW EVENTS
     */
    @Override
    public void resizeWindow(JFrame window) {

    }

    @Override
    public void modifySymbolsData() {

    }

    @Override
    public void showTable() {

    }

    @Override
    public void showMath() {

    }

    @Override
    public void updateMath() {

    }
}
