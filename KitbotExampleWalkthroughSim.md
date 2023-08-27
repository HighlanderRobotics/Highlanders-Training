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

Next we need to add a method called `updateInputs` that takes in the `IOInputs` object and updates it based off of the new values from our sensors.
Because we are just defining the interface instead of the actual method, we can call it an `abstract` method.
`abstract` means that it won't have any default behaviour but will require our IO Implementations to implement it.

```Java
public abstract void updateInputs(DrivetrainIOInputs inputs);
```

Finally, lets add the methods that we use to interact with the hardware.
For this case it will just be a method `setVolts` to set the left and right output volts, like our `setVoltages` method that we have right now.

Now your `DrivetrainIO` file should look like this:

```Java
package frc.robot.Subsystems.Drivetrain;

import org.littletonrobotics.junction.AutoLog;

public interface DrivetrainIO {
    @AutoLog
    public static class DrivetrainIOInputs {
        public double leftOutputVolts = 0.0;
        public double rightOutputVolts = 0.0;

        public double leftVelocityMetersPerSecond = 0.0;
        public double rightVelocityMetersPerSecond = 0.0;

        public double leftPositionMeters = 0.0;
        public double rightPositionMeters = 0.0;

        public double[] leftCurrentAmps = new double[0];
        public double[] leftTempCelsius = new double[0];
        public double[] rightCurrentAmps = new double[0];
        public double[] rightTempCelsius = new double[0];
    }

    public abstract void updateInputs(DrivetrainIOInputs inputs);

    public abstract void setVolts(double left, double right);
}
```

Next lets add the IO Implementation.
This will be a file that defines the `updateInputs()` and `setVolts()` method.
Since we aren't writing this for real hardware, we are going to make `DrivetrainIOSim`.
On a real robot we would also have a `DrivetrainIOFalcon` or something similar, named after the type of hardware it is built with.

Start by making a new class called `DrivetrainIOSim` in the Drivetrain folder.

Then, add `implements DrivetrainIO` to the class declaration.
This is very similar to using `extends`, except for interfaces instead of classes.
This means we could have multiple interfaces it implements, instead of just one class.

```Java
public class DrivetrainIOSim implements DrivetrainIO {}
```

This will make vscode angry since `DrivetrainIOSim` needs to implement all the abstract methods in `DrivetrainIO`.
Hover over it, hit "quick fix", and click "add unimplemented methods".
Now you should have a template for both of these methods.

```Java
public class DrivetrainIOSim implements DrivetrainIO {

    @Override
    public void updateInputs(DrivetrainIOInputs inputs) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setVolts(double left, double right) {
        // TODO Auto-generated method stub

    }
}
```

Before we get into implementing these, we need to add the hardware to this IO Implementation.
Add in the falcons from `DrivetrainSubsystem`.

```Java
TalonFX leftFalcon = new TalonFX(Constants.drivetrainLeftFalconID);
TalonFX rightFalcon = new TalonFX(Constants.drivetrainRightFalconID);
```

Lets add all of the needed fields to `updateInputs`, even though we won't be able to set any of them yet.
One way to get all of the needed fields is to copy and paste the body of `DrivetrainIOInputs` into `updateInputs`.

```Java
@Override
public void updateInputs(DrivetrainIOInputs inputs) {
    public double leftOutputVolts = 0.0;
    public double rightOutputVolts = 0.0;

    public double leftVelocityMetersPerSecond = 0.0;
    public double rightVelocityMetersPerSecond = 0.0;

    public double leftPositionMeters = 0.0;
    public double rightPositionMeters = 0.0;

    public double[] leftCurrentAmps = new double[0];
    public double[] leftTempCelsius = new double[0];
    public double[] rightCurrentAmps = new double[0];
    public double[] rightTempCelsius = new double[0];
}
```

Then replace the `public` and types with `inputs.`

