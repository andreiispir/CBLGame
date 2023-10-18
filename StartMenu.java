import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class StartMenu extends JPanel {
    
    protected Image menuBackground = new ImageIcon("Images\\menubckg.jpg").getImage();
    JSlider slider;
    MusicPlayer music = new MusicPlayer();

    StartMenu() {
        JFrame startGame = new JFrame("Gravity Dash");
        startGame.setSize(600, 400);
        startGame.setLocationRelativeTo(null);
        startGame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the Game Over frame
        
        JLabel startLabel = new JLabel("");
        startLabel.setBounds(500, 400, 100, 100);
        startGame.add(startLabel);
        
        JButton b = new JButton("Start Game");
        b.setBounds(10, 10, 180, 30);
        b.setName("Start Game");
        startLabel.add(b);
        startGame.setVisible(true);

        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame.setVisible(false);
                new GamePanel().setVisible(true);
            }
        });

        URL musicURL = getClass().getResource("reginanoptii.wav");
        playMusic(musicURL);

        JButton op = new JButton("Options");
        op.setBounds(10, 60, 180, 30);
        op.setName("Options");
        startLabel.add(op);

        op.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new OptionsMenu(music).setVisible(true);    
            }
            
        });
        //new MusicPlayer().playMusic(filePath);

        
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(menuBackground, 0, 0, getWidth(), getHeight(), this);
    }
    
    public void playMusic(URL url) {
        music.setFile(url);
        music.play(url);
        music.loop(url);
    }
    

}