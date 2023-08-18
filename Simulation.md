# Simulation

## Simulation is the process of running our robot code on a computer other than the robot to test it without being limited by physical hardware

Sometimes simulation includes physics simulation, where we use a mathematical model of a mechanism to predict how it will behave.
Sometimes this will be very accurate.
Sometimes it will make a lot of untrue assumptions about the mechanism.
Both are useful, since we will likely have to retune the mechanism one way or another once we have real hardware.
Deciding how much effort to put into a simulation model is a key part of the intuition you will build as you code more robots.

We have a number of tools at our disposal for simulation.
One is WPILibs built in desktop simulation.
This provides a way for us to send joystick inputs from a keyboard and run robot code on a computer.
This is the backbone of our simulation.

Then we have WPILibs simulation physics classes.
These are built around a state-space model of a class of mechanism, and exist for many common mechanisms.
We tend to use one or two of these per robot for major mechanisms.
If you want to learn more about this sort of modelling, look at the [control theory for FRC book](https://file.tavsys.net/control/controls-engineering-in-frc.pdf).

Running a model is useful, but even more useful is being able to visualize the output of the model.
[AdvantageScope](https://github.com/Mechanical-Advantage/AdvantageScope) is a tool developed by team 6328 to visualize data from live robots, simulated robots, and robot logs.
It provides a variety of formats to visualize data from line graphs to 3d field models to tables and more.
It is closely integrated with AdvantageKit but does not require it.

The final major tool is being able to swap between simulated inputs and outputs (io) and real io easily and correctly.
This is the real advantage of the AdvantageKit Subsystem -> IO -> IOImplementation structure.
By having a simulation and real IOImplementation for each mechanism we can easily test our code in sim and have minimal work to move it to real hardware.

### Resources

- Read through the [WPILib docs intro to simulation](https://docs.wpilib.org/en/stable/docs/software/wpilib-tools/robot-simulation/introduction.html).

### Examples

- The [kitbot example code](Examples/KitbotDemoSim/)

### Exercises

- Download and install [AdvantageScope](https://github.com/Mechanical-Advantage/AdvantageScope).
- Follow this tutorial to convert your kitbot code to AdvantageKit and simulation.

### Notes

- Structuring a codebase to be simulateable is not an easy task.
It is important to be familiar with the tools we have and stick to a clean structure to make it easy to simulate our code.
