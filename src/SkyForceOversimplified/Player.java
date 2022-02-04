package SkyForceOversimplified;

import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class Player extends GameObject {

    public final int MAX_HP = 30;

    // these variables indicating direction need to be boolean otherwise they would be inaccessible from lambda operators
    public static boolean left = false;
    public static boolean up = false;
    public static boolean right = false;
    public static boolean down = false;

    public Player(double x, double y) {
        super(x, y);
        hp = 30;
    }

    /**
     * move based on key presses in JavaFX
     * 
     * this method of moving is better than only using onKeyPressed because this allows fleunt movement,
     * this method functions by setting the character to moving until a certain key is released
     * 
     * @param scene the JavaFX scene to listen for key events, only works if scene is active on the window
     */
    public void checkKeys(Scene scene) {

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
            case W: Player.up = true; App.action += 1; App.actionTot += 1; break; 
            case S: Player.down = true; App.action += 1; App.actionTot += 1; break;
            case A: Player.left = true; App.action += 1; App.actionTot += 1; break;
            case D: Player.right = true; App.action += 1; App.actionTot += 1; break;
            case SPACE:
                App.action += 1; 
                Bullet.bulletQueue.add(new Bullet(this.x + 14, this.y + 10));
                break;
            default:
                break;
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
            case W: Player.up = false; break;
            case S: Player.down = false; break;
            case A: Player.left = false; break;
            case D: Player.right = false; break;
            default:
                break;
            }
        });
                
    }

    /**
     * method that controls the player box to move in various directions depending on its direction boolean variables
     */
    public void movement(){
        if(Player.left){
            if (this.x > 2) {
                this.shape.setTranslateX(this.shape.getTranslateX() - 8);
                this.x -= 8;
            }
        }
        if(Player.right){
            if (this.x < 768) {
                this.shape.setTranslateX(this.shape.getTranslateX() + 8);
                this.x += 8;
            }
        }
        if(Player.up){
            if (this.y > 2){
                this.shape.setTranslateY(this.shape.getTranslateY() - 8);
                this.y -= 8;
            }
        }
        if(Player.down){
            if (this.y < 768){
                this.shape.setTranslateY(this.shape.getTranslateY() + 8);
                this.y += 8;
            }
        }
    }

    /**
     * dynamically change a player's colour based on its proportion of currentHP / maxHP
     * at max health the player is yellow, at 0 health the player is black
     */
    public void setColor(){        
        int red = (int) Math.round(255 * (double) this.hp/(double)MAX_HP);
        int green = (int) Math.round(231 * (double) this.hp/(double)MAX_HP);
        int blue = (int) Math.round(97 * (double) this.hp/(double)MAX_HP);

        if(this.hp <= 0)    red = green = blue = 0;

        this.shape.setFill(Color.rgb(red, green, blue));
    }

    

}
