# Choreo

Choreo is a path planning library that uses path-optimization to create time-constrained autonomous paths for the autonomous period. Learning to integrate Choreo into our codebase is important as autonomous, the first 15 seconds of the match, can be the make or break of a high-scoring teleop match.

## Choreo Structure

The following is the stack that is involved to integrate with Choreo:

### Choreo GUI

![Choreo Hero Home Page](/Assets/choreo-hero.png)

The Choreo GUI is the multiplatform Tauri (React + Rust) app that allows you to create and save the paths. It's an independent app from Visual Studio or WPILib (although for the 2025 season it might be bundled in WPILib).

### [Sleipnir](https://github.com/SleipnirGroup/Sleipnir)

Sleipnir, written in C++23, is a "a linearity-exploiting sparse nonlinear constrained optimization problem solver that uses the interior-point method." What this really means is that based on multiple constraints and variables (some listed [here](https://sleipnirgroup.github.io/Choreo/document-settings/robot-configuration/)), the problem-solver is able to constrain them while optimizing the path time. 8033 was the first team to run Choreo at a competition (Chezy Champs, where we saw very reliable paths and great potential in constrast to what we used to use: PathPlanner). 

### Communication: From Path to Trajectory

The Choreo GUI communicates with Sleipnr and receives results back (as Sleipnir runs through step iterations). First, Choreo sends all the project robot data and the paths as data to Choreo's backend. Choreo's backend is able to communicate with Sleipnir, which generates the trajectory. While the trajectories are generating, each iteration of the trajectory (which theoretically gets closer to a more optimized solution) is communicated back to Choreo, where the GUI can show a preliminary trajectory. Finally, the trajectory is finalized, Choreo displays it and autosaves the project. 

## Official Choreo Docs

The official Choreo [usage docs](https://sleipnirgroup.github.io/Choreo/usage/editing-paths/) give the best instructions on how to use the Choreo GUI app for creating these paths.


## Choreo Java Integration

To integrate Choreo into our Java codebase, first follow the instructions to add Choreo to the project via vendordeps online. See full instructions [here](https://sleipnirgroup.github.io/Choreo/choreolib/installation/).

After installing via vendordeps, we need to integrate the Choreo auto swerve command within our SwerveSubsystem. We integrate it in the SwerveSubsystem because the `choreoSwerveCommand` consumes our drive subsystem to correctly drive. 

Reference the [Choreo Java Usage](https://sleipnirgroup.github.io/Choreo/choreolib/usage/) for a general understanding of the usage of `choreoSwerveCommand`.

A few notes while setting up the `choreoSwerveCommand`:
1. Make sure to set your PIDControllers's PID values or have a designated day to test out PID values for possible new motors.

2. Choreo already accounts for field mirroring, so we generally don't need to worry about field flipping in the GUI, but we need to flip it in our code as follows:

```java
public Command runChoreoTraj(ChoreoTrajectory traj, boolean resetPose) {
    return choreoFullFollowSwerveCommand(
            traj,
            () -> pose,
            Choreo.choreoSwerveController(
                new PIDController(1.5, 0.0, 0.0), // don't copy these values, test these on your own. These values didn't work perfectly for our season.
                new PIDController(1.5, 0.0, 0.0),
                new PIDController(3.0, 0.0, 0.0)),
            (ChassisSpeeds speeds) -> this.runVelocity(speeds),
            () -> {
                Optional<Alliance> alliance = DriverStation.getAlliance();
                return alliance.isPresent() && alliance.get() == Alliance.Red;
            },
            this)
        .beforeStarting(
            Commands.runOnce(
                    () -> {
                        if (DriverStation.getAlliance().isPresent() // Checking if the `DriverStation` before accessing (with `get()`) so we don't need to handle possible Exceptions back up the stack (where `runChoreoTraj` is called).
                            && DriverStation.getAlliance().get().equals(Alliance.Red)) {
                        setPose(traj.getInitialState().flipped().getPose());
                        } else {
                        setPose(traj.getInitialPose());
                        }
                    })
                .onlyIf(() -> resetPose));
}
```
