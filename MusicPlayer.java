import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * The MusicPlayer class provides functionality for playing audio clips from a specified URL.
 * It allows setting the audio file, playing it, and looping it continuously.
 */
public class MusicPlayer {
    Clip clip;             // Represents the audio clip.
    float previousVolume = 0; // Stores the previous volume level.
    float currentVolume = -20.0f; // Stores the current volume level.
    FloatControl fc;       // Controls the volume of the audio clip.

    /**
     * Sets the audio file to be played from the provided URL.
     *
     * @url The URL of the audio file to be played.
     */
    public void setFile(URL url) {
        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioInput);
            fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        } catch (Exception e) {
            // Handle any exceptions that may occur during audio file setup.
        }
    }

    /**
     * Plays the audio clip from the beginning.
     *
     * @param url The URL of the audio file to be played.
     */
    public void play(URL url) {
        clip.setFramePosition(0);
        clip.start();
    }

    /**
     * Loops the audio clip continuously, creating a continuous sound loop.
     *
     * @url The URL of the audio file to be looped.
     */
    public void loop(URL url) {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}