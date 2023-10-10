import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class GameOver {
    GameOver() {
        JFrame gameOverFrame = new JFrame("Game Over");
        gameOverFrame.setSize(200, 100);
        gameOverFrame.setLocationRelativeTo(null);
        gameOverFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the Game Over frame

        JLabel gameOverLabel = new JLabel("Game Over", JLabel.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 24));

        gameOverFrame.add(gameOverLabel);
        gameOverFrame.setVisible(true);
    }
}
