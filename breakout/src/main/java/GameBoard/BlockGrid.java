/// By Victor & Sebastian
/// Model for the grid made of blocks
package GameBoard;

import javafx.scene.paint.Color;
import java.util.*;

import com.example.App;

public class BlockGrid {
    private HashSet<Block> blockGrid = new HashSet<Block>(); // Set containing all blocks on the grid
    private int m; // Width of the grid (number of blocks horizontally)
    private int n; // Height of the grid (number of blocks vertically)
    private Random rand = new Random(); // Random number generator for block generation
    private Paddle pad; // Paddle object interacting with blocks

    public BlockGrid(int difficulty,Paddle pad) {
        switch(difficulty) {
            case 0:
                n = 5;
                m = 5;
                break;

            case 1:
                n = 7;
                m = 10;
                break;
            
            case 2:
                n = 10;
                m = 20;
                break;
        }
        this.pad = pad;
        createField();
    }
    /// Creates the block grid with random power-ups and color patterns
    private void createField() {
        double margin = 10; // Margin around the grid
        double offset = 5;  // Spacing between blocks

        double blockWidth = (672 - 2 * margin - offset * (m + 1)) / m;
        double blockHeight = ((970 / 4) - offset * (n + 1)) / n;
        Color col;
        for (int i = 0; i < n; i++) {
                switch ((i/2)%8) {
                    case 0:
                        col = Color.rgb(231,100,154);
                        break;
                    
                    case 1:
                        col = Color.rgb(252,79,81);
                        break;
                    
                    case 2:
                        col = Color.rgb(248,123,65);
                        break;

                    case 3:
                        col = Color.rgb(243,211,42);
                        break;

                    case 4:
                        col = Color.rgb(82,189,85);
                        break;

                    case 5:
                        col = Color.rgb(69,69,229);
                        break;

                    case 6:
                        col = Color.rgb(140,77,243);
                        break;

                    case 7:
                        col = Color.rgb(44,240,239);
                        break;
                
                    default:
                        col = Color.WHITE;
                        break;
                }
            for (int j = 0; j < m; j++) {
                double x = margin + offset * (j + 1) + blockWidth * j;
                double y = 970 / 4 + offset * (i + 1) + blockHeight * i;
                if (rand.nextInt(100) < 10) {
                    blockGrid.add(new Powerup(x, y, blockWidth, blockHeight, rand.nextInt(5),pad));
                } else {
                    blockGrid.add(new Block(x, y, blockWidth, blockHeight, col));
                }
                
            }
        }
    }

    public int getBlockGridHeight() {
        return n;
    }

    public int getBlockGridWidth() {
        return m;
    }

    public HashSet<Block> getBlockGrid() {
        return blockGrid;
    }

    public void removeBlock(Block block) {
        block.kill(); // Perform any necessary cleanup on the block
        blockGrid.remove(block); // Remove block from the set
        block = null; // Nullify reference for garbage collection
    }
    
}
