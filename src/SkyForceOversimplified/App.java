package SkyForceOversimplified;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.*;

/**
 * @version 1.0
 * @author Leo Liu (https://github.com/LeonLiur/)
 * 
 * Oversimplified version of Sky Force made with JavaFX
 * Courtesy of: Sky Force Reloaded, Infinite Dreams
 * Courtesy of: Brick Breaker, Mr. Patel
 */
public class App extends Application{
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    private static Pane mainLayout;
    private static Scene scene;
    private static Player player;
    private static ArrayList<Bullet> bullets;               // ArrayList containing all the bullets on the screen
    private static ArrayList<Obstacle> obstacles;           // ArrayList containing all the obstacles on the screen
    private static ArrayList<Bullet> bulletRemove;          // ArrayList containing all the bullets to be removed
    private static ArrayList<Obstacle> obstaclesRemove;     // ArrayList containing all the obstacles to be removed
    private static int[] st;                                // segment tree to store range maximum APM
    public static Text apmInd;
    public static Text scoreInd;
    public static Text HPInd;
    public static Text peakInd;
    public static Text gg;
    public static long score;
    public static int hp = 30;
    public static int gameTime = 0;                         // counting how many frames has elapsed since the beginning of the game
    public static int freqCounter;                          // counting every 2.5s interval to set difficulty of the game 
    public static int action;                               // counting total actions (keybaord) during a given 2.5s timeframe
    public static int batch = 0;                            // number of 2.5s interval elapsed since beginning of the game, a BATCH is a time interval of 
    public static int peakApm;                              // global peak APM: highest APM achieved throughout entire game
    public static int localApm;                             // local peak APM: highest APM achieved within the last 10 seconds
    public static double avgApm = 0;                        // average APM: total actions / total gametime (in frames) * frame rate (65) * 60s
    public static AnimationTimer timer;
    public static int actionTot = 0;                        // counting total actions (keyboard) since the beginning of the game

    public static int difficulty;                           // difficulty represents how many obstacles generated per 2.5 seconds

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainLayout = new Pane();
        scene = new Scene(mainLayout, WIDTH, HEIGHT);
        player = new Player(400, 650);
        player.checkKeys(scene);

        // variable initialization
        bullets = new ArrayList<>();
        obstacles = new ArrayList<>();
        obstaclesRemove = new ArrayList<>();
        bulletRemove = new ArrayList<>();
        st = new int[100005];
        scoreInd = new Text(550, 50, "score: 0");
        apmInd = new Text(550, 60, "avg apm: 0.00");
        HPInd = new Text(550, 70, "cur hp: 30");
        peakInd = new Text(550, 80, "glob/loc peak apm: 0/0");
        gg = new Text(380, 390, "GG WP <3");

        // blitting all the textfields * display fields onto the screen
        mainLayout.getChildren().addAll(player.getShape(), scoreInd, apmInd, HPInd);
        
        freqCounter = 0;

        // animation timer to control the game loop, this timer approximately executes 65 times/second
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                ++gameTime;

                // calling methods to be executed within every frame
                renderBullets();    // method to render bullets
                renderObstacles();  // method to render obstacles
                renderPlayer();     // method to render players

                updateText();       // updating the text field on the screen with new statistics and information
                
