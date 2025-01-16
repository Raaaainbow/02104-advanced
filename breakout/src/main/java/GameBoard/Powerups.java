package GameBoard;

import javafx.scene.shape.Rectangle;

import com.example.App;

import javafx.scene.paint.Color;

public class Powerups extends Block {
    private double[] pos = new double[2]; 
    private double[] velo = new double[2]; 
    private Rectangle rect;
    private int type; // Type of powerup 
    
    public Powerups(double x, double y, double width, double height, int type) {
        super(x, y, width, height, Color.GREEN); // Powerup size
        this.pos[0] = x;
        this.pos[1] = y;
        this.type = type;
        
        rect = new Rectangle(x, y, width, height); // Powerup size
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

    public int getType() {
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

