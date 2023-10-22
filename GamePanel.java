import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;


public class GamePanel extends JPanel implements ActionListener, KeyListener {

    // Load walking animations
    protected Image walkImg = new ImageIcon("src\\knightWalk.gif").getImage();
    protected Image injuredImage = new ImageIcon("src\\injuredKnight.gif").getImage();
    
    // Load platform images
    protected Image platformImage = new ImageIcon("src\\platformvar1.png").getImage(); 
    protected Image largePlatformImage = new ImageIcon("src\\platformvar2.png").getImage();

    // Load coin animations
    protected Image coinImg = new ImageIcon("src\\coinAnim2.gif").getImage();
    protected Image staticCoinImg = new ImageIcon("src\\staticCoin.png").getImage();
    
    // Load trap animation
    protected Image trapImg = new ImageIcon("src\\spikeTrap.gif").getImage();    

    // Load close game button
    protected Image closeBckgr = new ImageIcon("src\\closeButton.png").getImage();


    private int characterX = 100; // Initial character X position
    private int characterY = 518; // Initial character Y position
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
    private static final int MAX_JUMP_COUNT = 2; // Maximum allowed jumps
    private List<Coin> coins = new ArrayList<>();
    private List<Trap> traps = new ArrayList<>();
    private int collectedCoins = 0; // Counter for collected coins
    private String highScore = "nobody:0"; // The high score
    private int lifeBar = 100; // Initialize life bar to 100
    private boolean isInjured = false; // Flag to track if the character is injured
    private boolean onPlatform = false; // Flag to track if the character is on a platform
    private boolean canDoubleJump = false; // Flag to track if double jump is permitted

