import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class StartMenu extends JPanel {
    StartMenu() {
        JFrame startGame = new JFrame("Gravity Dash");
        startGame.setSize(600, 400);
        startGame.setLocationRelativeTo(null);
        startGame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the Game Over frame

        JButton b = new JButton("Start Game");
        b.setBounds(50, 50, 100, 100);
        b.setName("Start Game");
        startGame.add(b);
        startGame.setVisible(true);

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame.setVisible(false);
                new GamePanel().setVisible(true); // Main Form to show after the Login Form..
            }
        });
    }
}