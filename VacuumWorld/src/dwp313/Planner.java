/**
 * Author:      Daniel Phang
 * Class:       CSE431 Intelligent Agents
 * Professor:   Jeff Heflin
 * Date:        September 19, 2013
 * Assignment:  Program #1
 * Environment: Java 1.6
 */

package dwp313;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;

import vacworld.GoForward;
import vacworld.ShutOff;
import vacworld.SuckDirt;
import vacworld.TurnLeft;
import vacworld.TurnRight;
import agent.Action;

/**
 * This planner class helps our agent decide what action to take by creating a
 * plan for it to follow. These plans are based on searching for an unexplored
 * location to explore, and then finding the best path to it (using heuristics).
 * 
 * The plans are based on the best information known to the agent at the time
 * they are created. This class allows dynamic plan changing in the event an
 * obstacle
 * 
 * @author Daniel
 * 
 */
public class Planner {
    /* The action decider uses the agent's internal state to make decisions. */
    InternalState iState;

    /*
     * The plan holds any currently queued actions that the agent may decide to
     * take.
     */
    LinkedList<Action> plan;

    /**
     * Construct that initialized this object with a reference to some internal
     * state, as well as initializing a plan LinkedList.
     * 
     * @param iState
     */
    public Planner(InternalState iState) {
        this.iState = iState;
        this.plan = new LinkedList<Action>();
    }

    /**
     * Decides what action the agent should take by looking at its internal
     * state and returning an action.
     * 
     * @return the next Action for our agent to perform.
     */
    public Action nextAction() {
        if (iState.isTurnedOff()) {
            return null;
        }

        // If an obstacle, dirt, or bump was just seen/felt, we need to
        // dynamically change the plan.
        // For example, the agent might have been moving to a location but now
        // saw some dirt on the way, or maybe an obstacle (if it's plan was
        // based on the assumption that the obstacle wasn't there before.
        if (iState.isObstacleSeen() || iState.isDirtSeen()
                || iState.isFeltBump()) {
            plan.clear();
        }

        // If there's no current plan, build a new plan.
        if (plan.isEmpty()) {
            buildPlan();
        }

        // Return the next action in the plan
        Action next = plan.remove();

        // Update internal state according to what action are about to perform.
        // (e.g change relative position, direction, etc.)
        // Of course, this update might be invalid if the agent (for some
        // reason) bumps into an obstacle. However, we are careful to avoid
        // bumping into obstacle squares through dynamic plan changing.
        iState.update(next);

        return next;
    }

    /**
     * This builds a queue of actions an agent should take (i.e a plan)
     */
    private void buildPlan() {
        final Vector2 position = iState.getAgentPosition();
        if (iState.isLocationDirty(position)) { // If location is dirty, the
                                                // plan is simply to suck the
                                                // dirt up
            plan.add(new SuckDirt());
        } else {
            buildMovementPlan();
        }

        // If a plan is empty, presumably there was nothing to do (e.g no
        // movement plan), so we're done.
        if (plan.isEmpty()) {
            plan.add(new ShutOff());
        }
    }

    /**
     * This builds a movement plan for the agent by adding a sequence of
     * movement actions as a plan. The two steps involved are:
     * 
     * 1) Find an unexplored position to explore.
     * 
     * 2) Find the best path to get to that position.
     * 
     * This uses heuristics to do both parts.
     */
    private void buildMovementPlan() {
        // Find an unexplored location to move to.
        // Note that this method tries to find the closest position based on a
        // heuristic (turn cost and Manhattan distance).
        final Vector2 unexplored = findUnexploredPosition();

        // All unexplored locations exhausted, we are done exploring all
        // squares so simply return
        if (unexplored == null) {
            return;
        }

        // Use an A* search to find the best path from the current position to
        // the unexplored position.
        LinkedList<Vector2> path = findPath(unexplored);

        // First element in the path is the starting location, so we can remove
        // it
        if (!path.isEmpty()) {
            path.remove();
        }

        Vector2 current = iState.getAgentPosition();
        Vector2 next;
        int currentDirection = iState.getAgentDirection();
        int nextDirection;

        // Iterate through each location on the path, and build the correct
        // sequence of actions in the plan.
        while (!path.isEmpty()) {
            next = path.remove();

            // Get the correct direction between the two tiles.
            // Since current and next are adjacent, the direction number is
            // found by converting (next - current) vectors to an integer.
            nextDirection = Vector2.vectorToDirection(Vector2
                    .sub(next, current));

            if (nextDirection != currentDirection) { // We only need to turn
                                                     // when directions are
                                                     // different.

                int diff = nextDirection - currentDirection;
                if (diff > 3) { // Handle negative directions by making them
                                // positive instead.
                    diff = diff - 4;
                } else if (diff < 0) {
                    diff = diff + 4;
                }
                if (diff == 1) {
                    plan.add(new TurnRight());
                } else if (diff == 2) {
                    plan.add(new TurnRight());
                    plan.add(new TurnRight());
                } else if (diff == 3) { // Turn left to avoid having to turn
                                        // right three times
                    plan.add(new TurnLeft());
                }
            }

            // Finally, move forward.
            plan.add(new GoForward());

            // Go to the next position and direction in the path
            current = next;
            currentDirection = nextDirection;
        }
    }

