package GameBoard;

import com.example.App;
import com.example.PrimaryController;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import java.util.*;

// Change int to double
public class Ball {
    private double[] velo = new double[2];    //change inital velocity when program works
    private double[] pos = new double[2];
    private double speed = 3.5;   // change value to increase or decrease ball speed
    private Rectangle rect;
    private boolean moving = false;
    private Paddle pad;
    private BlockGrid blockGrid;
    private HashSet<Block> blocks;
    private double comboBlockAmount = 5, comboSpeed = 1.5, combo = 0;

    // temp variables move to App.java
    private double sideWall = 10;
    private double minWidth = 0 + sideWall;
    private double maxWidth = 672 - sideWall;
    private double minHeight = 92+34;
    private PrimaryController controller; 
    
    public Ball(double x, double y, Paddle pad, BlockGrid blockgrid, PrimaryController controller) {
        this.pad = pad;
        this.blockGrid = blockgrid;
        blocks = blockgrid.getBlockGrid();
        double dX = speed*.5*((Math.random()*2-1) > 0 ? 1 : -1);  // -1 < rand < 1
        double dY = -speed*.5;      // Ensures hypotenuse is always speed for any x
        setVelo(dX, dY);
        rect = new Rectangle(0,0,13,10);
        setPos(x,y);
        rect.setFill(Color.rgb(158, 158, 158));
        App.addElement(rect);
        this.controller = controller;
    }

    public void setMoving(boolean input) {
        moving = input;
    }

    public boolean isMoving() {
        return moving;
    }

    public double getSpeed() {
        return speed;
    }

    public void setVelo(double dX, double dY) {
        velo[0] = dX;
        velo[1] = dY;
    }

    public double[] getVelo() {
        return velo;
    }

    public double getXVelo() {
        return velo[0];
    }

    public double getYVelo() {
        return velo[1];
    }

    public void setPos(double x, double y) {
        pos[0] = x;
        pos[1] = y;
    }

    public double[] getPos() {
        return pos;
    }

    public double getXPos() {
        return pos[0];
    }
    
    public double getYPos() {
        return pos[1];
    }

    public void nextPos() {
        if (moving) {
            double comboMultiplierY = Math.signum(velo[1])*((combo/comboBlockAmount)*comboSpeed), 
            comboMultiplierX = Math.signum(velo[0])*((combo/comboBlockAmount)*comboSpeed);
            pos = new double[] {pos[0]+velo[0]+comboMultiplierX,pos[1]+velo[1]+comboMultiplierY};
        if (collidesWall() || collidesBlockHorizontal() || collidesSidePaddle()) {
            wallBounce();
        } else if (collidesRoof() || collidesBlockVertical() || collidesTopPaddle()) {
            roofBounce();
        }
        rect.setLayoutX(pos[0]);
        rect.setLayoutY(pos[1]);
        } else {
            rect.setLayoutX(pos[0]);
            rect.setLayoutY(pos[1]);
        }
    }

    public void resetPosition() { // When lost a life the ball will reset and you press space again to start 
        this.setPos(pad.getX() + pad.getLength() / 2 - 13 / 2, pad.getY() - 30); 
        this.setMoving(false); 
    }

    public void wallBounce() {
        velo[0] *= -1;
    }

    public void roofBounce() {
        velo[1] *= -1;
    }

    public Rectangle getShape() {
        return rect;
    }

    public boolean collidesWall() {
        return (minWidth <= getXPos() && getXPos()+rect.getWidth() <= maxWidth)? false: true; 
    }

    public boolean collidesRoof() {
        return (minHeight <= getYPos() )? false: true;
    }

    public boolean collidesBlockHorizontal() {
        for (Block b : blocks) {
            if (b.getRect().intersects(pos[0]+velo[0],pos[1],rect.getWidth(),rect.getHeight())) {
                blocks.remove(b);
                blockGrid.removeBlock(b);
                controller.Addscore(b.getScoren()); 
                combo = combo < comboBlockAmount ? combo+1 : combo;
                return true;
            }
        }
        return false;
    }

    public boolean collidesBlockVertical() {
        for (Block b : blocks) {
            if (b.getRect().intersects(pos[0],pos[1]+velo[1],rect.getWidth(),rect.getHeight())) {
                blocks.remove(b);
                blockGrid.removeBlock(b);
                controller.Addscore(b.getScoren());
                combo = combo < comboBlockAmount ? combo+1 : combo;
                return true;
            }
        }
        return false;
    }
    
    public boolean collidesTopPaddle() {
        double paddleX = pad.getX();
        double paddleY = pad.getY();
        double paddleWidth = pad.getObject().getWidth();
        double paddleHeight = pad.getObject().getHeight();
    
        boolean collides = pos[0] + rect.getWidth() > paddleX && pos[0] < paddleX + paddleWidth &&
                           pos[1] + rect.getHeight() > paddleY && pos[1] < paddleY + paddleHeight;
        if (collides) {
            combo = combo-2 >= 0 ? combo-2 : 0;
            pos[1] = paddleY - rect.getHeight();
            if (pos[0] + rect.getWidth()/2 <= paddleX + paddleWidth/2) {
                velo[0] = velo[0] > 0 ? -velo[0] : velo[0];
            } else {
                velo[0] = velo[0] < 0 ? -velo[0] : velo[0];
            }
        }
        return collides;
    }
    
    public boolean collidesSidePaddle() {
        boolean collides = pad.getObject().intersects(pos[0] + velo[0], pos[1], rect.getWidth(), rect.getHeight());
        if (collides) {
            combo = 0;
            if (pos[0] + velo[0] < pad.getX()) {
                pos[0] = pad.getX() - rect.getWidth();
            } else {
                pos[0] = pad.getX() + pad.getLength();
            }
        }
        return collides;
    }
    
}
