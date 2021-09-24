package text_editor;

import java.io.File;

/**
 * <p>Interface to interact with files.</p>
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
     * @param file the file to open.
     * @return true if the file was saved.
     */
    boolean saveSourceFile(File file);


}


