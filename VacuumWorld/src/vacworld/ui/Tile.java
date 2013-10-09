package vacworld.ui;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents a generic tile. Other tiles inherit from this class.
 * 
 * @author Daniel Phang
 * 
 */
public class Tile {
    public static final Color TILE_COLOR = new Color(214, 217, 223);

    public void draw(Graphics g, int x, int y, int width, int height) {
        g.setColor(TILE_COLOR);
        g.fillRect(x, y, width, height);
    }
}
