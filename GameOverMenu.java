import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GameOverMenu  {
   
    GameOverMenu(int collectedCoins, String highScore) {
        JFrame gameOverFrame = new JFrame("Game Over!");
        gameOverFrame.setSize(400, 300);
        gameOverFrame.setLocationRelativeTo(null);
        gameOverFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the Game Over frame
        gameOverFrame.getContentPane().setBackground(Color.BLACK);
        
        gameOverFrame.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });

        JLabel gameOverLabel = new JLabel("Game Over!", JLabel.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gameOverLabel.setForeground(Color.WHITE);

        JLabel score = new JLabel("Score: " + collectedCoins);
        score.setFont(new Font("Arial", Font.BOLD, 18));
        score.setBounds(10, 50, 300, 300);
        score.setForeground(Color.WHITE);

        JLabel hScore = new JLabel("Highscore:  " + highScore);
        hScore.setFont(new Font("Arial", Font.BOLD, 18));
        hScore.setBounds(10, 80, 300, 300);
        hScore.setForeground(Color.WHITE);

        gameOverFrame.add(gameOverLabel);
        gameOverLabel.add(score);
        gameOverLabel.add(hScore);
        gameOverFrame.setVisible(true);
    }
    
    
}
