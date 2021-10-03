package text_editor;

import java.io.File;

/**
 * <p>This Interface denotes some methods that are used to interact with the files of the computer.</p>
 */
public interface FileEvents {

    /**
     * <P>Opens the file.</P>
     *
     * @param file the file to open.
     */
    void openFile(File file);

    /**
     * <p>Saves to the file.</p>
     *
     * @param file the file to save.
     * @return true if the file was saved.
     */
    boolean saveSourceFile(File file);

    /*
    existe():
        return file != null
//    save():
//        if (existe()) {
//            saveFile();
//        } else {
//            saveDialog(null);
//        }
//    saveAs():
//        if (existe()) {
//            saveDialog(info);
//        } else {
//            saveDialog(null);
//        }
//    ejecutar(Action):
//        save();
//        ejecutar;
//    open():
//        fileChooser
//        file = selected;
//        show;
     */
}


