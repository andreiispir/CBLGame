/**
 * The Coin class represents a coin object in a game. Coins have a position (x, y),
 * visibility status, and a speed at which they move.
 */
public class Coin {
    private int x;          // The x-coordinate of the coin's position.
    private int y;          // The y-coordinate of the coin's position.
    private boolean visible; // A flag indicating whether the coin is visible.
    private int speed;      // The speed at which the coin moves.

    /**
     * Constructs a new Coin object with the specified initial position (x, y).
     * The coin is initially visible and has a default speed of 5 units per step.
     *
     * @param x The initial x-coordinate of the coin.
     * @param y The initial y-coordinate of the coin.
     */
    public Coin(int x, int y) {
        this.x = x;
        this.y = y;
        this.visible = true;
        this.speed = 5;
    }

    /**
     * Moves the coin to the left by the default speed.
     */
    public void moveLeft() {
        x -= speed;
    }

    /**
     * Moves the coin to the left by a specified speed.
     *
     * @speed The speed at which the coin should move to the left.
     */
    public void moveCollision() {
        x -= 3;
    }

    /**
     * Retrieves the speed at which the coin moves.
     *
     * @return The current speed of the coin's movement.
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Retrieves the x-coordinate of the coin's position.
     *
     * @return The current x-coordinate of the coin.
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the y-coordinate of the coin's position.
     *
     * @return The current y-coordinate of the coin.
     */
    public int getY() {
        return y;
    }

    /**
     * Checks whether the coin is currently visible.
     *
     * @return true if the coin is visible, false otherwise.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets the visibility status of the coin.
     *
     * @visible true to make the coin visible, false to hide it.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}