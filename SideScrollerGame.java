import javax.swing.*;
import java.awt.*;

public class SideScrollerGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Side Scroller Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1366, 768);
            frame.setLocationRelativeTo(null);
            //GamePanel gp = new GamePanel();
            StartMenu sm = new StartMenu();
            frame.add(sm);
            frame.setVisible(true);
        });
    }
}
