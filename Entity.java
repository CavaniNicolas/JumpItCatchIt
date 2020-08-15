public class Entity {
    /** coordinates, speed and acceleration variables */
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

    /** creates the next moment coordinates */
    public void move() {
        speedX += accelX;
        speedY += accelY;
        x += speedX;
        y += speedY;
    }

    /** set x and y acceleration */
    public void setAcceleration(int accelX, int accelY) {
        this.accelX = accelX;
        this.accelY = accelY;
    }

    /** set x and y speed */
    public void setSpeed(int speedX, int speedY) {
        this.speedX = speedX;
        this.speedY = speedY;
    }
}