    private JProgressBar healthBar;
    

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
                    g.drawImage(closeBckgr, 0, 0, 30, 30, this);
                }
            };

            closeButton.setBounds(getWidth() - 250, 10, 30, 30);
            closeButton.setOpaque(false);
            closeButton.setContentAreaFilled(false);
            closeButton.setBorderPainted(false);

            closeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new GameOverMenu(collectedCoins, highScore);
                    timer.cancel();
                }
            });
            frame.add(label);
            closeButton.setVisible(true);
            //label.add(closeButton);
            frame.add(closeButton);
            

            healthBar = new JProgressBar(0, 100);
            //healthBar.setStringPainted(true);
            healthBar.setValue(lifeBar);

            System.out.println(getWidth());
            healthBar.setBounds(getWidth() - 200, 90, 100, 10);
            healthBar.setForeground(Color.RED);
            healthBar.setBackground(Color.DARK_GRAY);
        
            healthBar.setVisible(true);

            label.add(healthBar);
            label.setVisible(true);
            frame.setVisible(true);

        });        
        

        //lastTrap = System.currentTimeMillis();

        System.out.println(System.currentTimeMillis() + " " + lastTrap);
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
        backgroundImage = new ImageIcon("src\\background2.png").getImage();

        System.out.println(traps.size() + "Traps");
        System.out.println(platformsLevel1.size());

        // Add the KeyListener to the game panel
        addKeyListener(this);

        // Load coin image
        coinImg = coinImg.getScaledInstance(35, 35, Image.SCALE_DEFAULT);

        walkImg = walkImg.getScaledInstance(87, 87, Image.SCALE_DEFAULT);
        injuredImage = injuredImage.getScaledInstance(87, 87, Image.SCALE_DEFAULT);

        // Request focus for the game panel to enable keyboard input
        setFocusable(true);
        requestFocus();
    }

    private Platform lastPlatformLevel2;

    public void generateLevel1() {
        int platformWidth = 120 + random.nextInt(20);
        int platformHeight = 50;
        Platform platform = new Platform(getWidth() + 1300, 555, platformWidth, platformHeight);
        platformsLevel1.add(platform);

        int numCoins = random.nextInt(3) + 1; // Generate 1 to 3 coins per platform
        for (int i = 0; i < numCoins; i++) {
            int coinX = platform.getX() + random.nextInt(platform.getWidth() - 40);
            int coinY = platform.getY() - 30; // Place the coin above the platform
            Coin coin = new Coin(coinX, coinY);
            coins.add(coin);
            //coin.moveLeft();
        }

        int trapWidth = 30;
        int trapHeight = 30;

        String[] trapY = {"530", "385"};
        int randomIndex = random.nextInt(trapY.length);
        String randomTrapY = trapY[randomIndex];
        if (System.currentTimeMillis() - lastTrap >= 3000) {
            //System.out.println(randomTrapY);
            if (randomTrapY.equals("385") && lastPlatformLevel2 != null) {
                // Generate the trap on the last platform of Level 2 if trapY is "385"
                Trap trap = new Trap(lastPlatformLevel2.getX(), 
                            Integer.parseInt(randomTrapY), trapWidth, trapHeight);
                traps.add(trap);
                lastTrap = System.currentTimeMillis();
            } else {
                // Otherwise, generate the trap on the Level 1 platform
                Trap trap = new Trap(platform.getX(), 
                            Integer.parseInt(randomTrapY), trapWidth, trapHeight);
                traps.add(trap);
                lastTrap = System.currentTimeMillis();
            }
        }

        //System.out.println("l1 called");
        
    }

    public void generateLevel2() {
        int platformWidth = 70 + random.nextInt(20);
        int platformHeight = 50;
        Platform platform = new Platform(getWidth() + 1300, 405, platformWidth, platformHeight);
        lastPlatformLevel2 = platform; // To obtain Y-axis for generating traps on Level 2
        platformsLevel2.add(platform);
        
        int numCoins = random.nextInt(3) + 1; // Generate 1 to 3 coins per platform
        for (int i = 0; i < numCoins; i++) {
            int coinX = platform.getX() + random.nextInt(platform.getWidth() - 40);
            int coinY = platform.getY() - 30; // Place the coin above the platform
            Coin coin = new Coin(coinX, coinY);
            coins.add(coin);
        }
        
        //System.out.println("l2 called");
    }


    // public void generateTrap() {
    //     if (System.currentTimeMillis() - lastTrap >= 2000) {
    //         int trapWidth = 30;
    //         int trapHeight = 30;
    //         String[] trapY = {"496", "546"};
    //         int randomTrapY = random.nextInt(trapY.length);
    //         Trap trap = new Trap(getWidth() + 2450, 
    //                      random.nextInt(randomTrapY), trapWidth, trapHeight);
    //         traps.add(trap);
    //         lastTrap = System.currentTimeMillis(); // Update the time of the last trap generated
    //     }
    // }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isJumping) {
            characterSpeedY = -15; // Move character upwards during jump
            jumpHeight -= 15;
            if (jumpHeight <= 0) {
                isJumping = false;
            }
        } else {
            characterSpeedY = 5; // Apply gravity when not jumping
        }

        characterY += characterSpeedY;

        if (characterY > 518) {
            characterY = 518;
            characterSpeedY = 0;
            canJump = true;
            jumpCount = 0; // Reset jump count when character lands
        }

        characterRect.setBounds(characterX, characterY, 87, 87);

        // Check if the character is on a platform
        if (characterY == 518) {
            onPlatform = true;
            canDoubleJump = true; // Reset the ability to double jump when on a platform
        } else {
            onPlatform = false;
        }

        Iterator<Platform> iteratorLevel1 = platformsLevel1.iterator();
        Iterator<Coin> iteratorCoin = coins.iterator();
        Iterator<Trap> iteratorTrap = traps.iterator();
        while (iteratorLevel1.hasNext()) {
            Platform platform = iteratorLevel1.next(); // Y = 555, characterY on platform = 468
            Coin coin = iteratorCoin.next();
            if (characterY + 87 > platform.getY() && characterY < platform.getY() 
                        + platform.getHeight() && characterX + 87 > platform.getX()
                        && characterX < platform.getX() + platform.getWidth()) {
                // Character is colliding with the platform
                if (characterY > 505 && characterY <= 518) {
                    checkScore();
                    new GameOverMenu(collectedCoins, highScore);
                    timer.cancel();
                }

                if (!isJumping && characterY < platform.getY()) {
                    // Character is above the platform, reset its vertical position
                    characterY = platform.getY() - 87;
                    canJump = true; // Enable jumping when on the platform
                }
            }


            

            platform.moveLeft();
            coin.moveLeft();


            if (platform.getX() + platform.getWidth() <= 0) {
                // Remove platforms that are out of the screen
                iteratorLevel1.remove();
            }
            if (coin.getX() + 30 < 0) {
                // Coin is out of the screen
                iteratorCoin.remove();
            }
            
            if (iteratorTrap.hasNext()) {
                Trap trap = iteratorTrap.next();
                trap.moveLeft();
                if (trap.getX() + trap.getWidth() < 0) {
                    // Trap is out of the screen
                    iteratorTrap.remove();
                }
            }
        
        }

        Iterator<Platform> iteratorLevel2 = platformsLevel2.iterator();
        while (iteratorLevel2.hasNext()) {
            Platform platform = iteratorLevel2.next(); // Y = 405 || characterY on platform = 318
            Coin coin = iteratorCoin.next();
            if (characterY + 87 > platform.getY() && characterY < platform.getY()
                    && characterX + 87 > platform.getX()
                    && characterX < platform.getX() + platform.getWidth()) {
                // Character is colliding with the platform
                if (characterY > 330 && characterY < 405) {
                    checkScore();
                    new GameOverMenu(collectedCoins, highScore);
                    timer.cancel();
                }

                if (isJumping && characterY - 87 == platform.getY()) {
                    characterY = platform.getY() + 88;
                    isJumping = false;
                    jumpHeight = 0;
                }

                if (!isJumping && characterY < platform.getY()) {
                    // Character is above the platform, reset its vertical position
                    characterY = platform.getY() - 87;
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
        if (System.currentTimeMillis() - lastPlatformTimeLevel1 >= random.nextInt(2000, 3500)) {
            generateLevel1();
            lastPlatformTimeLevel1 = System.currentTimeMillis();
        }
        // Generate new platforms for Level 2
        if (System.currentTimeMillis() - lastPlatformTimeLevel2 >= random.nextInt(2200, 4000)) {
            generateLevel2();
            lastPlatformTimeLevel2 = System.currentTimeMillis();
        }
        
        
        // if (System.currentTimeMillis() - lastTrap >= random.nextInt(2500, 4000)) {
        //     generateTrap();
        //     lastTrap = System.currentTimeMillis();
        // }

        //generateTrap();

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        g.setColor(Color.RED); // Set the color to red for Level 1 Platforms
        for (Platform platform :platformsLevel1) {
            g.drawImage(largePlatformImage, platform.getX(), platform.getY(), this);
        }

        g.setColor(Color.GREEN); // Set the color to green for Level 2 platforms
        for (Platform platform : platformsLevel2) {
            g.drawImage(platformImage, platform.getX(), platform.getY(), this);
        }

        // Draw coins
        for (Coin coin : coins) {
            if (coin.isVisible()) {
                g.drawImage(coinImg, coin.getX(), coin.getY(), this);
            }
        }

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
        g.drawImage(staticCoinImg, getWidth() - 140, 30, this);
        g.drawString(String.valueOf(collectedCoins), getWidth() - 100, 54);
        g.drawString(String.valueOf(characterY), getWidth() - 300, 100);

        g.drawString(String.valueOf(lifeBar), getWidth() - 350, 100);

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
                jumpHeight = (jumpCount == 0) ? 110 : 80; // Different heights for 1st and 2nd jump
                canJump = false; // Disable jumping until the player lands
                jumpCount++; // Increment the jump count
                if (onPlatform) {
                    jumpCount = 0;
                    canDoubleJump = true; // Allow a double jump when on a platform
                }
            }
        }
    }

    private void checkCoinCollection() {
        Rectangle characterRect = new Rectangle(characterX, characterY, 87, 87);
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
        Rectangle characterRect = new Rectangle(characterX, characterY, 87, 87);
        Iterator<Trap> iterator = traps.iterator();
        while (iterator.hasNext()) {
            Trap trap = iterator.next();
            Rectangle trapRect = new Rectangle(trap.getX(), trap.getY(), 30, 30);
            if (characterRect.intersects(trapRect) && !isInjured) {
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
            String name = JOptionPane.showInputDialog(
                "You set a new high score! What is your name?");

            highScore = name + ":" + collectedCoins;

            File scoreFile = new File("highscore.dat");
            if (!scoreFile.exists()) {
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
                // 
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (Exception e) {
                    //
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