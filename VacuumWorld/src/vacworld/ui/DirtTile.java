package vacworld.ui;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents a dirt tile.
 * 
 * @author Daniel Phang
 * 
 */
public class DirtTile extends Tile {
    public static final Color DIRT_COLOR = new Color(153, 102, 0);

    @Override
    public void draw(Graphics g, int x, int y, int width, int height) {
        super.draw(g, x, y, width, height);
        g.setColor(DIRT_COLOR);
        g.fillOval(x, y, (int) (width * 0.4), (int) (height * 0.4));
        g.fillOval(x + (int) (width * 0.5), y + (int) (width * 0.2),
                (int) (width * 0.3), (int) (height * 0.4));
        g.fillOval(x + (int) (width * 0.1), y + (int) (width * 0.5),
                (int) (width * 0.3), (int) (height * 0.3));
    }

}
