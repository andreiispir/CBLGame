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

    protected Image walkImg = new ImageIcon("Images\\knightWalk.gif").getImage(); // Walking right
    protected Image platformImage = new ImageIcon("Images\\platformvar1.png").getImage(); // Platform image
    protected Image largePlatformImage = new ImageIcon("Images\\platformvar2.png").getImage(); // Larger platform image
    protected Image coinImg = new ImageIcon("Images\\coinAnim2.gif").getImage(); // Load coin animation
    protected Image trapImg = new ImageIcon("Images\\spikeTrap.gif").getImage(); // Load spike animation

    private int characterX = 100; // Initial character X position
    private int characterY = 525; // Initial character Y position
    private int characterSpeedY = 0; // Character's vertical speed
    private int jumpHeight = 0; // Initialize jump height to 0
    private boolean isJumping = false;
    private boolean canJump = true; // Add a boolean flag to track if the player can jump
    private Timer timer;
    private long lastPlatformTimeLevel1;
    private long lastPlatformTimeLevel2;
    private int platformsCount;
    private Rectangle characterRect;
    private Image backgroundImage;
    private List<Platform> platformsLevel1 = new ArrayList<>();
    private List<Platform> platformsLevel2 = new ArrayList<>();
    private Random random = new Random();
    private int jumpCount = 0; // Track the number of jumps
    private static final int MAX_JUMP_COUNT = 2; // Maximum allowed jumps (including the initial jump)
    private List<Coin> coins = new ArrayList<>();
    private List<Trap> traps = new ArrayList<>();
    private int collectedCoins = 0; // Counter for collected coins
    private int lifeBar = 100; // Initialize life bar to 100
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
        backgroundImage = new ImageIcon("Images\\background2.png").getImage(); // Change to your background image path

        // Add the KeyListener to the game panel
        addKeyListener(this);

        // Load coin image
        coinImg = coinImg.getScaledInstance(35, 35, Image.SCALE_DEFAULT);

        // Request focus for the game panel to enable keyboard input
        setFocusable(true);
        requestFocus();
    }

    public void generatePlatformLevel1() {
        int platformWidth = 120 + random.nextInt(20);
        int platformHeight = 50;
        Platform platform = new Platform(getWidth()+1000, 520, platformWidth, platformHeight);
        platformsLevel1.add(platform);

        int numCoins = random.nextInt(3) + 1; // Generate 1 to 3 coins per platform
        for (int i = 0; i < numCoins; i++) {
            int coinX = platform.getX() + random.nextInt(platform.getWidth() - 30); // Adjust as needed
            int coinY = platform.getY() - 30; // Place the coin above the platform
            Coin coin = new Coin(coinX, coinY);
            coins.add(coin);
            //coin.moveLeft();
        }
        
    }

    public void generatePlatformLevel2() {
        int platformWidth = 70 + random.nextInt(20);
        int platformHeight = 50;
        Platform platform = new Platform(getWidth()+1000, 450, platformWidth, platformHeight);
        platformsLevel2.add(platform);
        

        int numCoins = random.nextInt(3) + 1; // Generate 1 to 3 coins per platform
        for (int i = 0; i < numCoins; i++) {
            int coinX = platform.getX() + random.nextInt(platform.getWidth() - 30); // Adjust as needed
            int coinY = platform.getY() - 30; // Place the coin above the platform
            Coin coin = new Coin(coinX, coinY);
            coins.add(coin);
            //coin.moveLeft();
        }
    }

    public void generateTrap() {
        int trapWidth = 30;
        int trapHeight = 30;

        
        Trap trap = new Trap(getWidth(), 496, trapWidth, trapHeight);
        traps.add(trap);
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

        if (characterY > 525) {
            characterY = 525;
            characterSpeedY = 0;
            canJump = true;
            jumpCount = 0; // Reset jump count when character lands
        }
        characterRect.setBounds(characterX, characterY, 50, 50);

        // Check if the character is on a platform
        if (characterY == 525 && !isJumping) {
            onPlatform = true;
            canDoubleJump = true; // Reset the ability to double jump when on a platform
        } else {
            onPlatform = false;
        }

        Iterator<Platform> iteratorLevel1 = platformsLevel1.iterator();
        Iterator<Coin> iteratorCoin = coins.iterator();
        Iterator<Trap> iteratorTrap = traps.iterator();
        while (iteratorLevel1.hasNext()) {
            Platform platform = iteratorLevel1.next();
            Coin coin = iteratorCoin.next();
            Trap trap = iteratorTrap.next();
            if (characterY + 50 > platform.getY() && characterY < platform.getY() + platform.getHeight()
                    && characterX + 50 > platform.getX() && characterX < platform.getX() + platform.getWidth()) {
                // Character is colliding with the platform
                //platformsCount++;
                if (characterY > 485 && characterY <= 525) {
                    new GameOver();
                    timer.cancel();
                }
                if (!isJumping && characterY < platform.getY()) {
                    // Character is above the platform, reset its vertical position
                    characterY = platform.getY() - 50;
                    canJump = true; // Enable jumping when on the platform
                }
            }

            platform.moveLeft();
            coin.moveLeft();
            trap.moveLeft();

            if (platform.getX() + platform.getWidth() <= 0) {
                // Remove platforms that are out of the screen
                iteratorLevel1.remove();
            }
        }

        Iterator<Platform> iteratorLevel2 = platformsLevel2.iterator();
        while (iteratorLevel2.hasNext()) {
            Platform platform = iteratorLevel2.next();
            Coin coin = iteratorCoin.next();
            if (characterY + 50 > platform.getY() && characterY < platform.getY() + platform.getHeight()
                    && characterX + 50 > platform.getX() && characterX < platform.getX() + platform.getWidth()) {
                // Character is colliding with the platform
                //platformsCount++;
                if (characterY > 410 && characterY <= 450) {
                    new GameOver();
                    timer.cancel();
                }
                if (!isJumping && characterY < platform.getY()) {
                    // Character is above the platform, reset its vertical position
                    characterY = platform.getY() - 50;
                    canJump = true; // Enable jumping when on the platform
                }
            }

            platform.moveLeft();
            coin.moveLeft();


            if (platform.getX() + platform.getWidth() <= 0) {
                // Remove platforms that are out of the screen
                iteratorLevel2.remove();
            }
        }
        

        // Check for coin collection
        checkCoinCollection();
        checkTrapCollision();

        // Generate new platforms for Level 1
        if (System.currentTimeMillis() - lastPlatformTimeLevel1 >= random.nextInt(1000, 2000)) {
            generatePlatformLevel1();
            System.out.println(platformsCount + " ");

            if (platformsCount % 5 == 0) {
                generateTrap();
            }


            lastPlatformTimeLevel1 = System.currentTimeMillis();
        }
        // Generate new platforms for Level 2
        if (System.currentTimeMillis() - lastPlatformTimeLevel2 >= random.nextInt(1500, 2500)) {
            generatePlatformLevel2();
            lastPlatformTimeLevel2 = System.currentTimeMillis();
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        g.setColor(Color.RED); // Set the color to red for Level 1 Platforms
        for (Platform platform : platformsLevel1) {
            //g.fillRect(platform.getX(), platform.getY(), platform.getWidth(), platform.getHeight());
            g.drawImage(largePlatformImage, platform.getX(), platform.getY(), this);
        }

        g.setColor(Color.GREEN); // Set the color to green for Level 2 platforms
        for (Platform platform : platformsLevel2) {
            //g.fillRect(platform.getX(), platform.getY(), platform.getWidth(), platform.getHeight());
            g.drawImage(platformImage, platform.getX(), platform.getY(), this);
        }

        // Draw coins
        for (Coin coin : coins) {
            if (coin.isVisible()) {
                g.drawImage(coinImg, coin.getX(), coin.getY(), this);
            }
        }

        for (Trap trap : traps)
        {
            g.drawImage(trapImg, trap.getX(), trap.getY(), this);
        }

        Toolkit.getDefaultToolkit().sync();

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(walkImg, characterX, characterY, this);

        // Draw coin counter
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Tahoma", Font. BOLD, 17));
        g.drawString("Coins: " + collectedCoins, getWidth() - 100, 30);
        g.setColor(Color.RED);
        g.drawString("Life: " + lifeBar, getWidth() - 100, 60);

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

    private void checkTrapCollision() {
        Rectangle characterRect = new Rectangle(characterX, characterY, 50, 50);
        Iterator<Trap> iterator = traps.iterator();
        while (iterator.hasNext()) {
            Trap trap = iterator.next();
            Rectangle trapRect = new Rectangle(trap.getX(), trap.getY(), 30, 30);
            if (characterRect.intersects(trapRect)) {
                lifeBar -= 10; // Decrease the life bar by 10
                if (lifeBar == 0) {
                    new GameOver();
                    timer.cancel();
                }
                //lastTrapTime = System.currentTimeMillis();
                continue;
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