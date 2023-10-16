public class Coin {
    private int x;
    private int y;
    private boolean visible;
    private int speed;

    public Coin(int x, int y) {
        this.x = x;
        this.y = y;
        this.visible = true;
        this.speed = 5;
    }

    public int getSpeed() {
        return speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void moveLeft() {
        x -= speed;
    }
    
}

