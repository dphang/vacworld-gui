/**
 * Author:      Daniel Phang
 * Class:       CSE431 Intelligent Agents
 * Professor:   Jeff Heflin
 * Date:        September 19, 2013
 * Assignment:  Program #1
 * Environment: Java 1.6
 */

package dwp313;

/**
 * Heuristic functions used by our planner, such as manhattan distance, turn
 * proximity (e.g. how many turns the agent needs to make to go to that
 * direction. It's better for the agent to go forward than to to have to turn
 * around).
 * 
 * @author Daniel Phang
 * 
 */
public class Heuristics {
    /**
     * Calculates the Manhattan distance between two points.
     * 
     * @param start
     *            the start position.
     * @param end
     *            the end position.
     */
    public static int manHattanDistance(Vector2 start, Vector2 end) {
        int dx = start.getX() - end.getX();
        int dy = start.getY() - end.getY();

        return Math.abs(dx) + Math.abs(dy);
    }

    /**
     * Estimates the cost in terms of turning, i.e how much turning our robot
     * needs to do in order to face the new location.
     * 
     * @param start
     *            the starting position.
     * @param end
     *            the ending position.
     * @param direction
     *            the direction faced from the start position (e.g. the would-be
     *            agent direction).
     * @return
     */
    public static int turnCost(Vector2 start, Vector2 end, int direction) {
        Vector2 currentDirectionVector = Vector2.directionToVector(direction);
        Vector2 newDirectionVector = Vector2.sub(end, start);

        double angle = Vector2
                .angle(currentDirectionVector, newDirectionVector);

        if (angle == 0.0) {
            return 0;
        } else if (angle <= Math.PI / 2) {
            return 1;
        } else if (angle > Math.PI / 2) {
            return 2;
        } else { // Covers NaN case
            return 0;
        }
    }

    /**
     * Heuristic to estimate the cost between the two given positions. This
     * takes into account the estimated initial number of moves/turns needed to
     * get between the two positions. Moves are treated as twice as expensive as
     * turns.
     * 
     * @param start
     *            the starting position.
     * @param end
     *            the ending position.
     * @param direction
     *            the direction faced from the start position (e.g the would-be
     *            agent direction).
     * @return
     */
    public static int estimateCost(Vector2 start, Vector2 end, int direction) {
        int moveCost = manHattanDistance(start, end) * 2;
        int turnCost = turnCost(start, end, direction);

        return moveCost + turnCost;
    }
}
