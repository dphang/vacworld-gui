package randomagent;

import java.util.Random;

import vacworld.GoForward;
import vacworld.ShutOff;
import vacworld.SuckDirt;
import vacworld.TurnLeft;
import vacworld.TurnRight;
import vacworld.VacPercept;
import agent.Action;
import agent.Agent;
import agent.Percept;

/**
 * A very simple random agent for test purposes.
 * 
 * @author Daniel Phang
 * 
 */
public class VacAgent extends Agent {
    private boolean seeDirt;
    private boolean seeObstacle;

    public VacAgent() {
        seeDirt = false;
        seeObstacle = false;
    }

    @Override
    public void see(Percept p) {
        VacPercept vp = (VacPercept) p;
        seeDirt = vp.seeDirt();
        seeObstacle = vp.seeObstacle();
    }

    @Override
    public Action selectAction() {
        Random r = new Random();
        float chance;

        if (seeObstacle) {
            chance = r.nextFloat();
            if (chance < 0.5) {
                return new TurnLeft();
            } else {
                return new TurnRight();
            }
        } else if (seeDirt) {
            return new SuckDirt();
        } else {
            chance = r.nextFloat();
            if (chance < 0.2) {
                return new TurnLeft();
            } else if (chance < 0.4) {
                return new TurnRight();
            } else if (chance < 0.95) {
                return new GoForward();
            } else {
                return new ShutOff();
            }
        }
    }

    @Override
    public String getId() {
        return "Random Agent";
    }
}
