import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import java.util.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    protected Image walkImg = new ImageIcon("characterIdle.png").getImage(); // Walking right

    private int characterX = 100; // Initial character X position
    private int characterY = 500; // Initial character Y position
    private int characterSpeedY = 0; // Character's vertical speed
    private int jumpHeight = 0; // Initialize jump height to 0
    private boolean isJumping = false;
    private boolean canJump = true; // Add a boolean flag to track if the player can jump
    private Timer timer;
    private long lastObstacleTimeLevel1;
    private long lastObstacleTimeLevel2;
    private Rectangle characterRect;
    private Image backgroundImage;
    private List<Obstacle> obstaclesLevel1 = new ArrayList<>();
    private List<Obstacle> obstaclesLevel2 = new ArrayList<>();
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

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                actionPerformed(null); // Call your game loop logic
            }
        }, 0, 20); // Adjust the delay as needed

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

    public void generateObstacleLevel1() {
        int obstacleWidth = 60 + random.nextInt(20);
        int obstacleHeight = 50;
        Obstacle obstacle = new Obstacle(getWidth(), 500, obstacleWidth, obstacleHeight);
        obstaclesLevel1.add(obstacle);
    }

    public void generateObstacleLevel2() {
        int obstacleWidth = 60 + random.nextInt(20);
        int obstacleHeight = 50;
        Obstacle obstacle = new Obstacle(getWidth(), 450, obstacleWidth, obstacleHeight);
        obstaclesLevel2.add(obstacle);
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

        Iterator<Obstacle> iteratorLevel1 = obstaclesLevel1.iterator();
        while (iteratorLevel1.hasNext()) {
            Obstacle obstacle = iteratorLevel1.next();
            if (characterY + 50 > obstacle.getY() && characterY < obstacle.getY() + obstacle.getHeight()
                    && characterX + 50 > obstacle.getX() && characterX < obstacle.getX() + obstacle.getWidth()) {
                // Character is colliding with the obstacle
                if (characterY > 460 && characterY <= 500) {
                    new GameOver();
                    timer.cancel();
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
                iteratorLevel1.remove();
            }
        }

        Iterator<Obstacle> iteratorLevel2 = obstaclesLevel2.iterator();
        while (iteratorLevel2.hasNext()) {
            Obstacle obstacle = iteratorLevel2.next();
            if (characterY + 50 > obstacle.getY() && characterY < obstacle.getY() + obstacle.getHeight()
                    && characterX + 50 > obstacle.getX() && characterX < obstacle.getX() + obstacle.getWidth()) {
                // Character is colliding with the obstacle
                if (characterY > 410 && characterY <= 450) {
                    new GameOver();
                    timer.cancel();
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
                iteratorLevel2.remove();
            }
        }

        // Generate new obstacles for Level 1
        if (System.currentTimeMillis() - lastObstacleTimeLevel1 >= 850) {
            generateObstacleLevel1();
            lastObstacleTimeLevel1 = System.currentTimeMillis();
        }
        // Generate new obstacles for Level 2
        if (System.currentTimeMillis() - lastObstacleTimeLevel2 >= 1300) {
            generateObstacleLevel2();
            lastObstacleTimeLevel2 = System.currentTimeMillis();
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        g.setColor(Color.RED); // Set the color to red for Level 1 obstacles
        for (Obstacle obstacle : obstaclesLevel1) {
            g.fillRect(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
        }

        g.setColor(Color.GREEN); // Set the color to green for Level 2 obstacles
        for (Obstacle obstacle : obstaclesLevel2) {
            g.fillRect(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
        }

        Toolkit.getDefaultToolkit().sync();

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(walkImg, characterX, characterY, this);
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

    public static void main(String[] args) {
        new GamePanel();
    }
}