```Java
@Override
public void updateInputs(DrivetrainIOInputs inputs) {
    inputs.leftOutputVolts = 0.0;
    inputs.rightOutputVolts = 0.0;

    inputs.leftVelocityMetersPerSecond = 0.0;
    inputs.rightVelocityMetersPerSecond = 0.0;

    inputs.leftPositionMeters = 0.0;
    inputs.rightPositionMeters = 0.0;

    inputs.leftCurrentAmps = new double[0];
    inputs.leftTempCelsius = new double[0];
    inputs.rightCurrentAmps = new double[0];
    inputs.rightTempCelsius = new double[0];
}
```

Some of these fields we can get directly from a simulated falcon.
TalonFX comes with built-in simulation support, so we can treat it like a regular motor for the most part.
To get the simulated outputs we can call `talon.getSimState()` and store the results in a variable.
Do this in `updateInputs`.

```Java
@Override
public void updateInputs(DrivetrainIOInputs inputs) {
    var leftSimState = leftFalcon.getSimState();
    var rightSimState = rightFalcon.getSimState();
    // Snip
}
```

Now you can call `simState.getMotorVoltage()` to get the voltage for each motor.
Do this for the `left-` and `rightOutputVolts`.

```Java
@Override
public void updateInputs(DrivetrainIOInputs inputs) {
    // Snip
    inputs.leftOutputVolts = leftSimState.getMotorVoltage();
    inputs.rightOutputVolts = rightSimState.getMotorVoltage();
    // Snip
}
```

We can also get the current from each of the motor's sim states.
There are two types of current we can measure: `TorqueCurrent` and `SupplyCurrent`.
`SupplyCurrent` is like the available current for the motor, and is what the breaker measures.
`TorqueCurrent` is the amount of current the motor is actually using, and is important for heat management.
`TorqueCurrent` is more useful for our purposes since it shows the actual current usage of the motor.

```Java
@Override
public void updateInputs(DrivetrainIOInputs inputs) {
    // Snip
    inputs.leftCurrentAmps = new double[] {leftSimState.getTorqueCurrent()};
    inputs.leftTempCelsius = new double[0];
    inputs.rightCurrentAmps = new double[] {rightSimState.getTorqueCurrent()};
    inputs.rightTempCelsius = new double[0];
}
```

This information is useless without having a way to set the motor voltage.
Copy the `VoltageOut` objects from `DrivetrainSubsystem` below the `TalonFX`s.

```Java
TalonFX leftFalcon = new TalonFX(Constants.drivetrainLeftFalconID);
TalonFX rightFalcon = new TalonFX(Constants.drivetrainRightFalconID);

VoltageOut leftVoltage = new VoltageOut(0);
VoltageOut rightVoltage = new VoltageOut(0);
```

Then copy the `setVoltages` method from `DrivetrainSubsystem` into the `setVoltage` method.

```Java
@Override
public void setVolts(double left, double right) {
    leftFalcon.setControl(leftVoltage.withOutput(left));
    rightFalcon.setControl(rightVoltage.withOutput(right));
}
```

Finally we need to add a physics sim.
This will take these motor voltages and figure out how the mechanims might actually behave using a mathematical model.
WPILib provides the `DifferentialDrivetrainSim` class for this.
This class has the `createKitbotSim()` static method to provide a convenient way to set this up with the physical constants for a kitbot.

Start by creating a new `DifferentialDrivetrainSim` called `physicsSim` under the `VoltageOut`s.

```Java
DifferentialDrivetrainSim physicsSim = DifferentialDrivetrainSim.createKitbotSim(
    null,
    null,
    null,
    null);
```

The first value in this constructor is the motor for the sim.
We can use `KitbotMotor.kDoubleFalcon500PerSide` to generate the constants for this.
This model assumes we have 2 motors on each side of the drivetrain, although we only programmed one.
For a sim this doesn't matter, but for a real robot we would need to add follower motors.
We are going to ignore that for this tutorial.

```Java
DifferentialDrivetrainSim physicsSim = DifferentialDrivetrainSim.createKitbotSim(
    KitbotMotor.kDoubleFalcon500PerSide,
    null,
    null,
    null);
```

The second parameter is the gearing of the drivetrain.
This is something we normally would ask mechanical for.
In this case, since the drivetrain is simulated, we can use the standard 8p45 gearbox.

