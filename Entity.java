public class Entity {
    protected int x;
    protected int y;
    protected int speedX;
    protected int speedY;
    protected int accelX;
    protected int accelY;

    public Entity(int x, int y, int speedX, int speedY, int accelX, int accelY) {
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.accelX = accelX;
        this.accelY = accelY;
    }

    public void move() {
        speedX += accelX;
        speedY += accelY;
        x += speedX;
        y += speedY;
    }

    public void setAcceleration(int accelX, int accelY) {
        this.accelX = accelX;
        this.accelY = accelY;
    }
}