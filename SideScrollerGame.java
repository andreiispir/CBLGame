import javax.swing.*;
import java.awt.*;

public class SideScrollerGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StartMenu();
        });
    }
}
