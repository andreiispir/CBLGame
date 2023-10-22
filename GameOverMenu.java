import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRootPane;

public class GameOverMenu  {
   
    protected Image closeBckgr = new ImageIcon("src\\closeButton.png").getImage();
    protected Image gameOverBkg = new ImageIcon("src\\gameOverBkg.png").getImage();

    GameOverMenu(int collectedCoins, String highScore) {
        JFrame gameOverFrame = new JFrame("Game Over!");
        gameOverFrame.setSize(500, 300);
        gameOverFrame.setLocationRelativeTo(null);
        gameOverFrame.getContentPane().setBackground(Color.BLACK);
        gameOverFrame.setUndecorated(true);
        gameOverFrame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        gameOverFrame.setShape(new RoundRectangle2D.Double(0, 0, 500, 300, 50, 50));
        
        gameOverFrame.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });

        JLabel gameOverLabel = new JLabel("Game Over!", JLabel.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(gameOverBkg, 0, 0, getWidth(), getHeight(), this);
                }
            };

        gameOverLabel.setFont(new Font("Algerian", Font.BOLD, 24));
        gameOverLabel.setForeground(Color.WHITE);

        JLabel score = new JLabel(String.valueOf(collectedCoins));
        score.setFont(new Font("Algerian", Font.BOLD, 18));
        score.setForeground(new Color(58, 59, 81));
        score.setBounds(125, 24, 300, 300);

        JLabel hScore = new JLabel(highScore);
        hScore.setFont(new Font("Algerian", Font.BOLD, 18));
        hScore.setForeground(new Color(58, 59, 81));
        hScore.setBounds(165, 54, 300, 300);

        JButton retryButton = new JButton("Retry") {
            // @Override
            // protected void paintComponent(Graphics g) {
            //     super.paintComponent(g);
            //     //g.drawImage(retryImg, 0, 0, getWidth(), getHeight(), this);
            // }
        };

        // retryButton.setOpaque(false);
        // retryButton.setContentAreaFilled(false);
        // retryButton.setBorderPainted(false);

        retryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new GamePanel().setVisible(true);
            }
        });

        retryButton.setBounds(250, 100, 50, 50);
        gameOverLabel.add(retryButton);

        JButton closeButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(closeBckgr, 0, 0, getWidth(), getHeight(), this);
            }
        };

        closeButton.setOpaque(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        closeButton.setBounds(450, 10, 30, 30);
        gameOverLabel.add(closeButton);

        gameOverFrame.add(gameOverLabel);
        gameOverLabel.add(score);
        gameOverLabel.add(hScore);
        gameOverFrame.setVisible(true);
    }
    
    
}
