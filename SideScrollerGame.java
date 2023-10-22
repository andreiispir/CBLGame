import javax.swing.SwingUtilities;

public class SideScrollerGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StartMenu();
        });
    }
}
