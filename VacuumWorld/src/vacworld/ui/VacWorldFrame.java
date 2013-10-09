package vacworld.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import vacworld.VacuumState;
import vacworld.VacuumWorld;

/**
 * Represents the Vacuum World GUI.
 * 
 * @author Daniel Phang
 * 
 */
public class VacWorldFrame extends JFrame {

    private static final long serialVersionUID = -7659865770458522413L;
    private static final double DEFAULT_INTERVAL = 2;
    private static final double MAX_INTERVAL = 10;
    private static final double MIN_INTERVAL = 0.01;
    private static final String RUNNING = "RUNNING";
    private static final String STOPPED = "STOPPED";

    // Represents the VacuumWorld map.
    private Map map;

    private String agentName;
    private JPanel contentPane;
    private JTextField fieldInterval;
    private JTextField fieldSeed;
    private JButton btnRun;
    private JButton btnStep;
    private JButton btnStop;
    private JButton btnScore;
    private JButton btnReset;
    private VacuumWorld world;

    // Timer for starting/stopping simulation
    private Timer timer;

    private JLabel lblPerceptString;
    private JLabel lblActionString;
    private JLabel lblPositionString;
    private JLabel lblStatus;
    private JCheckBox chckbxShowGridLines;

    /**
     * Create the frame.
     */
    public VacWorldFrame() {

        // Generated code for child components
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 450);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu help = new JMenu("Help");

