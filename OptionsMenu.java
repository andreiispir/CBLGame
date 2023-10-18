import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class OptionsMenu extends JPanel {
    JSlider slider = new JSlider();

    MusicPlayer music = new MusicPlayer();

    OptionsMenu(MusicPlayer music) {
        this.music = music;

        JFrame optionsFrame = new JFrame("Adjust Volume");
        optionsFrame.setSize(500, 300);
        optionsFrame.setLocationRelativeTo(null);
        optionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the Game Over frame

        JLabel optionsLabel = new JLabel("Adjust Volume");
        optionsLabel.setBounds(10,10, 10, 10);
        optionsLabel.setFont(new Font("Arial", Font.BOLD, 16));

        optionsFrame.add(optionsLabel);
        optionsFrame.setVisible(true);
        
        slider = new JSlider(-40, 6);
        slider.setBounds(10, 70, 200, 50);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent event) {
                music.currentVolume = slider.getValue();
                if (music.currentVolume == -40) {
                    music.currentVolume = 80;
                }
                music.fc.setValue(music.currentVolume);
            }

        });

        optionsLabel.add(slider);
        
        //URL musicURL = getClass().getResource("reginanoptii.wav");
        //playMusic(musicURL);
    }

}
