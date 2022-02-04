package SkyForceOversimplified;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class GameObject {
    protected double x, y;
    protected double speed;
    protected double direction;
    protected double hp;
    protected Shape shape;

    // this constructor will be overloaded
    // default constructor because it is 'good practice'
    public GameObject(){

    }

    // constructor for rectangle
    public GameObject(double x, double y, int speed, double direction, int width, int height) {
        this.x = x;
        this.y = y;
        this.speed = speed/60.0;
        this.direction = direction;
        this.shape = new Rectangle(x, y, width, height);
    }

    // constructor for circle
    public GameObject(double x, double y, int speed, double direction, int radius){
        this.x = x;
        this.y = y;
        this.speed = speed/60.0;
        this.direction = Math.toRadians(direction);
        this.shape = new Circle(x, y, radius);
    }

    // constructor for only x and y
    public GameObject(double x, double y) {
        this.x = x;
        this.y = y;
        this.shape = new Rectangle(x, y, 30, 30);
    }

    // getter for shape
    public Shape getShape() {
        return this.shape;
    }

    // getter for hp
    public double getHp() {
        return this.hp;
    }

    // modified setter for hp, used to deduce hp from a GameObject
    public void loseHP(double hp) {
        this.hp -= hp;
    }
}
