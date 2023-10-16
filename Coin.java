public class Coin {
    private int x;
        private int y;
        private boolean visible;

        public Coin(int x, int y) {
            this.x = x;
            this.y = y;
            this.visible = true;
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
}

