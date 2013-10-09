package vacworld.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import vacworld.VacuumState;

/**
 * This class renders the world map for the Vacuum World environment.
 * 
 * @author Daniel Phang
 * 
 */
public class Map extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = -2335932731849582209L;

    public static final ObstacleTile OBSTACLE = new ObstacleTile();
    public static final DirtTile DIRT = new DirtTile();
    public static final Tile CLEAR = new Tile();
    public static final Robot ROBOT = new Robot();

    public static final int TILE_SIZE = 50;

    private VacuumState state;
    private boolean showGridLines;

    /**
     * Default constructor. Note that this method does not initialize the map to
     * be used.
     */
    public Map(boolean showGridLines) {
        super();
        this.showGridLines = showGridLines;
    }

    /**
     * Initializes this map with the given VacuumState.
     * 
     * @param state
     */
    public void init(VacuumState state) {
        this.state = state;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.clearRect(0, 0, getWidth(), getHeight());
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        drawMap(g);
        drawRobot(g);
    }

    /**
     * Draw our robot to the specified Graphics object.
     * 
     * @param g
     */
    private void drawRobot(Graphics g) {
        if (state != null) {
            int x = state.getAgentX() * TILE_SIZE;
            int y = state.getAgentY() * TILE_SIZE;

            ROBOT.draw(g, x, y, TILE_SIZE, TILE_SIZE, state.getAgentDir());
        }
    }

    /**
     * Draw our map to the specified Graphics object.
     * 
     * @param g
     */
    private void drawMap(Graphics g) {
        if (state != null) {
            int width = state.getWidth();
            int height = state.getHeight();

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int x = i * TILE_SIZE;
                    int y = j * TILE_SIZE;
                    Tile tile;
                    if (state.hasObstacle(i, j)) {
                        tile = OBSTACLE;
                    } else if (state.hasDirt(i, j)) {
                        tile = DIRT;
                    } else {
                        tile = CLEAR;
                    }
                    tile.draw(g, x, y, TILE_SIZE, TILE_SIZE);
                }
            }

            if (showGridLines) {
                // Draw grid lines
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.GREEN);
                g2d.setStroke(new BasicStroke(1));

                // Draw vertical lines
                for (int i = 0; i < width + 1; i++) {
                    int x = i * TILE_SIZE;
                    int y = height * TILE_SIZE;

                    if (i == width) { // Last grid line should be on the inside
                        x--;
                    }
                    g2d.drawLine(x, 0, x, y);
                }

                // Draw horizontal lines
                for (int i = 0; i < height + 1; i++) {
                    int x = width * TILE_SIZE;
                    int y = i * TILE_SIZE;

                    if (i == height) { // Last grid line should be on the inside
                        y--;
                    }
                    g2d.drawLine(0, y, x, y);
                }
            }
        }
    }

    /**
     * Set whether grid lines should be overlayed on the map.
     * 
     * @param showGridLines
     */
    public void setShowGridLines(boolean showGridLines) {
        this.showGridLines = showGridLines;
    }
}
