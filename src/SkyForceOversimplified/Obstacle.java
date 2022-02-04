package SkyForceOversimplified;

import javafx.scene.paint.Color;

public class Obstacle extends GameObject{
    int MAX_HP;
    /**
     * constructor for this class, calls the super constructor GameObject(double x, double y, int speed, double direction, int width, int height)
     * with its speed, width, and height being random between 100, 150 and 30, 50
     * @param x     x-coordinate of the obstacle
     * @param y     y-coordinate of the obstacle
     * @param dir   direction of the obstacle
     */
    public Obstacle(int x, int y, double dir){
        super(x,
        y,
        100 + (int) Math.floor(Math.random() * 150),
        dir,
        30 + (int) Math.floor(Math.random() * 20), 
        30 + (int) Math.floor(Math.random() * 20));

        this.shape.setFill(Color.rgb(255, 41, 25));
    }

    /**
     * calculate the movement of the bullet based on the direction (stored in radians)
     * determine the x and y components of the vector using trig
     * move in y and x direction based on the components
     */
    public void move() {
        double ycomp = -this.speed * Math.cos(this.direction);
        double xcomp = this.speed * Math.sin(this.direction);
        this.shape.setTranslateY(this.shape.getTranslateY() + ycomp);
        this.y += ycomp;
        this.shape.setTranslateX(this.shape.getTranslateX() + xcomp);
        this.x += xcomp;
    }

    /**
     * helper method that checks if this obstacle collides with another GameObject
     * @param collidee object to check collision with
     * @return true for collision, false for no collision
     */
    public boolean collision(GameObject collidee){
        return collidee.shape.getBoundsInParent().intersects(this.getShape().getBoundsInParent());
    }

    /**
     * helper method that checks if this obstacle is expired, which means if it is outside the screen
     * @return true for expired, false for not expired
     */
    public boolean checkExpiry(){
        return this.x <= -50 || this.x >= App.WIDTH || this.y >= App.HEIGHT || this.y <= -50;
    }
}
