package GameBoard;

import javafx.scene.shape.Rectangle;

import com.example.App;

import javafx.scene.paint.Color;

public class Powerups {
    private double[] pos = new double[2]; 
    private double[] velo = new double[2]; 
    private Rectangle rect;
    private String type; // Type of powerup 
    
    public Powerups(double x, double y, String type) {
        this.pos[0] = x;
        this.pos[1] = y;
        this.type = type;
        this.velo[0] = 0; // No horizontal movement
        this.velo[1] = 1.5; // Falling speed
        
        rect = new Rectangle(x, y, 20, 20); // Powerup size
        rect.setFill(Color.GREEN); 
        
        App.addElement(rect); 
    }

    public void move() {
        pos[0] += velo[0];
        pos[1] += velo[1];
        rect.setLayoutX(pos[0]);
        rect.setLayoutY(pos[1]);
    }

    public Rectangle getRect() {
        return rect;
    }

    public String getType() {
        return type;
    }
    
    public double[] getPos() {
        return pos;
    }

    public void setPos(double x, double y) {
        pos[0] = x;
        pos[1] = y;
    }
    // Større paddle 
// stjerne fra mario 
// Ekstra point 
// Usynlig væg
// Ekstra liv 
// Langsommere bold
// Ekstra bolde 


// Power downs: 
// Score bliver nulstillet 
// Paddle bliver mindre
// Mister et liv 
}

