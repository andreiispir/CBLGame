import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSlider;

public class StartMenu extends JPanel {
    
    protected Image menuBackground = new ImageIcon("src\\menu.jpg").getImage();
    protected Image closeBckgr = new ImageIcon("src\\closeButton.png").getImage();
    JSlider slider;
    MusicPlayer music = new MusicPlayer();

    StartMenu() {
        JFrame startGame = new JFrame("Gravity Dash");
        startGame.setSize(600, 400);
        startGame.setLocationRelativeTo(null);
        startGame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the Game Over frame
        startGame.add(this);
        startGame.setUndecorated(true);
        startGame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);

        startGame.setShape(new RoundRectangle2D.Double(0, 0, 600, 400, 50, 50));

        JLabel startLabel = new JLabel("") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(menuBackground, 0, 0, getWidth(), getHeight(), this);
            }
        };

        startLabel.setBounds(500, 400, 100, 100);
        startGame.add(startLabel);
        
        JButton b = new JButton("Start Game");
        b.setBounds(20, 25, 180, 30);
        b.setName("Start Game");
        b.setForeground(Color.WHITE);
        b.setBackground(Color.GRAY);
        b.setFont(new Font("Tahoma", Font.BOLD, 14));
        b.setFocusPainted(false);
        startLabel.add(b);
        startGame.setVisible(true);

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame.setVisible(false);
                new GamePanel().setVisible(true);
            }
        });

        URL musicURL = getClass().getResource("src\\reginanoptii.wav");
        playMusic(musicURL);

        JButton op = new JButton("Options");
        op.setBounds(20, 65, 180, 30);
        op.setName("Options");
        op.setForeground(Color.WHITE);
        op.setBackground(Color.GRAY);
        op.setFont(new Font("Tahoma", Font.BOLD, 14));
        op.setFocusPainted(false);
        startLabel.add(op);

        op.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new OptionsMenu(music).setVisible(true);    
            }
            
        });

        JButton closeButton = new JButton("Close") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(closeBckgr, 0, 0, getWidth(), getHeight(), this);
            }
        };

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        closeButton.setBounds(550, 10, 30, 30);
        startLabel.add(closeButton);
                
    }
    
    public void playMusic(URL url) {
        music.setFile(url);
        music.play(url);
        music.loop(url);
    }
    

}