        JMenuItem mntmAbout = new JMenuItem("About");
        mntmAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInfoMessage();
            }
        });

        help.add(mntmAbout);
        menuBar.add(help);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        btnRun = new JButton("Run");
        btnRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                run();
            }
        });
        btnRun.setBounds(379, 11, 89, 23);
        contentPane.add(btnRun);

        btnStep = new JButton("Step");
        btnStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                step();
            }
        });
        btnStep.setBounds(379, 45, 89, 23);
        contentPane.add(btnStep);

        fieldInterval = new JTextField();
        fieldInterval.setText(String.valueOf(DEFAULT_INTERVAL));
        fieldInterval.setBounds(488, 138, 86, 20);
        contentPane.add(fieldInterval);
        fieldInterval.setColumns(10);

        JLabel lblRunInterval = new JLabel("Run Delay (s)");
        lblRunInterval.setBounds(379, 141, 89, 14);
        contentPane.add(lblRunInterval);

        btnStop = new JButton("Stop");
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop();
            }
        });
        btnStop.setBounds(485, 11, 89, 23);
        contentPane.add(btnStop);

        btnReset = new JButton("Reset");
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
            }
        });
        btnReset.setBounds(485, 45, 89, 23);
        contentPane.add(btnReset);

        btnScore = new JButton("Score");
        btnScore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showScore();
            }
        });
        btnScore.setBounds(379, 79, 89, 23);
        contentPane.add(btnScore);

        map = new Map(false);
        map.setBounds(10, 11, 350, 350);
        contentPane.add(map);

        JLabel lblSeed = new JLabel("Seed");
        lblSeed.setBounds(379, 172, 89, 14);
        contentPane.add(lblSeed);

        fieldSeed = new JTextField();
        fieldSeed.setText("");
        fieldSeed.setColumns(10);
        fieldSeed.setBounds(488, 169, 86, 20);
        contentPane.add(fieldSeed);

        JLabel lblAgentPosition = new JLabel("Agent Information");
        lblAgentPosition.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblAgentPosition.setBounds(379, 197, 185, 23);
        contentPane.add(lblAgentPosition);

        JLabel lblPercept = new JLabel("Last Percept:");
        lblPercept.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblPercept.setBounds(379, 229, 89, 14);
        contentPane.add(lblPercept);

        JLabel lblAction = new JLabel("Last Action:");
        lblAction.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblAction.setBounds(379, 254, 89, 14);
        contentPane.add(lblAction);

        JLabel lblPosition = new JLabel("Position:");
        lblPosition.setFont(new Font("Tahoma", Font.BOLD, 11));
        lblPosition.setBounds(379, 279, 89, 14);
        contentPane.add(lblPosition);

        JLabel lblSimulationStatus = new JLabel("Simulation Status");
        lblSimulationStatus.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblSimulationStatus.setBounds(379, 315, 155, 14);
        contentPane.add(lblSimulationStatus);

        lblStatus = new JLabel(STOPPED);
        lblStatus.setForeground(Color.RED);
        lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblStatus.setBounds(379, 345, 89, 14);
        contentPane.add(lblStatus);

        lblPerceptString = new JLabel("");
        lblPerceptString.setBounds(488, 229, 86, 14);
        contentPane.add(lblPerceptString);

        lblActionString = new JLabel("");
        lblActionString.setBounds(488, 254, 86, 14);
        contentPane.add(lblActionString);

        lblPositionString = new JLabel("");
        lblPositionString.setBounds(488, 279, 86, 14);
        contentPane.add(lblPositionString);

        chckbxShowGridLines = new JCheckBox("Show grid lines");
        chckbxShowGridLines.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                map.setShowGridLines(e.getStateChange() == ItemEvent.SELECTED);
                repaint();
            }
        });
        chckbxShowGridLines.setBounds(379, 108, 97, 23);
        contentPane.add(chckbxShowGridLines);

        // Set properties
        this.setTitle("Vacuum World Simulator");
        this.setResizable(false);

        // Initialize timer for running simulation automatically
        timer = new Timer((int) (DEFAULT_INTERVAL * 1000),
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ae) {
                        step();
                    }
                });
        timer.setInitialDelay(0);
    }

    /**
     * Set the VacuumWorld this GUI represents.
     * 
     * @param world
     */
    public void setWorld(VacuumWorld world) {
        this.world = world;
    }

    /**
     * Initialize the map component by using the current world's state.
     */
    public void initMap() {
        VacuumState state = world.getState();
        this.map.init(state);
    }

    /**
     * Show the error message when an invalid interval is given.
     */
    private void showIntervalErrorMessage() {
        JOptionPane.showMessageDialog(null, String.format(
                "Please enter a valid interval (%.2f - %.2f).", MIN_INTERVAL,
                MAX_INTERVAL), "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Show the error message when an invalid seed is given.
     */
    private void showSeedErrorMessage() {
        JOptionPane.showMessageDialog(null,
                "Please enter a valid seed (should be an integer).", "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Show version and author information.
     */
    private void showInfoMessage() {
        JOptionPane
                .showMessageDialog(
                        null,
                        "Version 1.0, September 15, 2013\nWritten by Daniel Phang for Lehigh University's CSE 431 Fall 2013 class.",
                        "About", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Update simulation status text (e.g "RUNNING" or "STOPPED")
     * 
     * @param running
     */
    private void updateStatusText(boolean running) {
        if (running) {
            lblStatus.setText(RUNNING);
            lblStatus.setForeground(Color.GREEN);
        } else {
            lblStatus.setText(STOPPED);
            lblStatus.setForeground(Color.RED);
        }
    }

    /**
     * Update agent information displayed in this GUI.
     */
    private void updateAgentInfo() {
        VacuumState state = world.getState();
        lblPerceptString.setText(world.getCurrentPercept().toString());
        lblActionString.setText(world.getCurrentAction().toString());
        lblPositionString
                .setText(String.format("(%d, %d, %s)", state.getAgentX(),
                        state.getAgentY(), state.getAgentDirString()));

    }

    /**
     * Reset all agent information displayed to blank values.
     */
    private void resetAgentInfo() {
        lblPerceptString.setText("");
        lblActionString.setText("");
        lblPositionString.setText("");

    }

    /**
     * Run this simulation (called by clicking the run button).
     * 
     * @author Daniel Phang
     */
    private void run() {
        if (!timer.isRunning()) {
            try {
                double delay = Double.parseDouble(fieldInterval.getText());
                if (delay >= MIN_INTERVAL && delay <= MAX_INTERVAL) {
                    timer.setDelay((int) (delay * 1000));
                    timer.restart();
                    updateStatusText(true);
                    setFieldsEditable(false);
                    repaint();
                } else {
                    showIntervalErrorMessage();
                }
            } catch (Exception e) {
                showIntervalErrorMessage();
            }
        }
    }

    /**
     * Reset the simulation.
     */
    public void reset() {
        // Stop timer
        timer.stop();

        // Reset environment state
        VacuumState initState;
        try {
            int randSeed = Integer.parseInt(fieldSeed.getText());
            initState = VacuumState.getRandomState(randSeed);
            System.out.println("State generated with seed " + randSeed);
            System.out.println();
        } catch (Exception e) {
            if (fieldSeed.getText().trim().equals("")) {
                initState = VacuumState.getInitState();
            } else {
                showSeedErrorMessage();
                return;
            }
        }
        world = new VacuumWorld();
        VacuumWorld.loadAgent(world, agentName);
        world.start(initState);
        initMap();

        // Update any text on the GUI
        updateStatusText(false);
        resetAgentInfo();
        setFieldsEditable(true);
        repaint();
    }

    /**
     * Step the simulation and update the GUI.
     */
    private void step() {
        world.step();
        updateAgentInfo();
        repaint();

        if (world.isComplete()) {
            stop();
            btnRun.setEnabled(false);
            btnStep.setEnabled(false);
            showScore();
        }
    }

    /**
     * Stop the simulation and update the GUI.
     */
    private void stop() {
        timer.stop();
        updateStatusText(false);
        setFieldsEditable(true);
        repaint();
    }

    /**
     * Set the agent class name to be used in the simulation.
     * 
     * @param agentName
     */
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    /**
     * Set fields to be editable or not (useful when running so the user doesn't
     * change them, expecting some change).
     * 
     * @param b
     */
    private void setFieldsEditable(boolean b) {
        fieldInterval.setEditable(b);
        fieldSeed.setEditable(b);
        btnStep.setEnabled(b);
        btnStop.setEnabled(!b);
        btnRun.setEnabled(b);
    }

    /**
     * Set the initial seed to be used (for example, if a seed was passed
     * through the command line)
     * 
     * @param seed
     */
    public void setSeed(int seed) {
        fieldSeed.setText(String.valueOf(seed));
    }

    /**
     * Show a popup containing score information.
     */
    private void showScore() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(os);
        world.printScore(ps);
        world.printScore(System.out);
        String output = "";
        output = os.toString();
        JOptionPane.showMessageDialog(null, output, "Score",
                JOptionPane.INFORMATION_MESSAGE);

    }
}
