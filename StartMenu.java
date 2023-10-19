import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JSlider;

public class StartMenu extends JPanel {
    
    protected Image menuBackground = new ImageIcon("src\\startMenuBkg.png").getImage();
    protected Image closeBckgr = new ImageIcon("src\\closeButton.png").getImage();
    protected Image optionsButton = new ImageIcon("src\\optionsButton.png").getImage();
    protected Image newGameButton = new ImageIcon("src\\newGameButton.png").getImage();

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
        
        JButton b = new JButton("") {
                 @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(newGameButton, 0, 0, getWidth(), getHeight(), this);
            }
        };
        b.setBounds(200, 200, 180, 30);
        b.setFocusPainted(false);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
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

        JButton op = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(optionsButton, 0, 0, getWidth(), getHeight(), this);
            }
        };
        op.setBounds(200, 250, 180, 30);
        op.setFocusPainted(false);
        op.setOpaque(false);
        op.setContentAreaFilled(false);
        op.setBorderPainted(false);
        startLabel.add(op);

        op.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new OptionsMenu(music).setVisible(true);    
            }
            
        });

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


        closeButton.setBounds(550, 10, 30, 30);
        startLabel.add(closeButton);
                
    }
    
    public void playMusic(URL url) {
        music.setFile(url);
        music.play(url);
        music.loop(url);
    }
    

}