                endgame();          // actively checking if the game has ended (indicated by a non-positive player hp value), if it has, then skip to the endscreen
            }
        };
        timer.start();

        primaryStage.setScene(scene);
        primaryStage.setTitle("Sky Force Oversimplified");
        primaryStage.show();
    }

    /**
     * method to keep the information displayed in text fields on the screen up-to-date
     * 
     * first removes the text fields from the screen, changes them to be updated, then blit them back onto the screen with the updated information.
     * @return none
     */
    private static void updateText(){
        mainLayout.getChildren().removeAll(scoreInd, apmInd, HPInd, peakInd);
        scoreInd = new Text(550, 50, "score: " + score);
        apmInd = new Text(550, 70, "avg apm: " + Math.round(avgApm * 100) / (double) 100);
        HPInd = new Text(550, 100, "cur hp: " + player.hp);
        peakInd = new Text(550, 80, "glob/loc peak apm: " + peakApm + "/" + localApm);
        mainLayout.getChildren().addAll(scoreInd, apmInd, HPInd, peakInd);
    }

    /**
     * method to render the bullets and display them onto the screen if they're not already drawn
     * 
     * 1) in the static Queue bulletQueue<Bullet>, which represent bullets not yet drawn on the screen, draw all elements from it onto the screen and add to bullets ArrayList
     * 2) iterate over the bullets ArrayList and call the move() and checkExpiry() function on each of them, if the bullet has expired, add it to the bulletRemove ArrayList to await being removed
     * 3) remove all elements in bulletRemove from bullets
     */
    private static void renderBullets(){
        // retrieving every element in the queue until it's empty to draw on the screen and add to bullets ArrayList
        while(!Bullet.bulletQueue.isEmpty()){
            Bullet b = Bullet.bulletQueue.poll();
            mainLayout.getChildren().add(b.getShape());
            bullets.add(b);
        }


        for(Bullet bullet : bullets){
            bullet.move();
            if(bullet.checkExpiry()){
                bulletRemove.add(bullet);
                mainLayout.getChildren().remove(bullet.getShape());
            }
        }
        bullets.removeAll(bulletRemove);
    }

    /**
     * method that controls the player aspect of the game
     * 
     * 1) dynamically changes the colour of the player based on the player's current health (in proportion with the player's maximum health)
     * 2) controls the movement of the player on the screen
     */
    private static void renderPlayer(){
        player.setColor();
        player.movement();
    }
    
    /**
     * method to render obstacles and compute their collisions with other GameObjects
     * 
     * 1) generate a new obstacle following the frequency(difficulty) variable
     * 2) after each batch, compute APM for the batch and update and query the segment tree accordingly
     * 3) compute collision
     *      i)  when [an obstacle collides with a bullet]
     *      ii) when [an obstacle collides with a player]
     */
    private static void renderObstacles(){
        // computing parameters for the current frame:
        // freqCounter indicates how many frames have passed after the last batch
        // difficulty indicates how many obstacles is generated per 2.5 seconds (163 frames)
        freqCounter += 1;
        int difficulty = 2 + (int) Math.ceil(gameTime / (double) 163);
        avgApm = actionTot / (double) gameTime * 65 * 60;               // also calculate the average APM for every frame, avgAPM = actions / framesTot * 65fps * 60s/min

        // this is a custom timer to make sure that every 163 frames or 2.5 seconds is the end of a batch
        if(freqCounter <= 163){

            // generate an obstacle if and only if we are in the right time to do so, for example, if the difficulty is 4, that means we need to generate 4 obstacles in 163 frames,
            // therefore, we should generate obstacles in frames 40, 80, 120 and 160, which turn out to be multiples of 40, which is represented by (163 / difficulty)
            // intuitively, this can be understood as since we want to generate x obstacles, we have to find 163/x equally spaced times to do so, which is represented by t = 163/x * k,
            // therefore, for every one of these times, t % (163/x) == 0
            if(freqCounter % (163 / difficulty) == 0){
                boolean flag  = false;
                
                int x = 0;
                int y = 0;

                // the flag is a security mechanism that ensures that obstacles won't generate on top of the players
                // if they do, then x and y for the obstacles are regenerated until they aren't
                while(!flag){
                    // the obstacle can be generated everywhere on the screen, therefore a random point with x and y axis
                    // each taking ar andom value between 0 and 750 (screen width - obstacle width)
                    x = (int) Math.floor(Math.random() * 750);
                    y = (int) Math.floor(Math.random() * 750);
                    flag = Math.abs(player.x - x) > 50 && Math.abs(player.y - y) > 50;
                }
                // instantiate a new Obstacle with its x and y coordinates and its direction calculated by the
                // calcDir() method in radians (see method calcDir() for more)
                Obstacle ob = new Obstacle(x, y, calcDir(player.x, player.y, x, y));

                // adding the newly instantiated obstacle to the screen and to the obstacles ArrayList
                mainLayout.getChildren().add(ob.getShape());
                obstacles.add(ob);
            }
        }else{
            // when a batch ends

            // update the APM registered from the current batch into the segment tree while increasing the batch number
            update(1, 10000, ++batch, action * 24, 1);
            
            // query the maximum APM within the entire segment tree
            peakApm = query(1, 10000, 1, batch, 1);

            // query the maximum APM within the last 4 batches in the segment tree
            localApm = query(1, 10000, Math.max(batch - 4, 1), batch, 1);

            // reset the variables freqCounter, which counts how many frames in the current batch, and action, which counts keyboard entries in the current batch
            freqCounter = 0;
            action = 0;
        }

        // iterating through every obstacle on the screen
        for(Obstacle obstacle : obstacles){
            obstacle.move();
            for(Bullet b : bullets){      
                // if it collides with a bullet, add points to the player and remove both of them
                if(obstacle.collision(b)){
                    score += 100;
                    System.out.println("[T+" + gameTime + "] +100 points for striking target");

                    // removing
                    bulletRemove.add(b);
                    mainLayout.getChildren().remove(b.getShape());
                    obstaclesRemove.add(obstacle);
                    mainLayout.getChildren().remove(obstacle.getShape());
                }
            }

            // if it expires, which means it's out of the screen, then remove it
            if(obstacle.checkExpiry()){
                obstaclesRemove.add(obstacle);
                mainLayout.getChildren().remove(obstacle.getShape());
                System.out.println("[T+" + gameTime + "] +10 points for dodging obstacle");
                score += 10;
            }

            // if it collides with the player, then remove it and remove HP from the player
            if(obstacle.collision(player)){
                player.loseHP(1);
                obstaclesRemove.add(obstacle);
                mainLayout.getChildren().remove(obstacle.getShape());
            }
        }

        bullets.removeAll(bulletRemove);
        obstacles.removeAll(obstaclesRemove);
    }

    /**
     * method calculating the direction towards which the generated obstacle need to move in.
     * this is done through taking the arctangent angle of their slope, which is calculated by rise/run = playerY - y / palyerX - x
     * a positive π/2 is added to compensate direction because the coordinate plane has been rotated due to the way that javaFX coordinates function
     * @param playerX x-coordinate of the player
     * @param playerY y-coordinate of the lpayer
     * @param x x-coordinate of the newly generated obstacle
     * @param y y-coordinate of the newly generated obstacle
     * @return
     */
    private static double calcDir(double playerX, double playerY, int x, int y) {
        // 2atan2 is used here because it gives the angle of the polar angle of a value, which is exactly what we need
        double angle = Math.atan2(playerY - y, playerX - x) + Math.PI / 2;
    
        // if this direction is negative, simply add 2π as that doesn't change the angle
        if(angle < 0){
            angle += 2 * Math.PI;
        }
    
        return angle;
    } 

    /**
     * method to check and engage in an endgame sequence
     */
    private static void endgame(){
        // the game ends if the player's HP reaches 0
        if(player.hp <= 0){
            mainLayout.getChildren().clear();                                                   // clear everything from screen
            scoreInd = new Text(550, 50, "score: " + score);
            apmInd = new Text(550, 70, "avg apm: " + Math.round(avgApm * 100)/100);
            HPInd = new Text(550, 100, "cur hp: " + player.hp);
            peakInd = new Text(550, 80, "glob/loc peak apm: " + peakApm + "/" + localApm);
            mainLayout.getChildren().addAll(scoreInd, apmInd, gg, peakInd);
            st = null;                                                                          // relax the segment tree to free up memory
            timer.stop();                                                                       // free the animation timer
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    // segment tree method to update a certain index to a certain value
    private static void update(int l, int r, int target, int value, int index){
        if(l == r){
            st[index] = value;
            return;
        }
        int mid = (l + r) >> 1;
        if(target <= mid)   update(l, mid, target, value, index * 2);
        else    update(mid + 1, r, target, value, index * 2 + 1);

        st[index] = Math.max(st[index * 2], st[index * 2 + 1]);
    }

    // segment tree method to query the maximum value between an interval [targetL, targetR]
    private static int query(int l, int r, int targetL, int targetR, int index){
        if(l > r || l > targetR || r < targetL) return 0;
        if(l >= targetL && r <= targetR)    return st[index];
        int mid = (l + r) >> 1;
        return Math.max(query(l, mid, targetL, targetR, index * 2), query(mid + 1, r, targetL, targetR, index * 2 + 1));
    }
}

