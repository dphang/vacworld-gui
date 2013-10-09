package vacworld.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Represents an obstacle tile, which simply displays an "X" graphic.
 * 
 * @author Daniel Phang
 * 
 */
public class ObstacleTile extends Tile {
    public static final Color OBSTACLE_COLOR = Color.BLACK;
    public static final int OBSTACLE_THICKNESS = 5;

    @Override
    public void draw(Graphics g, int x, int y, int width, int height) {
        super.draw(g, x, y, width, height);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(OBSTACLE_THICKNESS));
        g2d.setColor(OBSTACLE_COLOR);
        g2d.drawLine(x + OBSTACLE_THICKNESS, y + OBSTACLE_THICKNESS, x + width
                - OBSTACLE_THICKNESS, y + height - OBSTACLE_THICKNESS);
        g2d.drawLine(x + width - OBSTACLE_THICKNESS, y + OBSTACLE_THICKNESS, x
                + OBSTACLE_THICKNESS, y + height - OBSTACLE_THICKNESS);
    }

}
