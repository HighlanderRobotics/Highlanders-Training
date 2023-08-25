# AdvantageKit

## AdvantageKit is a logging framework developed by team 6328

### What is logging?

Logging is recording some or all of the state (think the current values of variables, inputs and outputs, and what Commands are running,) of the robot so that we can play it back later.

### Why log?

Logging helps with debugging by letting us see exactly what the robot was doing when it broke.
This is especially useful at competition when we have limited time and testing ability to diagnose problems.
For instance, at Houston 2023 we had an issue in our second quals match where our grabber stopped responding to input.
Using the logs of that match, we saw that the sensor readings of the grabber had stopped responding, which suggested that the CAN bus to the grabber had broken.

### Why AdvantageKit?

AdvantageKit logs every input and output to and from the robot, so that we can perfectly recreate the state of the robot in the log or with a simulator.
Logging everything means we never have to say "if only we had logged one more thing!" It also means that we can simulate how the code might have responded differently with changes.
One of the examples 6328 uses is when they adjusted the way they tracked vision targets based on a match log that revealed a problem, then used the log replay to confirm that the change fixed the vision issue.
AdvantageKit is a mature and developed framework for this type of logging, and has been used on Einstein by 6328 and 3476.
The framework should continue to be maintained for the forseeable future and by using the framework instead of a custom solution we reduce our overhead for using a system like this.
AdvantageKit is closely integrated with AdvantageScope, a log and sim viewer built by 6328

### Drawbacks

Running this amount of logging has performance overhead on the rio, using valuable cpu time each loop.
Logging also requires a non-insignificant architecture change to our codebase by using an IO layer under each of our subsystems.
While this does require some additional effort to write subsystems, it also makes simulating subsystems easier so it is a worthwhile tradeoff.

We have not yet done significant on-robot r&d with AdvantageKit and need to assess the performance impacts of using it.

### Resources

- [AdvantageKit repository](https://github.com/Mechanical-Advantage/AdvantageKit)
  - See the README for this repo for docs
- [AdvantageScope log viewer](https://github.com/Mechanical-Advantage/AdvantageScope)
- [6328 logging talk](https://youtu.be/mmNJjKJG8mw)

### Examples

- [6328 2023 code](https://github.com/Mechanical-Advantage/RobotCode2023)
- [3476 2023 code](https://github.com/FRC3476/FRC-2023)
- [8033 2023 AdvantageKit port](https://github.com/HighlanderRobotics/Charged-Up/tree/advantagekit)

### Exercises

- Install AdvantageKit into your kitbot project following [this tutorial](https://github.com/Mechanical-Advantage/AdvantageKit/blob/45d8067b336c7693e63ee01cdeff0e5ddf50b92d/docs/INSTALLATION.md).
You do not need to modify the subsystem file yet, we will do that as part of the simulation tutorial.

### Notes

- See also the [Simulation](Simulation.md) article for more on the IO layer structure
- _When we have log files, put a link to one here as an example_
