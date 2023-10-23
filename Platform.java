/**
 * The Platform class represents a platform object used in a game. Platforms have a position (x, y),
 * width, height, and a speed at which they move.
 */
public class Platform {
    private int x;      // The x-coordinate of the platform's position.
    private int y;      // The y-coordinate of the platform's position.
    private int width;  // The width of the platform.
    private int height; // The height of the platform.
    public int speed;   // The speed at which the platform moves.

    /**
     * Constructs a new Platform object with the specified position, width, and height.
     * The platform is initially assigned a default speed of 5 units per step.
     *
     * @param x      The initial x-coordinate of the platform.
     * @param y      The initial y-coordinate of the platform.
     * @param width  The width of the platform.
     * @param height The height of the platform.
     */
    public Platform(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = 5;
    }

    /**
     * Moves the platform to the left by the current speed.
     */
    public void moveLeft() {
        x -= speed;
    }

    /**
     * Retrieves the x-coordinate of the platform's position.
     *
     * @return The current x-coordinate of the platform.
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the y-coordinate of the platform's position.
     *
     * @return The current y-coordinate of the platform.
     */
    public int getY() {
        return y;
    }

    /**
     * Retrieves the height of the platform.
     *
     * @return The height of the platform.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Retrieves the width of the platform.
     *
     * @return The width of the platform.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Retrieves the current speed at which the platform moves.
     *
     * @return The current speed of the platform's movement.
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Sets the speed at which the platform moves.
     *
     * @param speed The new speed for the platform's movement.
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }
}