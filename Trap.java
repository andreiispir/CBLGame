/**
 * The Trap class represents a trap object used in a game. Traps have a position (x, y),
 * width, height, and a speed at which they move.
 */
public class Trap {
    private int x;      // The x-coordinate of the trap's position.
    private int y;      // The y-coordinate of the trap's position.
    private int speed;  // The speed at which the trap moves.
    private int width;  // The width of the trap.
    private int height; // The height of the trap.

    /**
     * Constructs a new Trap object with the specified position, width, and height.
     * The trap is initially assigned a default speed of 5 units per step.
     *
     * @param x      The initial x-coordinate of the trap.
     * @param y      The initial y-coordinate of the trap.
     * @param width  The width of the trap.
     * @param height The height of the trap.
     */
    public Trap(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = 5;
    }

    /**
     * Retrieves the x-coordinate of the trap's position.
     *
     * @return The current x-coordinate of the trap.
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the y-coordinate of the trap's position.
     *
     * @return The current y-coordinate of the trap.
     */
    public int getY() {
        return y;
    }

    /**
     * Retrieves the current speed at which the trap moves.
     *
     * @return The current speed of the trap's movement.
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Retrieves the width of the trap.
     *
     * @return The width of the trap.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Retrieves the height of the trap.
     *
     * @return The height of the trap.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Moves the trap to the left by the current speed.
     */
    public void moveLeft() {
        x -= speed;
    }
}