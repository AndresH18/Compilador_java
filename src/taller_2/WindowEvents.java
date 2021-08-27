package taller_2;

import javax.swing.*;

/**
 * <p>Interface to pass window behavior to other Objects</p>
 */
public interface WindowEvents {
    /**
     * Resizes the window to fit the elements
     *
     * @param window
     */
    void resizeWindow(JFrame window);

    /**
     * TODO: DOCUMENT
     */
    void displaySymbolsData();

    /**
     * TODO: DOCUMENT
     */
    void showTable();

}
