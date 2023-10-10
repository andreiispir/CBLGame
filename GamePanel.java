import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private int characterX = 100; // Initial character X position
    private int characterY = 500; // Initial character Y position
    private int characterSpeedY = 0; // Character's vertical speed
    private int jumpHeight = 0; // Initialize jump height to 0
    private boolean isJumping = false;
    private boolean canJump = true; // Add a boolean flag to track if the player can jump
    private Timer timer;
    private Rectangle characterRect;
    private Image backgroundImage;
    private List<Obstacle> obstacles = new ArrayList<>();
    private Random random = new Random();


    public GamePanel() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Side Scroller Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1366, 768);
            frame.setLocationRelativeTo(null);
            frame.add(this);
            frame.setVisible(true);
        });
        
        timer = new Timer(20, this); // Timer for the game loop
        timer.start();

        // Initialize character rectangle for collision detection
        characterRect = new Rectangle(characterX, characterY, 50, 50);

        // Load background image
        backgroundImage = new ImageIcon("background.png").getImage(); // Change to your background image path

        // Add the KeyListener to the game panel
        addKeyListener(this);

        // Request focus for the game panel to enable keyboard input
        setFocusable(true);
        requestFocus();
    }

     @Override
    public void actionPerformed(ActionEvent e) {
        if (isJumping) {
            characterSpeedY = -5; // Move character upwards during jump
            jumpHeight -= 5;
            if (jumpHeight <= 0) {
                isJumping = false;
            }
        } else {
            characterSpeedY = 5; // Apply gravity when not jumping
        }

        characterY += characterSpeedY;

        if (characterY > 500) {
            characterY = 500;
            characterSpeedY = 0;
            canJump = true; // Enable jumping when the player lands
        }

        characterRect.setBounds(characterX, characterY, 50, 50);

        Iterator<Obstacle> iterator = obstacles.iterator();
        while (iterator.hasNext()) {
            Obstacle obstacle = iterator.next();
            if (characterY + 50 > obstacle.getY() && characterY < obstacle.getY() + obstacle.getHeight() &&
                    characterX + 50 > obstacle.getX() && characterX < obstacle.getX() + obstacle.getWidth()) {
                // Character is colliding with the obstacle
                if (characterY == 500) {
                    new GameOver();
                    timer.stop();
                }
                if (!isJumping && characterY < obstacle.getY()) {
                    // Character is above the obstacle, reset its vertical position
                    characterY = obstacle.getY() - 50;
                    canJump = true; // Enable jumping when on the obstacle
                }
            }

            obstacle.moveLeft();

            if (obstacle.getX() + obstacle.getWidth() <= 0) {
                // Remove obstacles that are out of the screen
                iterator.remove();
            }
        }

        // Generate new obstacles
        if (random.nextInt(100) < 5) { // Adjust the probability to control obstacle generation rate
            int obstacleWidth = 30 + random.nextInt(20); // Randomize obstacle width
            int obstacleHeight = 50; // Adjust this value for obstacle height
            obstacles.add(new Obstacle(getWidth(), 500, obstacleWidth, obstacleHeight));
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        g.setColor(Color.RED);
        g.fillRect(characterX, characterY, 50, 50); // Character

        g.setColor(Color.BLUE); // Set the color to blue for obstacles
        for (Obstacle obstacle : obstacles) {
            g.fillRect(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
        }

        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE && !isJumping && (characterY >= 500 || canJump)) {
            // Start the jump when SPACE key is pressed, and not already jumping,
            // and character is on the ground or can jump
            isJumping = true;
            jumpHeight = 80;
            canJump = false; // Disable jumping until the player lands
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Unused for this example
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Unused for this example
    }
    
}
