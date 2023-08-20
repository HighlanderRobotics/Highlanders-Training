# Kitbot Sim Tutorial

## Intro

Writing code is plenty of fun on its own, but its rough having to wait for mechanical to be done to test it.
When we only have 2-3 weeks of time (which we have to share with drive practice!) after the robot is built before our first competition, we need to be able to test before the robot is ready.
Luckily, we have a handy piece of code that to refactor to be simulateable!
In this tutorial we are going to rework your kitbot code to be easy to simulate and expand on.
In the end your code should be able to do this:

[Video of robot in sim](Assets/SimDemo.mp4)

## Code Walkthrough

### Moving to AdvantageKit

The first step of moving our standard Command-based code to a loggable, simulateable structure is to install AdvantageKit.
Luckily AdvantageKit has a handy guide on how to install it on an existing code base.
Follow [this walkthrough](https://github.com/Mechanical-Advantage/AdvantageKit/blob/main/docs/INSTALLATION.md).
Follow all the steps in the doc through adding the `@AutoLog` annotation.
You do not need to add the suggested block in `robotInit()`, instead use the one below.

```Java
Logger.getInstance().recordMetadata("ProjectName", "KitbotExample"); // Set a metadata value

    if (isReal()) {
      Logger.getInstance().addDataReceiver(new WPILOGWriter("/media/sda1/")); // Log to a USB stick
      Logger.getInstance().addDataReceiver(new NT4Publisher()); // Publish data to NetworkTables
      new PowerDistribution(1, ModuleType.kRev); // Enables power distribution logging
    } else {
      Logger.getInstance().addDataReceiver(new NT4Publisher()); // Publish data to NetworkTables
    }

    Logger.getInstance().start(); // Start logging! No more data receivers, replay sources, or metadata values may be added.
```

The suggested logging configuration is built around replaying logs, while we just want to simulate code.

Once you've completed this, it's time to break `DrivetrainSubsystem` appart using the IO layer structure.

### Refactoring `DrivetrainSubsystem`

Recall the [IO layer structure](https://github.com/Mechanical-Advantage/AdvantageKit/blob/main/docs/CODE-STRUCTURE.md) for robot code.
Right now we have one file for `DrivetrainSubsystem`.
Lets put it in a `Drivetrain` folder under `Subsystems` so that everything stays organized as we add more files.

![A screenshot showing the new folder](Assets/KitbotExampleSimScreenshot0.png)

Next lets make a new file called `DrivetrainIO` in the same folder.
Remember that this file will define all the methods we use to interact with the hardware on the drivetrain.
You can use the "Create a new class/command" option when you right click on the folder to speed this up.
Make sure to change the type of `DrivetrainIO` to interface, not class.

```Java
package frc.robot.Subsystems.Drivetrain;

public interface DrivetrainIO {}
```

Inside of this interface lets define our `IOInputs` class.
Remember this is a container for all of the inputs (sensor readings) and outputs (motor commands) for this mechanism.

```Java
public static class DrivetrainIOInputs {}
```

Let's start by logging the output voltage of each side of the drivetrain.

```Java
public static class DrivetrainIOInputs {
    public double leftOutputVolts = 0.0;
    public double rightOutputVolts = 0.0;
}
```

Notice how we name our fields with units.
**It is very important to include units in all IOInputs variable names**.
It is very easy to assume different units in different places, which can cause frustrating and hard to find bugs.

Next lets log the current velocity of each side of the drivetrain.

```Java
public static class DrivetrainIOInputs {
    public double leftOutputVolts = 0.0;
    public double rightOutputVolts = 0.0;

    public double leftVelocityMetersPerSecond = 0.0;
    public double rightVelocityMetersPerSecond = 0.0;
}
```

Notice the units.
This field is useful both for being able to look at drivetrain performance but also for being able to track our position over time.
In fact, let's add a field for the current position of each side.

```Java
public static class DrivetrainIOInputs {
    // Snip
    public double leftPositionMeters = 0.0;
    public double rightPositionMeters = 0.0;
}
```

These values are useful for odometry.
Odometry is a way to track the robots position on the field by measuring how much each wheel is turning.
On a real robot we use it for auto so that we can draw paths on a map of the field instead of guessing where the robot should go.
For this sim we will use odometry to give us a position that we can use to visualize the robot on a fake field.

On a real robot it can be useful to log motor current draw and temperature.
Current draw is the amount of power the motor is actually using to try to reach it's desired output, and is measured in Amperes or amps.
The amount of power flowing through these motors also causes them to heat up over the course of a match.
When the motors get hotter, they get less efficient.
Logging the temparature of our motors can help us debug mechanisms which have problems near the end of a match.
Normally this issue is not too pronounced over the course of a single match.
However, in elims where we have a number of intense matches back to back or during drive practice where we run continuously for hours this can become an issue.
This simulation will not include temparature simulation, but its good to get into the habit of including it in your logged values.

```Java
public static class DrivetrainIOInputs {
    // Snip
    public double[] leftCurrentAmps = new double[0];
    public double[] leftTempCelsius = new double[0];
    public double[] rightCurrentAmps = new double[0];
    public double[] rightTempCelsius = new double[0];
}
```

Notice how these values arent doubles, but instead are arrays of doubles.
This is because we might have more than one motor on each side of the drivetrain, and we want to log the state of each of them.

Finally to finish this IOInputs class we need to add the `@AutoLog` annotation above it's definition.
This annotation automatically generates code to convert these values to a loggable format and back.
However it is limited to only certain types of values, so be careful when making IOInputs classes.
If you want to log other types than `@AutoLog` supports you can follow the [AdvantageKit docs example](https://github.com/Mechanical-Advantage/AdvantageKit/blob/main/docs/CODE-STRUCTURE.md#autolog-annotation) on it.
