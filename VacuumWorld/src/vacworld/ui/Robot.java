package vacworld.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import vacworld.Direction;

/**
 * Represents our robot (inspired by the Roomba, of course).
 * 
 * @author Daniel Phang
 * 
 */
public class Robot {
    public static final Color BODY_COLOR = Color.RED;
    public static final Color DIRECTION_COLOR = Color.WHITE;
    private static final double FILL_RATIO = 0.75; // How much space the robot
                                                   // takes of its square

    public Robot() {

    }

    public void draw(Graphics g, int x, int y, int width, int height,
            int direction) {
        int newWidth = (int) (width * FILL_RATIO);
        int newHeight = (int) (height * FILL_RATIO);
        int newX = x + (width - newWidth) / 2;
        int newY = y + (height - newHeight) / 2;

        int centerX = newX + newWidth / 2;
        int centerY = newY + newHeight / 2;

        g.setColor(BODY_COLOR);
        g.fillOval(newX, newY, newWidth, newHeight);

        // Draw an arrow to indicate direction
        Graphics2D g2d = (Graphics2D) g;

        // Save old transformation
        AffineTransform old = g2d.getTransform();

        // Draw rotated directional arrow
        g2d.rotate(directionToAngle(direction), centerX, centerY);
        g2d.setColor(DIRECTION_COLOR);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(centerX - newWidth / 4, centerY, centerX, centerY
                - newHeight / 4);
        g2d.drawLine(centerX + newWidth / 4, centerY, centerX, centerY
                - newHeight / 4);

        // Go back to old transformation
        g2d.setTransform(old);
    }

    /**
     * This converts a Direction into the appropriate rotation angle.
     * 
     * @param direction
     *            a direction number.
     * @return
     */
    public static double directionToAngle(int direction) {
        if (direction == Direction.NORTH) {
            return 0;
        } else if (direction == Direction.WEST) {
            return -Math.PI / 2;
        } else if (direction == Direction.SOUTH) {
            return Math.PI;
        } else {
            return Math.PI / 2;
        }
    }
}
