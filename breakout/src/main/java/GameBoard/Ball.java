package GameBoard;

import com.example.App;
import com.example.PrimaryController;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import java.util.*;

// Change int to double
public class Ball {
    private double[] velo = new double[2], pos = new double[2];    //change inital velocity when program works
    private double speed = 3.5, comboBlockAmount = 5, comboSpeed = 1.5, combo = 0, normalSpeed = speed;   // change value to increase or decrease ball speed
    private Random rand = new Random();
    private double hue = rand.nextInt(256);
    private boolean star = false;
    private Rectangle rect;
    private boolean moving = false;
    private Paddle pad;
    private BlockGrid blockGrid;
    private HashSet<Block> blocks;

    // Variables for wall collision
    private double sideWall = 10, minWidth = 0 + sideWall, maxWidth = 672 - sideWall, minHeight = 92+34;
    

    private PrimaryController controller; 
    
    public Ball(double x, double y, Paddle pad, BlockGrid blockgrid, PrimaryController controller) {
        this.pad = pad;
        this.blockGrid = blockgrid;
        blocks = blockgrid.getBlockGrid();
        double dX = speed*.5*((Math.random()*2-1) > 0 ? 1 : -1);  // -1 < rand < 1
        double dY = -speed*.5;      // Ensures hypotenuse is always speed for any x
        setVelo(dX, dY);
        rect = new Rectangle(13,10);
        setPos(x,y);
        rect.setLayoutX(x);
        rect.setLayoutY(y);
        rect.setFill(Color.rgb(158, 158, 158));
        App.addElement(rect);
        this.controller = controller;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getNormalSpeed() {
        return normalSpeed;
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
            double comboMultiplierY = Math.signum(velo[1])*((combo/comboBlockAmount > 1 ? 1 : combo/comboBlockAmount)*comboSpeed), 
            comboMultiplierX = Math.signum(velo[0])*((combo/comboBlockAmount > 1 ? 1 : combo/comboBlockAmount)*comboSpeed);
            pos = new double[] {pos[0]+velo[0]+comboMultiplierX,pos[1]+velo[1]+comboMultiplierY};
        if (collidesWall() || collidesBlockHorizontal() || collidesSidePaddle()) {
            wallBounce();
        } else if (collidesRoof() || collidesBlockVertical() || collidesTopPaddle() || collidesTopShield()) {
            roofBounce();
        }
        rect.setLayoutX(pos[0]);
        rect.setLayoutY(pos[1]);
        } else {
            rect.setLayoutX(pos[0]);
            rect.setLayoutY(pos[1]);
        }

        if (star) {
            hue = hue > 255 ? hue-255 : hue+1;
            rect.setFill(Color.hsb(hue, 0.7f, 1.0f));
        } else {
            rect.setFill(Color.rgb(158, 158, 158));
        } 
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public void wallBounce() {
        velo[0] *= -1;
    }

    public void roofBounce() {
        velo[1] *= -1;
    }

    public double getCombo() {
        return combo;
    }

    public Rectangle getShape() {
        return rect;
    }

    public boolean collidesWall() {
        return (minWidth <= getXPos() && getXPos()+rect.getWidth() <= maxWidth)? false: true; 
    }

    public boolean collidesRoof() {
        if (minHeight <= getYPos()) {
            if (pos[1] < minHeight-1) {
                pos[1]+=2;
            }
            return false;
        } else {
            return true;
        }
    }

    public boolean collidesBlockHorizontal() {
        Iterator<Block> iterator = blocks.iterator();
        while (iterator.hasNext()) {
            Block b = iterator.next();
            if (b.getRect().intersects(pos[0] + velo[0], pos[1], rect.getWidth(), rect.getHeight())) {
                iterator.remove(); // Use iterator to remove the block safely
                blockGrid.removeBlock(b);
                controller.Addscore(b.getScoren());
                combo++;
                return !star && true;
            }
        }
        return false;
    }

    public boolean collidesBlockVertical() {
        Iterator<Block> iterator = blocks.iterator();
        while (iterator.hasNext()) {
            Block b = iterator.next();
            if (b.getRect().intersects(pos[0], pos[1] + velo[1], rect.getWidth(), rect.getHeight())) {
                iterator.remove(); // Use iterator to remove the block safely
                blockGrid.removeBlock(b);
                controller.Addscore(b.getScoren());
                combo++;
                return !star && true;
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
            combo = combo-3 >= 0 ? combo-3 : 0;
            pos[1] = paddleY - rect.getHeight();
            if (pos[0] + rect.getWidth()/2 <= paddleX + paddleWidth/2) {
                velo[0] = velo[0] > 0 ? -velo[0] : velo[0];
            } else {
                velo[0] = velo[0] < 0 ? -velo[0] : velo[0];
            }
        }
        return collides;
    }
    
    public boolean collidesTopShield() {
        ShieldPaddle shield = pad.getShield();
        if (shield != null) {
            double shieldX = shield.getRect().getLayoutX();
            double shieldY = shield.getRect().getLayoutY();
            double shieldWidth = shield.getRect().getWidth();
            double shieldHeight = shield.getRect().getHeight();
            boolean collides = pos[0] + rect.getWidth() > shieldX && pos[0] < shieldX + shieldWidth &&
                            pos[1] + rect.getHeight() > shieldY && pos[1] < shieldY + shieldHeight;
            if (collides) {
                combo = combo-3 >= 0 ? combo-3 : 0;
                pos[1] = shieldY - rect.getHeight();
                shield.kill();
                if (pos[0] + rect.getWidth()/2 <= shieldX + shieldWidth/2) {
                    velo[0] = velo[0] > 0 ? -velo[0] : velo[0];
                } else {
                    velo[0] = velo[0] < 0 ? -velo[0] : velo[0];
                }
            }
            return collides;
        } else {
            return false;
        }
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
