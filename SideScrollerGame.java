import javax.swing.SwingUtilities;

/**
 * This is the main class of the Game used for instantiating the StartMenu.
 */
public class SideScrollerGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StartMenu();
        });
    }
}