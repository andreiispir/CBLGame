import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class MusicPlayer {
    Clip clip;

    float previousVolume = 0;
    float currentVolume = -20.0f;
    FloatControl fc;
    
    public void setFile(URL url) {
        try {            
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioInput);
            fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        } catch (Exception e) {

        }
    }

    public void play(URL url) {
        clip.setFramePosition(0);
        clip.start();
    }

    public void loop(URL url) {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

}
