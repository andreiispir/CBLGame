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

    protected Image walkImg = new ImageIcon("output-onlinegiftools.gif").getImage(); // Walking right
    protected Image platformImage = new ImageIcon("platformvar1.png").getImage(); // Platform image
    protected Image largePlatformImage = new ImageIcon("platformvar2.png").getImage(); // Larger platform image
    protected Image coinImg = new ImageIcon("coinAnim2.gif").getImage(); // Load coin animation

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
    private int jumpCount = 0; // Track the number of jumps
    private static final int MAX_JUMP_COUNT = 2; // Maximum allowed jumps (including the initial jump)
    private List<Coin> coins = new ArrayList<>();
    private int collectedCoins = 0; // Counter for collected coins
    private boolean onPlatform = false; // Flag to track if the character is on a platform
    private boolean canDoubleJump = false; // Flag to track if the character can perform a double jump

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
        backgroundImage = new ImageIcon("background2.png").getImage(); // Change to your background image path

        // Add the KeyListener to the game panel
        addKeyListener(this);

        // Load coin image
        coinImg = coinImg.getScaledInstance(35, 35, Image.SCALE_DEFAULT);

        // Request focus for the game panel to enable keyboard input
        setFocusable(true);
        requestFocus();
    }

    public void generateObstacleLevel1() {
        int obstacleWidth = 120 + random.nextInt(20);
        int obstacleHeight = 50;
        Obstacle obstacle = new Obstacle(getWidth()+1000, 500, obstacleWidth, obstacleHeight);
        obstaclesLevel1.add(obstacle);

        int numCoins = random.nextInt(3) + 1; // Generate 1 to 3 coins per platform
        for (int i = 0; i < numCoins; i++) {
            int coinX = obstacle.getX() + random.nextInt(obstacle.getWidth() - 30); // Adjust as needed
            int coinY = obstacle.getY() - 30; // Place the coin above the platform
            Coin coin = new Coin(coinX, coinY);
            coins.add(coin);
            //coin.moveLeft();
        }
        
    }

    public void generateObstacleLevel2() {
        int obstacleWidth = 70 + random.nextInt(20);
        int obstacleHeight = 50;
        Obstacle obstacle = new Obstacle(getWidth()+1000, 450, obstacleWidth, obstacleHeight);
        obstaclesLevel2.add(obstacle);

        int numCoins = random.nextInt(3) + 1; // Generate 1 to 3 coins per platform
        for (int i = 0; i < numCoins; i++) {
            int coinX = obstacle.getX() + random.nextInt(obstacle.getWidth() - 30); // Adjust as needed
            int coinY = obstacle.getY() - 30; // Place the coin above the platform
            Coin coin = new Coin(coinX, coinY);
            coins.add(coin);
            //coin.moveLeft();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isJumping) {
            characterSpeedY = -15; // Move character upwards during jump
            jumpHeight -= 15;
            if (jumpHeight <= 0) {
                isJumping = false;
            }
        } else{
            characterSpeedY = 5; // Apply gravity when not jumping
        }

        characterY += characterSpeedY;

        if (characterY > 500) {
            characterY = 500;
            characterSpeedY = 0;
            canJump = true;
            jumpCount = 0; // Reset jump count when character lands
        }
        characterRect.setBounds(characterX, characterY, 50, 50);

        // Check if the character is on a platform
        if (characterY == 500 && !isJumping) {
            onPlatform = true;
            canDoubleJump = true; // Reset the ability to double jump when on a platform
        } else {
            onPlatform = false;
        }

        Iterator<Obstacle> iteratorLevel1 = obstaclesLevel1.iterator();
        Iterator<Coin> iteratorCoin = coins.iterator();
        while (iteratorLevel1.hasNext()) {
            Obstacle obstacle = iteratorLevel1.next();
            Coin coin = iteratorCoin.next();
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
            coin.moveLeft();

            if (obstacle.getX() + obstacle.getWidth() <= 0) {
                // Remove obstacles that are out of the screen
                iteratorLevel1.remove();
            }
        }

        Iterator<Obstacle> iteratorLevel2 = obstaclesLevel2.iterator();
        while (iteratorLevel2.hasNext()) {
            Obstacle obstacle = iteratorLevel2.next();
            Coin coin = iteratorCoin.next();
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
            coin.moveLeft();


            if (obstacle.getX() + obstacle.getWidth() <= 0) {
                // Remove obstacles that are out of the screen
                iteratorLevel2.remove();
            }
        }
        

        // Check for coin collection
        checkCoinCollection();

        // Generate new obstacles for Level 1
        if (System.currentTimeMillis() - lastObstacleTimeLevel1 >= 1000) {
            generateObstacleLevel1();
            lastObstacleTimeLevel1 = System.currentTimeMillis();
        }
        // Generate new obstacles for Level 2
        if (System.currentTimeMillis() - lastObstacleTimeLevel2 >= 1600) {
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
            //g.fillRect(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
            g.drawImage(largePlatformImage, obstacle.getX(), obstacle.getY(), this);
        }

        g.setColor(Color.GREEN); // Set the color to green for Level 2 obstacles
        for (Obstacle obstacle : obstaclesLevel2) {
            //g.fillRect(obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
            g.drawImage(platformImage, obstacle.getX(), obstacle.getY(), this);
        }

        // Draw coins
        for (Coin coin : coins) {
            if (coin.isVisible()) {
                g.drawImage(coinImg, coin.getX(), coin.getY(), this);
            }
        }

        Toolkit.getDefaultToolkit().sync();

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(walkImg, characterX, characterY, this);

          // Draw coin counter
          g.setColor(Color.WHITE);
          g.setFont(new Font("Arial", Font.PLAIN, 20));
          g.drawString("Coins: " + collectedCoins, getWidth() - 100, 30);
  
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
    if (key == KeyEvent.VK_SPACE) {
        if (onPlatform || canJump || (canDoubleJump && jumpCount < MAX_JUMP_COUNT)) {
            // Start the jump when SPACE key is pressed, and not already jumping,
            // and character is on the ground or can jump or has available jumps
            isJumping = true;
            jumpHeight = (jumpCount == 0) ? 110 : 80; // Different jump heights for initial and double jumps
            canJump = false; // Disable jumping until the player lands
            jumpCount++; // Increment the jump count
            if (onPlatform) {
                canDoubleJump = true; // Allow a double jump when on a platform
            }
        }
    }
    }

    private void checkCoinCollection() {
        Rectangle characterRect = new Rectangle(characterX, characterY, 50, 50);
        Iterator<Coin> iterator = coins.iterator();
        while (iterator.hasNext()) {
            Coin coin = iterator.next();
            Rectangle coinRect = new Rectangle(coin.getX(), coin.getY(), 30, 30);
            if (characterRect.intersects(coinRect) && coin.isVisible()) {
                coin.setVisible(false); // Mark the coin as collected
                collectedCoins++; // Increment the coin counter
            }
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