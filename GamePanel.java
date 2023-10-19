import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import java.util.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    protected Image walkImg = new ImageIcon("src\\knightWalk.gif").getImage(); // Walking right
    protected Image platformImage = new ImageIcon("src\\platformvar1.png").getImage(); // Platform image
    protected Image largePlatformImage = new ImageIcon("src\\platformvar2.png").getImage(); // Larger platform image
    protected Image coinImg = new ImageIcon("src\\coinAnim2.gif").getImage(); // Load coin animation
    protected Image staticCoinImg = new ImageIcon("src\\staticCoin.png").getImage();
    protected Image trapImg = new ImageIcon("src\\spikeTrap.gif").getImage(); // Load spike animation
    protected Image injuredImage = new ImageIcon("src\\injuredKnight.gif").getImage(); // Load injured animation
    protected Image closeBckgr = new ImageIcon("src\\closeButton.png").getImage();


    private int characterX = 100; // Initial character X position
    private int characterY = 555; // Initial character Y position
    private int characterSpeedY = 0; // Character's vertical speed
    private int jumpHeight = 0; // Initialize jump height to 0
    private boolean isJumping = false;
    private boolean canJump = true; // Add a boolean flag to track if the player can jump
    private Timer timer;
    private long lastPlatformTimeLevel1 = System.currentTimeMillis();
    private long lastPlatformTimeLevel2 = System.currentTimeMillis();
    private long lastTrap = System.currentTimeMillis();
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
    private String highScore = "nobody:0"; // The high score
    private int lifeBar = 100; // Initialize life bar to 100
    private boolean collisionCheck = false; // Flag to track if the character has collided with a trap
    private boolean isInjured = false; // Flag to track if the character is injured
    private boolean onPlatform = false; // Flag to track if the character is on a platform
    private boolean canDoubleJump = false; // Flag to track if the character can perform a double jump

    JLabel healthBarLabel;
    JProgressBar healthBar;
    

    public GamePanel() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Side Scroller Game");
            frame.setSize(1366, 768);
            frame.setLocationRelativeTo(null);
            frame.setUndecorated(true);
            frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
            frame.add(this);
            frame.setVisible(true); 

            frame.setShape(new RoundRectangle2D.Double(0, 0, 1366, 768, 50, 50));

            JLabel label = new JLabel();

            JButton closeButton = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(closeBckgr, 0, 0, getWidth(), getHeight(), this);
                }
            };

            closeButton.setBounds(1200, 10, 30, 30);
            // closeButton.setOpaque(false);
            // closeButton.setContentAreaFilled(false);
            // closeButton.setBorderPainted(false);

            closeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new GameOverMenu(collectedCoins, highScore);
                    timer.cancel();
                }
            });
            frame.add(label);
            closeButton.setVisible(true);
            label.add(closeButton);
            

            healthBar = new JProgressBar(0, 100);
            //healthBar.setStringPainted(true);
            healthBar.setValue(lifeBar);
            healthBarLabel = new JLabel();
            healthBarLabel.setBounds(getWidth() - 200, 90, 100, 30);
            healthBar.setBounds(getWidth() - 200, 90, 100, 30);
            healthBar.setForeground(Color.RED);
            healthBar.setBackground(Color.DARK_GRAY);
            add(healthBarLabel);
            add(healthBar);


            JLabel initText = new JLabel();
        });        
        

        //lastTrap = System.currentTimeMillis();

        System.out.println(System.currentTimeMillis() + " " + lastTrap);
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                actionPerformed(null); // Call your game loop logic
            }
        },0, 20); // Adjust the delay as needed

        // Initialize character rectangle for collision detection
        characterRect = new Rectangle(characterX, characterY, 50, 50);

        // Load background image
        backgroundImage = new ImageIcon("src\\background2.png").getImage(); // Change to your background image path

            System.out.println(traps.size());
        // Add the KeyListener to the game panel
        addKeyListener(this);

        // Load coin image
        coinImg = coinImg.getScaledInstance(35, 35, Image.SCALE_DEFAULT);

        // Request focus for the game panel to enable keyboard input
        setFocusable(true);
        requestFocus();
    }

    public void generateLevel1() {
        int platformWidth = 120 + random.nextInt(20);
        int platformHeight = 50;
        Platform platform = new Platform(getWidth() + 1300, 535, platformWidth, platformHeight);
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

    public void generateLevel2() {
        int platformWidth = 70 + random.nextInt(20);
        int platformHeight = 50;
        Platform platform = new Platform(getWidth() + 1300, 465, platformWidth, platformHeight);
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
        
        String[] trapY = {"496", "546"};
        int randomTrapY = random.nextInt(trapY.length);
        Trap trap = new Trap(getWidth() + 2000,/* random.nextInt(randomTrapY)*/ 510, trapWidth, trapHeight);
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

        if (characterY > 555) {
            characterY = 555;
            characterSpeedY = 0;
            canJump = true;
            jumpCount = 0; // Reset jump count when character lands
        }
        characterRect.setBounds(characterX, characterY, 50, 50);

        // Check if the character is on a platform
        if (characterY == 535 && !isJumping) {
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
                if (characterY > 515 && characterY <= 555) {
                    checkScore();
                    new GameOverMenu(collectedCoins, highScore);
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
                if (characterY > 440 && characterY <= 480) {
                    checkScore();
                    new GameOverMenu(collectedCoins, highScore);
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
        // Check for trap collision
        checkTrapCollision();

        // Generate new platforms for Level 1
        if (System.currentTimeMillis() - lastPlatformTimeLevel1 >= random.nextInt(1000, 2000)) {
            generateLevel1();
            lastPlatformTimeLevel1 = System.currentTimeMillis();
        }
        // Generate new platforms for Level 2
        if (System.currentTimeMillis() - lastPlatformTimeLevel2 >= random.nextInt(1500, 2500)) {
            generateLevel2();
            lastPlatformTimeLevel2 = System.currentTimeMillis();
        }
        
        generateTrap();

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

        /*
        *    
        * !!! RUNTIME ERRORS - TRAPS
        *
        */ 
        for (Trap trap : traps) {
            g.drawImage(trapImg, trap.getX(), trap.getY(), this);
        }

        Toolkit.getDefaultToolkit().sync();

        //Graphics2D g2d = (Graphics2D) g;
        
        if (isInjured) {
            g.drawImage(injuredImage, characterX, characterY, this);
        } else {
            g.drawImage(walkImg, characterX, characterY, this);
        }
        //g2d.drawImage(walkImg, characterX, characterY, this);
        initScore();
        // Draw coin counter
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Tahoma", Font.BOLD, 17));
        g.drawImage(staticCoinImg, getWidth()-140, 30, this);
        g.drawString(String.valueOf(collectedCoins), getWidth() - 100, 54);
        g.setColor(Color.RED);

        healthBar.setValue(lifeBar);
        g.setColor(Color.RED);
        
        //g.drawImage(closeBckgr, 1200, 10, 30, 30, this);
    
        //g.drawString("HighScore: " + highScore, 0, 60);

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
            if (characterRect.intersects(trapRect) && !isInjured ) {
                lifeBar -= 10; // Decrease the life bar by 10
                if (lifeBar == 0) {
                    checkScore();
                    new GameOverMenu(collectedCoins, highScore);
                    timer.cancel();
                }
                
                isInjured = true;
                Timer injuredTime = new Timer();

                injuredTime.schedule(new TimerTask() {
                    public void run() {
                        isInjured = false;
                        repaint();
                        injuredTime.cancel();
                    }
                }, 1000);
                //lastTrapTime = System.currentTimeMillis();
            }
        }
    }


    public String getHighScore() { 
        
        FileReader readFile = null;
        BufferedReader reader = null;

        try {
            readFile = new FileReader("highscore.dat");
            reader = new BufferedReader(readFile);
            return reader.readLine();
        } catch (Exception e) {
            return "0";
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }

    public void checkScore() {

        System.out.println(highScore);
        if (highScore.equals("")) {
            return;
        }

        if (collectedCoins > Integer.parseInt((highScore.split(":")[1]))) {
            String name = JOptionPane.showInputDialog("You set a new high score! What is your name?");
            highScore = name + ":" + collectedCoins;

            File scoreFile = new File("highscore.dat");
            if(!scoreFile.exists()) {
                try {
                    scoreFile.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            FileWriter writeFile = null;
            BufferedWriter writer = null;
            
            try {
                writeFile = new FileWriter(scoreFile);
                writer = new BufferedWriter(writeFile);
                writer.write(this.highScore);

            } catch (Exception e) {

            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (Exception e) {
                    
                }
            }
        }
    }

    public void initScore() {
        if (highScore.equals("nobody:0")) {
            highScore = this.getHighScore();
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