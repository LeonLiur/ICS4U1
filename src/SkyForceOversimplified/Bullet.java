package SkyForceOversimplified;

import java.util.*;

import javafx.scene.paint.Color;

public class Bullet extends GameObject {

    // static queue that stores all the bullets not yet blitted onto the screen
    public static Queue<Bullet> bulletQueue = new LinkedList<>();

    /**
     * class constructor that calls the super constructor GameObject(double x, double y, int speed, double direction, int radius), thus creating a circle
     * @param x x-coordinate of the bullet
     * @param y y-coordinate of the bullet
     */
    public Bullet(double x, double y) {
        super(x, y, 250, 0, 4);
        this.shape.setFill(Color.DARKSLATEGRAY);
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
     * helper method that checks if this bullet is expired, which means if it is outside the screen
     * @return true for expired, false for not expired
     */
    public boolean checkExpiry(){
        return this.x <= -5 || this.x >= App.WIDTH || this.y >= App.HEIGHT || this.y <= -5;
    }
}