```Java
DifferentialDrivetrainSim physicsSim = DifferentialDrivetrainSim.createKitbotSim(
    KitbotMotor.kDoubleFalcon500PerSide,
    KitbotGearing.k8p45,
    null,
    null);
```

The third parameter is the size of the wheels.
By default the kitbot comes with 6 inch wheels.

```Java
DifferentialDrivetrainSim physicsSim = DifferentialDrivetrainSim.createKitbotSim(
    KitbotMotor.kDoubleFalcon500PerSide,
    KitbotGearing.k8p45,
    KitbotWheelSize.kSixInch,
    null);
```

The final parameter is the "measurement standard deviations".
This is a way for us to add uncertainty to our simulated model in a way that matches real sensor noise.
For this exercise we can ignore it and leave it as null.

Now we have a fully set up physics sim.
On real robots we often have to be more careful when finding these physical constans, since they aren't usually so standard, but the kitbot is nice and easy.
Make sure to work with mechanical to find these constants in season.

Next we need to actually use the physics sim.
In `updateInputs` add a call to `physicsSim.update()`.

```Java
@Override
    public void updateInputs(DrivetrainIOInputs inputs) {
    physicsSim.update(0.020);

    var leftSimState = leftFalcon.getSimState();
    var rightSimState = rightFalcon.getSimState();
    // Snip
}
```

This method runs the simulation for the number of seconds passed in.
We use 20 milliseconds, which is the default loop time of the rio.

Next we need up update the inputs to the sim based on the motor voltages.
Call `physicsSim.setInputs()` with the motor sim state voltages.

```Java
@Override
    public void updateInputs(DrivetrainIOInputs inputs) {
    physicsSim.update(0.020);

    var leftSimState = leftFalcon.getSimState();
    var rightSimState = rightFalcon.getSimState();

    physicsSim.setInputs(leftSimState.getMotorVoltage(), rightSimState.getMotorVoltage());
    // Snip
}
```

We're almost done with `DrivetrainIOSim`!
Now we just need to update the rest of the IOInputs based on the simulator.
Start by making the wheel speeds update with the `physicsSim.getLeftVelocityMetersPerSecond`.
Then update the wheel positions with `physicsSim.getLeftPositionMeters`.

```Java
@Override
public void updateInputs(DrivetrainIOInputs inputs) {
    // Snip
    inputs.leftVelocityMetersPerSecond = physicsSim.getLeftVelocityMetersPerSecond();
    inputs.rightVelocityMetersPerSecond = physicsSim.getRightVelocityMetersPerSecond();

    inputs.leftPositionMeters = physicsSim.getLeftPositionMeters();
    inputs.rightPositionMeters = physicsSim.getRightPositionMeters();
    // Snip
}
```

Theres one more thing we should do to help make this sim realistic.
When our motors are running the voltage of the battery will "sag" or drop.
This can reduce our motor's output in the extreme case.
To simulate this use `simState.setSupplyVoltage(RoboRioSim.getVInVoltage())`.
`setSupplyVoltage` will control how much voltage is available to the motor simulation.
`RoboRioSim.getVInVoltage()` will record how much battery our motors should be using, and the effect that will have on the battery.

```Java
@Override
public void updateInputs(DrivetrainIOInputs inputs) {
    physicsSim.update(0.020);

    var leftSimState = leftFalcon.getSimState();
    leftSimState.setSupplyVoltage(RoboRioSim.getVInVoltage());

    var rightSimState = rightFalcon.getSimState();
    rightSimState.setSupplyVoltage(RoboRioSim.getVInVoltage());
    // Snip
}
```

Now we're done with the `DrivetrainIOSim` class! 🎉

Let's refactor our `DrivetrainSubsystem` to finish off this rewrite.

First, we need to replace all the functionality that moved to `DrivetrainIOSim` with an instance of `DrivetrainIOSim`.
Get rid of the `TalonFX` objects and `VoltageOut` requests.

```Java
public class DrivetrainSubsystem extends SubsystemBase {
    DrivetrainIO io = new DrivetrainIOSim();
    // Snip
}
```

