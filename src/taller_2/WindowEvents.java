package taller_2;

import javax.swing.*;

/**
 * <p>Interface to pass window behavior to other Objects</p>
 */
public interface WindowEvents {
    /**
     * Resizes the window to fit the elements
     *
     * @param window window to resize
     */
    void resizeWindow(JFrame window);

    /**
     * <p>Update the symbols data</p>
     */
    void modifySymbolsData();

    /**
     * <p>Displays the Symbols Table.</p>
     */
    void showTable();

    /**
     * <p>Displays the Arithmetic expressions.</p>
     */
    void showMath();

    /**
     * <p>Updates the arithmetic expressions found.</p>
     */
    void updateMath();

}
