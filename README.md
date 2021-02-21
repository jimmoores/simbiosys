simbiosys
=========

![screenshot of simbiosys running on a Mac](screenshot.png)
SIMulation of BIOlogical SYStem is an Artificial Life program that simulates Organisms using small virtual machines 
driven by programs are initially random, but that evolve by mutating and recombining in an open-ended way.  Energy
is input into the system via a 'BASK' instruction that lets the Organism increase its energy store by an amount 
relative to the current seasonal conditions.  These conditions will vary over time to apply some selective pressure
to the world (weaker organisms will die off to make room for new offspring).

# Building
To build, you'll need Maven installed.  Then simply run:
```bash
mvn install
```
and any necessary dependencies will be downloaded and installed
# Running
To run the simulator, run:
```bash
mvn exec:java
```
and several windows should appear.  Sometimes the initial randomised set of organisms will all due off quickly.
If this happens, just quit and run it again.  Eventually a population should take off and reproduce, filling the
world.