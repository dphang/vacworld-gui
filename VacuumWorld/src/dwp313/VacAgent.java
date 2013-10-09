/**
 * Author:      Daniel Phang
 * Class:       CSE431 Intelligent Agents
 * Professor:   Jeff Heflin
 * Date:        September 19, 2013
 * Assignment:  Program #1
 * Environment: Java 1.6
 */

package dwp313;

import vacworld.VacPercept;
import agent.Action;
import agent.Agent;
import agent.Percept;

/**
 * This deceptively simple agent is more complex than it looks. It uses two
 * objects.
 * 
 * The first is an InternalState object, which is a database that stores
 * internal information such as the agent's relative position and direction, as
 * well as information about all states.
 * 
 * The second object is a Planner object, which effectively generates plans that
 * the agent should follow. The planner keeps a reference to the internal state
 * so it can process information from it, and decide the best course of action.
 * 
 * All the agent does is update the internal state with its percepts, and get
 * the next action it should perform from the Planner.
 * 
 * STRATEGY: The agent uses a combination of a HashMap as well as heuristic
 * exploration algorithms to determine which squares it should explore.
 * 
 * Exploration algorithm: as the agent explores the room, it indexes or "sees"
 * all adjacent squares around it (even though it technically only has true
 * vision of its own square and any obstacles in front.). The algorithm
 * basically picks the best possible indexed square (that's not already
 * explored). This is based on a heuristic that computes both Manhattan distance
 * and turn costs using simple Vector algebra.
 * 
 * Even though two squares may have the same Manhattan cost, they may have
 * different turn costs. For example, it is better for this agent to move
 * forward instead of turning around.
 * 
 * Pathfinding algorithm: this essentially uses an A* search to find the best
 * path to the unexplored location found above. It also uses the same heuristic
 * above that computes both Manhattan distance and turn costs. This returns a
 * path of Vector2 positions.
 * 
 * A third algorithm then converts this path into an efficient plan of Turn and
 * GoForward actions.
 * 
 * @author Daniel Phang
 * 
 */
public class VacAgent extends Agent {
    /*
     * Internal state used by the vacuum cleaner agent. This contains
     * information such as the agent's position, direction, and map information.
     */
    private InternalState iState;

    /*
     * This Planner object helps the agent choose what its next action should
     * be.
     */
    private Planner planner;

    /**
     * The Vacuum cleaning agent consists of just an internal state and a
     * planner, but of course it's more complicated under the hood.
     */
    public VacAgent() {
        iState = new InternalState();
        planner = new Planner(iState);
    }

    @Override
    public void see(Percept p) {
        // Check that the percept is the right type
        if (!(p instanceof VacPercept)) {
            System.out.println("Percept is not of type VacPercept");
            return;
        }

        // Update the internal state with the percept seen.
        iState.update((VacPercept) p);
    }

    @Override
    public Action selectAction() {
        return planner.nextAction();
    }

    /**
     * The next generation of vacuum cleaners, VAC-E!
     */
    @Override
    public String getId() {
        return "VAC-E";
    }
}