    /**
     * Finds an unexplored position to explore. This specific implementation
     * tries to return the closest unexplored position possible using a
     * heuristic estimation. This takes into account things like whether the
     * agent needs to turn, how many moves it needs to make, and whether the
     * position has an obstacle.
     * 
     * @return a Vector2 representing an unexplored position. If no unexplored
     *         position is found, return null.
     */
    private Vector2 findUnexploredPosition() {
        // World map and pair variables
        final HashMap<Vector2, LocationInformation> map = iState.getWorldMap();
        Entry<Vector2, LocationInformation> pair;

        int lowestCost = Integer.MAX_VALUE; // Lowest cost initialized to the
                                            // maximum value possible
        int cost;
        Vector2 lowestCostPosition = null;

        // Iterate through the map and find the lowest cost position using a
        // heuristic
        Iterator<Entry<Vector2, LocationInformation>> it = map.entrySet()
                .iterator();
        Vector2 pos;

        while (it.hasNext()) {
            pair = it.next();
            pos = pair.getKey();
            if (!iState.isLocationExplored(pos)
                    && !iState.isLocationObstacle(pos)) { // We need both
                                                          // conditions because
                                                          // obstacle positions
                                                          // are treated as
                                                          // unexplored by the
                                                          // internal state.
                cost = Heuristics.estimateCost(iState.getAgentPosition(), pos,
                        iState.getAgentDirection());

                if (cost < lowestCost) {
                    lowestCost = cost;
                    lowestCostPosition = pair.getKey();
                }
            }

        }

        return lowestCostPosition;
    }

    /**
     * Performs a slightly modified A star search to find the shortest known
     * path between the current position and the given goal position. This uses
     * the specific heuristic given in the Heuristics class.
     * 
     * @param goal
     * @return
     */
    private LinkedList<Vector2> findPath(Vector2 goal) {
        // Get start position and direction
        Vector2 start = iState.getAgentPosition();
        int currentDirection = iState.getAgentDirection();

        // Tentative G score
        int tentativeG;

        // Allows us to build the optimal path later
        HashMap<Vector2, Vector2> cameFrom = new HashMap<Vector2, Vector2>();

        // f and g scores as HashMap data structures
        final HashMap<Vector2, Integer> g = new HashMap<Vector2, Integer>();
        final HashMap<Vector2, Integer> f = new HashMap<Vector2, Integer>();

        // Initialize the f and g scores of each position in the worldMap to 0
        HashMap<Vector2, LocationInformation> worldMap = iState.getWorldMap();
        Iterator<Entry<Vector2, LocationInformation>> it = worldMap.entrySet()
                .iterator();
        while (it.hasNext()) {
            Vector2 pos = it.next().getKey();
            f.put(pos, 0);
            g.put(pos, 0);
        }

        // Open and closed sets. Note that the open set uses a priority queue on
        // f-scores.
        PriorityQueue<Vector2> open = new PriorityQueue<Vector2>(11,
                new Comparator<Vector2>() { // This comparator ensures the
                                            // priority queue is sorted by
                                            // lowest f values.
                    public int compare(Vector2 a, Vector2 b) {
                        return f.get(a) - f.get(b);
                    }
                });

        Set<Vector2> closed = new HashSet<Vector2>();
        g.put(start, 0);
        f.put(start,
                g.get(start)
                        + Heuristics
                                .estimateCost(start, goal, currentDirection));

        // Linked List to keep track of neighbors, as well as Vector2 elements
        // for the neighbor and current node being expanded.
        LinkedList<Vector2> neighbors;
        Vector2 neighbor;
        Vector2 current;

        // Add the start node to the open set
        open.add(start);

        while (!open.isEmpty()) {
            current = open.remove(); // This is always the lowest f-cost value
                                     // since open is a priority queue

            // Goal state reached, contruct the path
            if (current.equals(goal)) {
                return constructPath(cameFrom, goal);
            }

            // Move current to the closed set
            open.remove(current);
            closed.add(current);

            // Look at all neighbors of the current position
            neighbors = iState.neighbors(current);
            Iterator<Vector2> it2 = neighbors.iterator();
            while (it2.hasNext()) {
                neighbor = it2.next();

                // Compute the direction between the neighbor and
                // current node being expanded
                currentDirection = Vector2.vectorToDirection(Vector2.sub(
                        neighbor, current));

                tentativeG = g.get(current)
                        + InternalState.adjacentCost(current, neighbor,
                                currentDirection);
                if (closed.contains(neighbor) && tentativeG >= g.get(neighbor)) {
                    continue;
                }

                // Update the appropriate data structures with new values if a
                // better path was found
                if (g.get(neighbor) == 0 || tentativeG < g.get(neighbor)) {
                    cameFrom.put(neighbor, current);
                    g.put(neighbor, tentativeG);

                    // This uses the heuristic that estimates cost based on both
                    // turns and Manhattan distance.
                    f.put(neighbor,
                            g.get(neighbor)
                                    + Heuristics.estimateCost(neighbor, goal,
                                            currentDirection));
                    if (!open.contains(neighbor)) {
                        open.add(neighbor);
                    }
                }
            }

        }

        // No path was found, return null
        return null;

    }

    /**
     * Recursively constructs the optimal path found by the A* algorithm.
     * 
     * @param cameFrom
     *            a HashMap that maps vectors to the previous vector (on the
     *            optimal path
     * @param current
     * @return
     */
    private static LinkedList<Vector2> constructPath(
            HashMap<Vector2, Vector2> cameFrom, Vector2 current) {
        LinkedList<Vector2> p;
        if (cameFrom.containsKey(current)) { // This essentially follows the
                                             // HashMap recursively until there
                                             // are no more Vector2 members
                                             // left.
            p = constructPath(cameFrom, cameFrom.get(current));
            p.add(current);
            return p;
        } else {
            p = new LinkedList<Vector2>();
            p.add(current); // Note that this step adds the starting position in
                            // the path
            return p;
        }
    }
}
