vacworld-gui
============

A simple GUI for an intelligent agent simulation in the Vacuum World. This was adapted from a project for an artificial intelligence class.

The idea is simple: an agent needs to clean up all dirt in a two-dimensional grid. What's an efficient way to accomplish its goal while minimizing its actions?

##Usage

The GUI is pretty self-explanatory. You can run the simulation for different maps (using a random seed), step through the simulation, or run it at varying intervals.

I used vector graphics to display the Vacuum World.

###Agents

You can test using either `randomagent`, which is a very simple randomized agent, or `dwp313`, which is my heuristics-based solution. It performs pretty well in most of the maps I've tried.
