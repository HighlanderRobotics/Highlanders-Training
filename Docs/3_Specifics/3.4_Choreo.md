# Choreo

Choreo is a library that helps us create autonomous paths.
Each path is composed of waypoints (places that we would like the robot to hit along the way) and constraints (limitations of the path or robot).
Choreo allows us to find the most optimal path that gets to all those waypoints, while also staying within those constraints.

## Choreo GUI

<img src="../../Assets/choreo.png" alt="Choreo screenshot" width="600"/>

This is where we create and save the paths. 
It's an independent app from Visual Studio or WPILib.
More extensive documentation can be found [here](https://choreo.autos/), but generally you'll just click on the field to add waypoints (usually at pickup/scoring locations), add any constraints from the top toolbar, and click the Generate Path button to generate a file for the path that we can then use.

So what happens when you hit Generate?

## Numerical Optimization
You might have done optimization problems in classes like HMA/precalc or Calculus (it's okay if you haven't), where you're trying to find the minimum/maximum amount of something (say I'm trying to maximize my backyard's area) given some constraint (I only have 100 feet of fence).
Choreo uses the same basic idea of modeling our real life problem (and its constraints) with equations and mathematically finding the best solution.
There are infinite ways to go from point A to point B, but we want to find the fastest way for our robot to do so.
In other words, time is a constraint.
We also need to consider other constraints, like our particular robot's maximum acceleration, velocity, motor torque, etc.

Choreo uses the Sleipnir solver (which in turn uses CasADi, a numerical optimization software) to calculate the fastest path.
At a high level, the solver takes the waypoints and repeatedly "guesses" better ways to interpolate between those waypoints.
(This is what's happening when the path sort of balloons around after you click the generate button).
If you're interested in learning more about how this works under the hood, check out [Sleipnir](https://github.com/SleipnirGroup/Sleipnir).

## Why Choreo?

8033 used to use [PathPlanner](https://pathplanner.dev/), which instead uses [Bezier splines](https://en.wikipedia.org/wiki/Bézier_curve).
However, these splines aren't able to take real-life constraints into account, so there would often be a discrepancy between what was "supposed" to work and what actually happened because the spline would require the robot to do something it couldn't physically do.

## Choreolib Installation

First, follow the [instructions](https://choreo.autos/choreolib/getting-started/) to add the Choreo vendordep.

As of 2025, this is how we set up Choreo (and other auto things) in code.
This may change, so leads should ensure they keep this page up to date.
Check the [docs](https://choreo.autos/) for more info.

There are 3 major files that concern autos: `Autos.java`, `SwerveSubsystem.java`, and `Robot.java`.

### `Autos.java`

This file will contain an `AutoFactory`, which is the class Choreo uses to create auto trajectories.
The constructor of this file will look something like this:

```Java
public Autos(
      SwerveSubsystem swerve
      // other subsystems
      ) {
    this.swerve = swerve;
    // other subsystems
    factory =
        new AutoFactory(
            swerve::getPose, // A function that returns the current field-relative Pose2d of the robot
            swerve::resetPose, // A function that receives a field-relative Pose2d to reset the robot's odometry to.
            swerve.choreoDriveController(), // A function that receives the current SampleType and controls the robot. More info below
            true, // If this is true, when on the red alliance, the path will be mirrored to the opposite side, while keeping the same coordinate system origin.
            swerve, // The drive Subsystem to require for AutoTrajectory Commands.
            (traj, edge) -> { // Log trajectories
              if (Robot.ROBOT_TYPE != RobotType.REAL)
                Logger.recordOutput(
                    "Choreo/Active Traj",
                    DriverStation.getAlliance().isPresent()
                            && DriverStation.getAlliance().get().equals(Alliance.Blue)
                        ? traj.getPoses()
                        : traj.flipped().getPoses());
            });
  }
```
The rest of the file has methods that return a `Command` corresponding to each auto path.
They'll generally look like this:

```Java
  public Command autoPath() {
    final var routine = factory.newRoutine("Auto Path"); // Uses the AutoFactory to create a new routine
    final var traj = routine.trajectory("AutoPath"); // Uses that routine to load a new trajectory. This should match what the name of the trajectory is in the Choreo app
    routine.active().whileTrue(Commands.sequence(traj.resetOdometry(), traj.cmd())); // When the routine starts, the pose will first reset and then the trajectory will be run
    // May include other trigger bindings (scoring, intaking, etc)
    return routine.cmd(); // Returns command to run this routine
  }
```
Choreolib relies heavily on `Triggers`, which will not be discussed here.
Refer to the [WPILib docs](https://docs.wpilib.org/en/stable/docs/software/commandbased/binding-commands-to-triggers.html) for more *or potentially another article here?*

You'll notice one of the parameters above is the `choreoDriveController()` method.
This is in `SwerveSubsystem`.

### `SwerveSubsystem.java`

The `SwerveSample` class is used by Choreo to represent the robot's state (position, velocity, acceleration, and the forces applied to each swerve module) at a point in time along the trajectory.
The `choreoDriveController()` method returns a function that "consumes" (takes as a parameter) a `SwerveSample` and drives to it.
Because different teams might have different ways of doing that driving (for example we've added logging statements), the implementation is left up to teams.

```Java

/**
   * This function bypasses the command-based framework because Choreolib handles setting
   * requirements internally. Do NOT use outside of ChoreoLib
   *
   * @return a Consumer that runs the drivebase to follow a SwerveSample with PID feedback, sample
   *     target vel feedforward, and module force feedforward.
   */
  @SuppressWarnings("resource") // This is here because otherwise it gets angry about the pid controllers not being closed
  public Consumer<SwerveSample> choreoDriveController() {
    // PID controllers for x, y, and theta
    final PIDController xController = new PIDController(5.0, 0.0, 0.0);
    final PIDController yController = new PIDController(5.0, 0.0, 0.0);
    final PIDController thetaController =
        new PIDController(constants.getHeadingVelocityKP(), 0.0, 0.0);
    thetaController.enableContinuousInput(-Math.PI, Math.PI);

    return (sample) -> { // returns a new function that takes in a sample and then does all the stuff inside the brackets
      final var pose = getPose(); // Gets the current pose of the robot on the field

      Logger.recordOutput(
          "Choreo/Target Pose",
          new Pose2d(sample.x, sample.y, Rotation2d.fromRadians(sample.heading)));
      Logger.recordOutput(
          "Choreo/Target Speeds Field Relative",
          new ChassisSpeeds(sample.vx, sample.vy, sample.omega));

      var feedback =
          new ChassisSpeeds(
              xController.calculate(pose.getX(), sample.x),
              yController.calculate(pose.getY(), sample.y),
              thetaController.calculate(pose.getRotation().getRadians(), sample.heading)); // Calculates how to get from its current pose to the target pose in the sample
      var speeds =
          ChassisSpeeds.fromFieldRelativeSpeeds(
              new ChassisSpeeds(sample.vx, sample.vy, sample.omega).plus(feedback), getRotation()); // Adds what we just calculated ^ to the chassis speeds specified by the sample

      Logger.recordOutput("Choreo/Feedback + FF Target Speeds Robot Relative", speeds);

      // Drives with the chassis speeds we just calculated
      this.drive(
          speeds,
          false,
          useModuleForceFF ? sample.moduleForcesX() : new double[4],
          useModuleForceFF ? sample.moduleForcesY() : new double[4]);
    };
  }
```

### `Robot.java`

We have several different auto options, so picking one is handled in `Robot.java` with the `LoggedDashboardChooser<Command>` which puts a dropdown list on the dashboard.
We then add all the methods in `Autos.java` that return our auto paths to that chooser in the `addAutos()` method, which is triggered on startup or by an alliance change.

```Java
private final Autos autos;
private final LoggedDashboardChooser<Command> autoChooser = new LoggedDashboardChooser<>("Autos");

public Robot() {
    //...
    RobotModeTriggers.autonomous()
        .whileTrue(Commands.defer(() -> autoChooser.get().asProxy(), Set.of())); // Starts whatever auto command is selected in the chooser once the autonomous period starts

    new Trigger(
            () -> { // alliance changing stuff
              var allianceChange = !DriverStation.getAlliance().equals(lastAlliance);
              lastAlliance = DriverStation.getAlliance();
              return allianceChange && DriverStation.getAlliance().isPresent();
            })
        .onTrue(
            Commands.runOnce(() -> addAutos()));
}

private void addAutos() {
    autoChooser.addOption("Auto Path", autos.getAutoPath()); // adds all our auto options to the chooser so they can be selected
    //...
}
```