Then we should add an `IOInputs` object to track the inputs to the subsystem.
Theres a catch though: since we used `@AutoLog` we have to use `DrivetrainIOInputsAutoLogged`, otherwise it won't properly log.

```Java
public class DrivetrainSubsystem extends SubsystemBase {
    DrivetrainIO io = new DrivetrainIOSim();
    DrivetrainIOInputsAutoLogged inputs = new DrivetrainIOInputsAutoLogged();
}
```

Now we need to finish the AdvantageKit io setup.
Add a call to `io.updateInputs(inputs)` in the `periodic()` method of `DrivetrainSubsystem`.

```Java
@Override
public void periodic() {
    io.updateInputs(inputs);
}
```

Then we need to push those inputs to the log.
We do this by calling `Logger.getInstance().processInputs("Drivetrain", inputs)`.

```Java
@Override
public void periodic() {
    io.updateInputs(inputs);
    Logger.getInstance().processInputs("Drivetrain", inputs);
}
```

Finally, lets change the `setVoltages` method to use the io layer.

```Java
private void setVoltages(double left, double right) {
    io.setVolts(left, right);
}
```

This will work as a sim!
But it won't have any way to track the position of the drivebase.
Remember that we can use odometry to track the position of a drivebase.
We can create a new `DifferentialDriveOdometry` object in `DrivetrainSubsystem` to handle this.

```Java
public class DrivetrainSubsystem extends SubsystemBase {
  DrivetrainIO io = new DrivetrainIOSim();
  DrivetrainIOInputsAutoLogged inputs = new DrivetrainIOInputsAutoLogged();

  DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(new Rotation2d(), 0, 0);
  // Snip
}
```

We will need to update this every processor loop.
Normally we would have a gyro to track the heading (rotation) of the drive base.
However, in simulation we don't have an easy way to make a gyro that behaves how we would expect.
Instead, we can use the following line to fudge it:

```Java
odometry.getPoseMeters().getRotation()
    .plus(
        Rotation2d.fromRadians(
            (inputs.leftSpeedMetersPerSecond - inputs.rightSpeedMetersPerSecond)
             * 0.020 / Units.inchesToMeters(26)))
```

This line is using a small piece of math to calculate what the new rotation should be based off of our wheel speeds.
You don't need to know how this works in particular, just know it's a bit of a hack.

We are going to wrap this in an `odometry.update()` call.

```Java
@Override
public void periodic() {
io.updateInputs(inputs);
Logger.getInstance().processInputs("Drivetrain", inputs);

odometry.update(
    odometry.getPoseMeters().getRotation()
        // Use differential drive kinematics to find the rotation rate based on the wheel speeds and distance between wheels
        .plus(Rotation2d.fromRadians((inputs.leftVelocityMetersPerSecond - inputs.rightVelocityMetersPerSecond)
            * 0.020 / Units.inchesToMeters(26))),
    inputs.leftPositionMeters, inputs.rightPositionMeters);
}
```

Finally, we need to send the odometry pose to the logger so we can visualize it.
We do this using `Logger.getInstance().recordOutput()` and `odometry.getPoseMeters()`.

```Java
@Override
public void periodic() {
    // Snip
    Logger.getInstance().recordOutput("Drivebase Pose", odometry.getPoseMeters());
}
```

This should be everything you need to have a full drivebase simulation!

### Running the Simulation

To run a robot code simulation, click on the WPILib icon in the top right corner.
Then find or search for "Simulate Robot Code" and press it.
Once the code builds, it will prompt you to choose between using the driver station and sim gui.
Choose the sim gui.

The sim gui is perfectly serviceable for testing, but we can do better.
AdvantageScope supports connecting to a simulation to visualize IO.
When you open AdvantageScope, hit "file", then "Connect to Simulator".
This lets you visualize data from the simulator in the same way as you would with a real robot.
You can use the 3d field visualization to have a fun way to show the robot on the field.

To drive the robot, add "keyboard 0" to "joystick 0" in the sim gui.
You might have to change your keyboard settings in the sim gui to make it properly emulate a joystick.
Then make sure to set the mode to teleop.
